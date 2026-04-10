package ma.fst.projet2societe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationId implements Serializable {

    @Column(name = "employe_id")
    private Long employeId;

    @Column(name = "phase_id")
    private Long phaseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffectationId that = (AffectationId) o;
        return Objects.equals(employeId, that.employeId) &&
                Objects.equals(phaseId, that.phaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeId, phaseId);
    }
}