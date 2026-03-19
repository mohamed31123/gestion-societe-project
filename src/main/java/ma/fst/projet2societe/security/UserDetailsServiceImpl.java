package ma.fst.projet2societe.security;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.repositories.EmployeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeRepository employeRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Aucun employé trouvé avec le login : " + login));

        // Le rôle vient du libellé du profil (ex: ADMIN, COMPTABLE, CHEF_PROJET...)
        String role = "ROLE_USER";
        if (employe.getProfil() != null && employe.getProfil().getLibelle() != null) {
            role = "ROLE_" + employe.getProfil().getLibelle().toUpperCase();
        }

        return User.builder()
                .username(employe.getLogin())
                .password(employe.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .build();
    }
}
