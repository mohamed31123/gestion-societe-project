package ma.fst.projet2societe.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private double montant;

    private Long organismeId;
    private String organismeNom;

    private Long chefProjectId;
    private String chefProjectNom;
    private String chefProjectPrenom;

    private int nombrePhases;
}