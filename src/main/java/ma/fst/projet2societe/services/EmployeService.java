package ma.fst.projet2societe.services;

import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    public List<Employe> getAll() {
        return employeRepository.findAll();
    }

    public Employe getById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
    }

    public Employe create(Employe employe) {
        if (employeRepository.findByMatricule(employe.getMatricule()).isPresent()) {
            throw new RuntimeException("Matricule déjà existant");
        }
        if (employeRepository.findByLogin(employe.getLogin()).isPresent()) {
            throw new RuntimeException("Login déjà existant");
        }
        if (employeRepository.findByEmail(employe.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà existant");
        }
        return employeRepository.save(employe);
    }

    public Employe update(Long id, Employe employe) {
        Employe existing = getById(id);
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setTelephone(employe.getTelephone());
        existing.setEmail(employe.getEmail());
        existing.setLogin(employe.getLogin());
        existing.setProfil(employe.getProfil());
        return employeRepository.save(existing);
    }

    public void delete(Long id) {
        employeRepository.deleteById(id);
    }

    // recherche par les dates debut et fin
    public List<Employe> getDisponibles(String dateDebut, String dateFin) {
        LocalDate debut = LocalDate.parse(dateDebut);
        LocalDate fin = LocalDate.parse(dateFin);
        return employeRepository.findEmployesDisponibles(debut, fin);
    }
}