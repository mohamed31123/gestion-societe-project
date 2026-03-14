package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation,Long> {

    //recherche des affectations d'un employé
    List<Affectation> findByEmployeId(Long employeId);

}
