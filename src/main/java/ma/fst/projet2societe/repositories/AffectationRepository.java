package ma.fst.projet2societe.repositories;
import java.util.Optional;
import ma.fst.projet2societe.entities.Affectation;
import ma.fst.projet2societe.entities.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, AffectationId> {

    List<Affectation> findByEmployeId(Long employeId);
    List<Affectation> findByPhaseId(Long phaseId);
    Optional<Affectation> findByPhaseIdAndEmployeId(Long phaseId, Long employeId);
}