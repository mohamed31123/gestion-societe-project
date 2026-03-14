package ma.fst.projet2societe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;

    @Size(max = 50, message = "La description ne doit pas dépasser 50 caractères")
    private String descteption;


    // il vient du @PathVariable projetId dans l'URL
    // POST /api/projets/{projetId}/documents
}