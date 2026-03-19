package ma.fst.projet2societe.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String libelle;

    private String description;

    private String chemin;
    private String nomFichier ;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}