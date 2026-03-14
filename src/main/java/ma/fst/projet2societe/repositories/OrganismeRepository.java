package ma.fst.projet2societe.repositories ;

import ma.fst.projet2societe.entities.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganismeRepository extends JpaRepository<Organisme, Long> {
    Optional<Organisme> findByCode(String code);
    boolean existsByCode(String code);
    List<Organisme> findByNom(String nom);
    List<Organisme> findByNomContact(String nomContact);

}
