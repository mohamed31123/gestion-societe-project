package ma.fst.projet2societe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrganismeRequest {

    @NotBlank(message = "Le nom de l'organisme est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    private String code;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse est trop longue")
    private String address;

    @NotBlank(message = "Le nom du contact est obligatoire")
    @Size(min = 2, max = 100)
    private String nomContact;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String nomEmail;

    @Size(max = 150, message = "URL du site web trop longue")
    private String siteWeb;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @NotBlank(message = "L'identifiant de l'employé est obligatoire")
    private String idEmploye;
}