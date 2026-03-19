package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByCode(String code);

    Optional<Profil> findByLibelle(String libelle);
}
