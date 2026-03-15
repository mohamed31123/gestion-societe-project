package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivrableRequest {

    @NotBlank
    private String code;
    private String libelle;
    private String description;


}