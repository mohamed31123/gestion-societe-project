package ma.fst.projet2societe.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "employes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name = "idProfil")
    private Profil profil;
    //la relation entre projet et employe
    @OneToMany(mappedBy = "employe")
    private List<Project> projects;


}
