package ma.fst.projet2societe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Affectation {

    @EmbeddedId
    private AffectationId id ;

    @Temporal(TemporalType.DATE)
    private Date datedebut;
    @Temporal(TemporalType.DATE)
    private Date datefin;


    @ManyToOne
    @MapsId("employeId")
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne
    @MapsId("phaseId")
    @JoinColumn(name = "phase_id")
    private Phase phase;


}