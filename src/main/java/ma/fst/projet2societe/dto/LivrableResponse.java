package ma.fst.projet2societe.dto;

import lombok.Data;

@Data
public class LivrableResponse {

    private Long   id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;

    // metadonnees des  fichier
    private String nomFichier;
    private String contentType;
    private Long   tailleFichier;
    private boolean fichierPresent; // true si un fichier a ete uploade

    // infos phase parente
    private Long   phaseId;
    private String phaseLibelle;

    // infos project
    private Long   projectId;
    private String projectNom;

    // Lien de telechargement si le fichiier est present
    private String downloadUrl;
}