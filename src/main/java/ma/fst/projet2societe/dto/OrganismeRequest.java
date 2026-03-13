package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrganismeRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String nomEmail;

    @NotBlank(message = "Le nom du contact est obligatoire")
    @Size(min = 2, max = 100)
    private String nomContact;

    @Pattern(
            regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "URL du site web invalide"
    )
    private String siteWeb;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(
            regexp = "^[0-9+\\- ]{8,15}$",
            message = "Numéro de téléphone invalide"
    )
    private String telephone;

    @NotNull(message = "L'id de l'employé est obligatoire")
    private Long idEmploye;
}