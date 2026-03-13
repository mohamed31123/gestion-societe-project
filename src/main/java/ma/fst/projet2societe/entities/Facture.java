package ma.fst.projet2societe.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "factures")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Temporal(TemporalType.DATE)
    private Date dateFacture;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
}