package ma.fst.projet2societe.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employes")
@Getter
@Setter
@ToString(exclude = {"profil", "projects", "affectations"})
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
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_profil")
    private Profil profil;

    //la relation entre projet et employe
    @JsonIgnore
    @OneToMany(mappedBy = "employe")
    private List<Project> projects = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Affectation> affectations = new ArrayList<>();


}