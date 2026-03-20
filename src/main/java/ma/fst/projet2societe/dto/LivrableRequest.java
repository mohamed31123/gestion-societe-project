package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivrableRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    private String description;
}
