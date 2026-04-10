package ma.fst.projet2societe.repositories;
import java.util.Optional;
import ma.fst.projet2societe.entities.Affectation;
import ma.fst.projet2societe.entities.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, AffectationId> {

    @Query("SELECT a FROM Affectation a WHERE a.id.employeId = :employeId")
    List<Affectation> findByEmployeId(@Param("employeId") Long employeId);

    @Query("SELECT a FROM Affectation a WHERE a.id.phaseId = :phaseId")
    List<Affectation> findByPhaseId(@Param("phaseId") Long phaseId);

    @Query("SELECT a FROM Affectation a WHERE a.id.phaseId = :phaseId AND a.id.employeId = :employeId")
    Optional<Affectation> findByPhaseIdAndEmployeId(@Param("phaseId") Long phaseId, @Param("employeId") Long employeId);
}