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
    }

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

    private void seedAdmin() {
        if (employeRepository.findByLogin("admin").isEmpty()) {
            Profil adminProfil = profilRepository.findByCode("ADMIN").orElse(null);
            Employe admin = new Employe();
            admin.setMatricule("EMP000");
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@societe.ma");
            admin.setTelephone("0600000000");
            admin.setAdresse("Siege social");
            admin.setProfil(adminProfil);
            employeRepository.save(admin);
            log.info("=== Compte admin créé : login=admin / password=admin123 ===");
        }
    }
}