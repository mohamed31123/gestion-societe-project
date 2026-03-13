package ma.fst.projet2societe.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

@Embeddable
public class AffectationId implements Serializable {

    private Long employeId;
    private Long phaseId;


}