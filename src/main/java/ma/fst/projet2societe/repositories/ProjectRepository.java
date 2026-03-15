package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByCode(String code);



    //les projects en cours
    @Query("""
    SELECT p FROM Project p
    LEFT JOIN FETCH p.employe emp
    LEFT JOIN FETCH p.organisme org
    WHERE p.dateDebut <= :today
      AND p.dateFin   >= :today
      AND (:chefProjectId IS NULL OR emp.id = :chefProjectId)
      AND (:organismeId   IS NULL OR org.id = :organismeId)
    """)
    Page<Project> findEnCours(
            @Param("today")         Date today,
            @Param("chefProjectId") Long chefProjectId,
            @Param("organismeId")   Long organismeId,
            Pageable pageable);

    //  Projects clotures : dateFin < aujourd'hui
    @Query("""
    SELECT p FROM Project p
    LEFT JOIN FETCH p.employe emp
    LEFT JOIN FETCH p.organisme org
    WHERE p.dateFin < :today
      AND (:chefProjectId IS NULL OR emp.id = :chefProjectId)
      AND (:organismeId   IS NULL OR org.id = :organismeId)
    """)
    Page<Project> findClotures(
            @Param("today")         Date today,
            @Param("chefProjectId") Long chefProjectId,
            @Param("organismeId")   Long organismeId,
            Pageable pageable);

        //les statistiques pour le tableau de bord
    @Query("SELECT COUNT(p) FROM Project p WHERE p.dateDebut <= :today AND p.dateFin >= :today")
    long countEnCours(@Param("today") Date today);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.dateFin < :today")
    long countClotures(@Param("today") Date today);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.dateDebut > :today")
    long countAVenir(@Param("today") Date today);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Project p")
    Double sumMontantTotal();
}
