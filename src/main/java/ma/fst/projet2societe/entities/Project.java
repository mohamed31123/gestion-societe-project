package ma.fst.projet2societe.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projet")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private Double montant;

    //la relation entre projet et organisme
    @ManyToOne
    @JoinColumn(name = "idOrganisme")
    private Organisme organisme;

    //la relation entre l'employe et les projets
    @ManyToOne
    @JoinColumn(name = "idEmploye")
    private Employe employe;

    // La relation entre projet et phase
    @OneToMany(mappedBy = "project", cascade =  CascadeType.ALL, orphanRemoval = true)
    List<Phase> phases =  new ArrayList<>();

}
