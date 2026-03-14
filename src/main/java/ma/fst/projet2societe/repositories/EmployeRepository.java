package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByMatricule(String matricule);
    Optional<Employe> findByLogin(String login);
    Optional<Employe> findByEmail(String email);
    List<Employe> findByNomContaining(String nom);
    List<Employe> findByProjectsId(Long id);
}