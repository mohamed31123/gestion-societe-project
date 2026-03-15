package ma.fst.projet2societe.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livrable")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Livrable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;


    @ManyToOne
    @JoinColumn(name = "idPhase")
    private Phase phase ;

    private String nomFichier;
    private String contentType;
    private Long   tailleFichier;


}
