package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;


@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    @Query("SELECT DISTINCT e FROM Employe e WHERE e.id NOT IN " +
            "(SELECT a.employe.id FROM Affectation a WHERE " +
            "a.datedebut <= :dateFin AND a.datefin >= :dateDebut)")
    List<Employe> findEmployesDisponibles(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    Optional<Employe> findByMatricule(String matricule);
    Optional<Employe> findByLogin(String login);
    Optional<Employe> findByEmail(String email);
    List<Employe> findByNomContaining(String nom);
    List<Employe> findByProjectsId(Long id);

}


