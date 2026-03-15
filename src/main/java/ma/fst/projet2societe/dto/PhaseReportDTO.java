package ma.fst.projet2societe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class PhaseReportDTO {

    private Long    id;
    private String  code;
    private String  libelle;
    private String  description;
    private Date    dateDebut;
    private Date    dateFin;
    private Double  montant;

    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    private Long   projectId;
    private String projectNom;
    private String projectCode;

    private Long   chefProjectId;
    private String chefProjectNom;
    private String chefProjectPrenom;

    private String organismeNom;

    private int nombreLivrables;
}