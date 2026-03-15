package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDTO {
    private Long employeId;
    private Long phaseId;

    @NotNull(message = "La date de début est obligatoire")
    private Date datedebut;

    @NotNull(message = "La date de fin est obligatoire")
    private Date datefin;
}