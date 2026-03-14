package ma.fst.projet2societe.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResume {

    private Long id;
    private String code;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private double montant;

    private int nombrePhases;
    private int phasesTerminees;
    private int phasesFacturees;
    private int phasesPaees;

    private String organismeNom;
    private String chefProjectNom;
    private String chefProjectPrenom;
}