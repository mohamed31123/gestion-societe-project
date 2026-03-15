package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Phase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    // recherche des phases terminées non facturées
    List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

    // recherche des phases facturées non payées
    List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();

    // recherche des phases d'un project
    List<Phase> findByProjectId(Long projectId);


    //  les Phases terminées non facturees
    @Query("""
    SELECT p FROM Phase p
    JOIN FETCH p.project proj
    LEFT JOIN FETCH proj.employe emp
    LEFT JOIN FETCH proj.organisme org
    WHERE p.etatRealisation = true
      AND p.etatFacturation = false
      AND (:projectId     IS NULL OR proj.id = :projectId)
      AND (:chefProjectId IS NULL OR emp.id  = :chefProjectId)
      AND (:dateDebut     IS NULL OR p.dateDebut >= :dateDebut)
      AND (:dateFin       IS NULL OR p.dateFin   <= :dateFin)
    """)
    Page<Phase> findTermineesNonFacturees(
            @Param("projectId")     Long projectId,
            @Param("chefProjectId") Long chefProjectId,
            @Param("dateDebut")     Date dateDebut,
            @Param("dateFin")       Date dateFin,
            Pageable pageable);


    //  les phases facturees non payees
    @Query("""
    SELECT p FROM Phase p
    JOIN FETCH p.project proj
    LEFT JOIN FETCH proj.employe emp
    LEFT JOIN FETCH proj.organisme org
    WHERE p.etatFacturation = true
      AND p.etatPaiement = false
      AND (:projectId     IS NULL OR proj.id = :projectId)
      AND (:chefProjectId IS NULL OR emp.id  = :chefProjectId)
      AND (:dateDebut     IS NULL OR p.dateDebut >= :dateDebut)
      AND (:dateFin       IS NULL OR p.dateFin   <= :dateFin)
    """)
    Page<Phase> findFactureesNonPayees(
            @Param("projectId")     Long projectId,
            @Param("chefProjectId") Long chefProjectId,
            @Param("dateDebut")     Date dateDebut,
            @Param("dateFin")       Date dateFin,
            Pageable pageable);

        // les phases payees
    @Query("""
    SELECT p FROM Phase p
    JOIN FETCH p.project proj
    LEFT JOIN FETCH proj.employe emp
    LEFT JOIN FETCH proj.organisme org
    WHERE p.etatPaiement = true
      AND (:projectId     IS NULL OR proj.id = :projectId)
      AND (:chefProjectId IS NULL OR emp.id  = :chefProjectId)
      AND (:dateDebut     IS NULL OR p.dateDebut >= :dateDebut)
      AND (:dateFin       IS NULL OR p.dateFin   <= :dateFin)
    """)
    Page<Phase> findPayees(
            @Param("projectId")     Long projectId,
            @Param("chefProjectId") Long chefProjectId,
            @Param("dateDebut")     Date dateDebut,
            @Param("dateFin") Date dateFin,
            Pageable pageable);


        //  les statistiques pour le tableau de bord
    long countByEtatRealisationTrue();
    long countByEtatRealisationFalse();
    long countByEtatRealisationTrueAndEtatFacturationFalse();
    long countByEtatFacturationTrueAndEtatPaiementFalse();
    long countByEtatPaiementTrue();

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Phase p")
    Double sumMontantTotal();

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Phase p WHERE p.etatFacturation = true")
    Double sumMontantFacture();

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Phase p WHERE p.etatPaiement = true")
    Double sumMontantPaye();



}