package ma.fst.projet2societe.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ProjetRequest {

    @NotBlank(message = "Le nom du projet est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
    private Date dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
    private Date dateFin;

    @NotNull(message = "Le montant est obligatoire")
    @Min(value = 0, message = "Le montant doit être positif")
    private Double montant;

    @Size(max = 500, message = "La description ne peut dépasser 500 caractères")
    private String description;

    @NotNull(message = "L'organisme associé est obligatoire")
    private Long idOrganisme;

    @NotNull(message = "L'employé associé est obligatoire")
    private Long idEmploye;
}