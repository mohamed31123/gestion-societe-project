package ma.fst.projet2societe.dto.response;


import lombok.Data;

@Data
public class EmployeResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private String profilNom ;
}
