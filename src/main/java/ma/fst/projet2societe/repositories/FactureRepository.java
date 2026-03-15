package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Facture;
import ma.fst.projet2societe.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;



public interface FactureRepository extends JpaRepository<Facture,Long> {

    // phases terminées non facturées
    @Query("SELECT p FROM Phase p WHERE p.etatRealisation = true AND p.etatFacturation = false")
    List<Phase> findPhasesTermineesNonFacturees();

    // phases facturées non payées
    @Query("SELECT p FROM Phase p WHERE p.etatFacturation = true AND p.etatPaiement = false")
    List<Phase> findPhasesFactureesNonPayees();

    // phases payées
    @Query("SELECT p FROM Phase p WHERE p.etatPaiement = true")
    List<Phase> findPhasesPayees();
}
