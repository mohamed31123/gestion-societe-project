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
@ToString(exclude = {"organisme", "employe", "phases"})
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String code;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private Double montant;

    @ManyToOne
    @JoinColumn(name = "idOrganisme")
    private Organisme organisme;

    @ManyToOne
    @JoinColumn(name = "idEmploye")
    private Employe employe;

    // FIX: added cascade and orphanRemoval, also fixed @ToString to exclude this
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phase> phases = new ArrayList<>();

    // FIX: removed isPresent() method that always returned false
}
