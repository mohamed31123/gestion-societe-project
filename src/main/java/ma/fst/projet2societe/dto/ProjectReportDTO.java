package ma.fst.projet2societe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class ProjectReportDTO {

    private Long   id;
    private String code;
    private String nom;
    private String description;
    private Date   dateDebut;
    private Date   dateFin;
    private Double montant;

    private Long   chefProjectId;
    private String chefProjectNom;
    private String chefProjectPrenom;

    private Long   organismeId;
    private String organismeNom;

    private int    nombrePhases;
    private int    phasesTerminees;
    private int    phasesFacturees;
    private int    phasesPaees;

    private Double montantTotalPhases;
    private Double montantFacture;
    private Double montantPaye;
    private Double montantRestant;

    private double tauxRealisation;
    private double tauxFacturation;
    private double tauxPaiement;
}