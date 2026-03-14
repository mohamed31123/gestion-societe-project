package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = " Le code est obligatoire ")
    private String code;

    @NotBlank(message = " Le nom est obligatoire ")
    private String nom;

    private String description;

    @NotNull(message = " La date de début est obligatoire ")
    private Date dateDebut;

    @NotNull(message = " La date de fin est obligatoire " )
    private Date dateFin;

    @Positive(message = " Le montant doit être positif ")
    private double montant;

    @NotNull(message = " L'organisme est obligatoire ")
    private Long organismeId;

    @NotNull(message = " Le chef de projet est obligatoire ")
    private Long chefProjectId;


}