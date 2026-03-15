package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {
    private Long id;

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    private Date dateFacture;
    private Long phaseId;
    private String phaseCode;
    private String phaseLibelle;
    private boolean etatPaiement;
}