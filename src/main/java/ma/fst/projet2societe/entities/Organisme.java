package ma.fst.projet2societe.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "organisme")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Organisme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String code ;
    private String telephone ;
    private String address ;
    private String nomContact ;
    private String nomEmail;
    private String siteWeb ;
    //la relation entre l"organisme et les projets
    //une organisme peut avoir pluieurs projets
    @OneToMany(mappedBy = "organisme")
    private List<Project> projects ;
    //la relation entre projet employe
    //=====================
    @ManyToOne
    @JoinColumn(name = "idEmploye")
    private Employe employe ;
}
