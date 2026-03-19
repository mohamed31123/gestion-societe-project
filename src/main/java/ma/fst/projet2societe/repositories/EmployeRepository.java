package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByMatricule(String matricule);
    Optional<Employe> findByLogin(String login);
    Optional<Employe> findByEmail(String email);
    List<Employe> findByNomContaining(String nom);

    // Charge l'employé avec son profil en une seule requête — pour l'auth JWT
    @Query("SELECT e FROM Employe e LEFT JOIN FETCH e.profil WHERE e.login = :login")
    Optional<Employe> findByLoginWithProfil(@Param("login") String login);

    @Query("SELECT DISTINCT e FROM Employe e WHERE e.id NOT IN " +
            "(SELECT a.employe.id FROM Affectation a WHERE " +
            "a.datedebut <= :dateFin AND a.datefin >= :dateDebut)")
    List<Employe> findEmployesDisponibles(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin")   LocalDate dateFin);
}
