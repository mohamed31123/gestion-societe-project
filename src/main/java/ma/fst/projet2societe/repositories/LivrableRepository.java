package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivrableRepository extends JpaRepository<Livrable,Long> {


    Optional<Livrable> findByCode(String code);

    Optional<Livrable> findByPhaseId(Long phaseId);
}
