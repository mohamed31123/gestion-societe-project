package ma.fst.projet2societe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfilRequest {

    @NotBlank(message = "Le code du profil est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(min = 2, max = 50, message = "Le libellé doit contenir entre 2 et 50 caractères")
    private String libelle;

    @NotNull(message = "L'employé associé est obligatoire")
    private Long idEmploye;
}