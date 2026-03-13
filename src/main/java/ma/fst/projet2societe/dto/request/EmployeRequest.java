package ma.fst.projet2societe.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeRequest {

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 2, max = 20, message = "Le matricule doit contenir entre 2 et 20 caractères")
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50)
    private String prenom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 200)
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Entrez le nom d'utilisateur")
    @Size(min = 4, max = 30)
    private String login;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 100, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @NotNull(message = "Le profil est obligatoire")
    private Long idProfil;
}