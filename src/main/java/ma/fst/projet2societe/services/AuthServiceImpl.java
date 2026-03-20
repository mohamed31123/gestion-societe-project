package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;
    private final JwtUtils              jwtUtils;
    private final EmployeRepository     employeRepository;
    private final PasswordEncoder       passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        log.info("Tentative de connexion pour le login : {}", request.getLogin());

        // 1. Vérifier que l'employé existe
        Employe employe = employeRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> {
                    log.warn("Login introuvable : {}", request.getLogin());
                    return new BadCredentialsException("Login ou mot de passe incorrect");
                });

        log.info("Employé trouvé : {} — profil : {}",
                employe.getLogin(),
                employe.getProfil() != null ? employe.getProfil().getCode() : "NULL");

        // 2. Vérifier le mot de passe directement avec BCrypt
        if (!passwordEncoder.matches(request.getPassword(), employe.getPassword())) {
            log.warn("Mot de passe incorrect pour : {}", request.getLogin());
            throw new BadCredentialsException("Login ou mot de passe incorrect");
        }

        // 3. Authentifier via Spring Security (pour les logs et le contexte)
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );
            log.info("Authentification Spring Security réussie pour : {}", request.getLogin());
        } catch (BadCredentialsException e) {
            log.error("Échec authentification Spring Security pour {} : {}", request.getLogin(), e.getMessage());
            throw new BadCredentialsException("Login ou mot de passe incorrect");
        } catch (DisabledException e) {
            log.error("Compte désactivé : {}", request.getLogin());
            throw new BadCredentialsException("Compte désactivé");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification pour {} : {}", request.getLogin(), e.getMessage(), e);
            throw new BadCredentialsException("Erreur d'authentification : " + e.getMessage());
        }

        // 4. Générer le token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
        String token = jwtUtils.generateToken(userDetails);

        // 5. Déterminer le rôle
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        log.info("Token JWT généré pour {} avec le rôle {}", request.getLogin(), role);

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(employe.getId())
                .login(employe.getLogin())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .email(employe.getEmail())
                .role(role)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MeResponse getMe(String login) {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        String role      = "USER";
        String profilCode = null;

        if (employe.getProfil() != null) {
            role      = employe.getProfil().getLibelle() != null
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
    @Transactional
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
        log.info("Mot de passe changé avec succès pour : {}", login);
    }
}