package ma.fst.projet2societe.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PhaseResponse {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private Double montant;
    private Double pourcentage;
    private Integer ordre;

    // États du workflow
    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    // Infos du projet parent
    private Long projetId;
    private String projetCode;

}