package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class PhaseRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 20, message = "Le code ne doit pas dépasser 20 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private Date dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private Date dateFin;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être strictement positif")
    @DecimalMax(value = "999999999.99", message = "Le montant est trop élevé")
    private Double montant;





    // ⚠️ etatRealisation, etatFacturation, etatPaiement
    // ne sont PAS inclus ici — ils sont gérés via les PATCH :
    //   PATCH /api/phases/{id}/realisation
    //   PATCH /api/phases/{id}/facturation
    //   PATCH /api/phases/{id}/paiement
}