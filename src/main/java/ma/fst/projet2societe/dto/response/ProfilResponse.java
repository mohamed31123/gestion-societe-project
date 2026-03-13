package ma.fst.projet2societe.dto.response;

import lombok.Data;

@Data
public class ProfilResponse {

    private Long id;
    private String code;
    private String libelle;

    // informations de l'employe lie au profil
    private Long employeId;
    private String employeNom;
    private String employePrenom;

}