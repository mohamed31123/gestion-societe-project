package ma.fst.projet2societe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.entities.Profil;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.repositories.ProfilRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final EmployeRepository employeRepository;
    private final ProfilRepository  profilRepository;
    private final PasswordEncoder   passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedProfils();
        seedAdmin();
        migratePasswords();
    }

    /** Crée les 5 profils métier s'ils n'existent pas */
    private void seedProfils() {
        String[] codes = {"ADMIN", "SECRETAIRE", "DIRECTEUR", "CHEF_PROJET", "COMPTABLE"};
        for (String code : codes) {
            if (profilRepository.findByCode(code).isEmpty()) {
                Profil p = new Profil();
                p.setCode(code);
                p.setLibelle(code);
                profilRepository.save(p);
                log.info("Profil créé : {}", code);
            }
        }
    }

    /** Crée un compte admin par défaut si la table est vide */
    private void seedAdmin() {
        if (employeRepository.count() == 0) {
            Profil adminProfil = profilRepository.findByCode("ADMIN").orElse(null);
            Employe admin = new Employe();
            admin.setMatricule("EMP001");
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@societe.ma");
            admin.setTelephone("0600000000");
            admin.setAdresse("Siège social");
            admin.setProfil(adminProfil);
            employeRepository.save(admin);
            log.info("=== Compte admin créé — login: admin / password: admin123 ===");
        }
    }

    /** Migration : hashe les mots de passe en clair au démarrage */
    private void migratePasswords() {
        List<Employe> all = employeRepository.findAll();
        int n = 0;
        for (Employe e : all) {
            String pwd = e.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$")) {
                e.setPassword(passwordEncoder.encode(pwd));
                employeRepository.save(e);
                n++;
            }
        }
        if (n > 0) log.info("Migration : {} mot(s) de passe hashé(s)", n);
    }
}
