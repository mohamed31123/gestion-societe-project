package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    // recherche des phases terminées non facturées
    List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

    // recherche des phases facturées non payées
    List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();

    // recherche des phases d'un projet
    List<Phase> findByProjetId(Long projetId);



}