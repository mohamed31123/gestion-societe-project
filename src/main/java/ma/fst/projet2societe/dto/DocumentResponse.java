package ma.fst.projet2societe.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentResponse {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private String nomFichier;
    private Long tailleFichier;
    private LocalDateTime dateUpload;
    private String downloadUrl;
    private Long projectId;
    private String projectNom;
}
