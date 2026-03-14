package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivrableRepository extends JpaRepository<Livrable,Long> {

    List<Livrable> findByPhaseId(Long phaseId);


}
