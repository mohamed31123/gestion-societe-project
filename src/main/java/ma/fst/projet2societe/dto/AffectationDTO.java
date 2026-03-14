package ma.fst.projet2societe.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDTO {
    private Long employeId;
    private Long phaseId;
    private Date datedebut;
    private Date datefin;

}