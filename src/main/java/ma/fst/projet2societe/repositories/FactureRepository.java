package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Long> {
}
