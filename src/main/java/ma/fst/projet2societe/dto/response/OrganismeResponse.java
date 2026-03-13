package ma.fst.projet2societe.dto.response;

import lombok.Data;

@Data
public class OrganismeResponse {

    private Long id;
    private String nom;
    private String code;
    private String address;
    private String nomContact;
    private String nomEmail;
    private String siteWeb;
    private String telephone;

    // informations de l'employee responsable
    private Long employeId;
    private String employeNom;
    private String employePrenom;

}