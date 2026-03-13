package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganismeRepository extends JpaRepository<Organisme, Long> {
}
