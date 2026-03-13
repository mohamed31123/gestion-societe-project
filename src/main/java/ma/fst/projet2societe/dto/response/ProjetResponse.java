package ma.fst.projet2societe.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class ProjetResponse {

    private Long id;
    @NotNull
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private Double montant;
    private String description;

    // informations de l'organisme
    private Long organismeId;
    private String organismeNom;

    // informations de l'employé responsable
    private Long employeId;
    private String employeNom;
    private String employePrenom;

}