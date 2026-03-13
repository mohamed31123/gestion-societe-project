package ma.fst.projet2societe.dto;

import lombok.Data;

@Data
public class OrganismeResponse {

    private Long id;
    private String code;
    private String nom;
    private String nomEmail;
    private String nomContact;
    private String siteWeb;
    private String telephone;
    private Long idEmploye;

}