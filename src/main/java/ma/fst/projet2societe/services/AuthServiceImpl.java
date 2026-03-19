package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.auth.ChangePasswordRequest;
import ma.fst.projet2societe.dto.auth.LoginRequest;
import ma.fst.projet2societe.dto.auth.LoginResponse;
import ma.fst.projet2societe.dto.auth.MeResponse;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MeResponse getMe(String login) {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        String role = "USER";
        String profilCode = null;
        if (employe.getProfil() != null) {
            role = employe.getProfil().getLibelle() != null
                    ? employe.getProfil().getLibelle().toUpperCase()
                    : "USER";
            profilCode = employe.getProfil().getCode();
        }

        return MeResponse.builder()
                .id(employe.getId())
                .matricule(employe.getMatricule())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .email(employe.getEmail())
                .telephone(employe.getTelephone())
                .login(employe.getLogin())
                .role(role)
                .profilCode(profilCode)
                .build();
    }

    @Override
    public void changePassword(String login, ChangePasswordRequest request) {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        if (!passwordEncoder.matches(request.getAncienPassword(), employe.getPassword())) {
            throw new BusinessException("L'ancien mot de passe est incorrect");
        }

        if (!request.getNouveauPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("La confirmation du mot de passe ne correspond pas");
        }

        employe.setPassword(passwordEncoder.encode(request.getNouveauPassword()));
        employeRepository.save(employe);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Employe employe = employeRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        System.out.println("=== PASSWORD EN BASE : " + employe.getPassword());
        System.out.println("=== MATCH : " + passwordEncoder.matches(request.getPassword(), employe.getPassword()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new BusinessException("Login ou mot de passe incorrect");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

        String token = jwtUtils.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .build();
    }
}