package ma.fst.projet2societe.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"project", "livrables", "factures", "affectations"})
@NoArgsConstructor
@AllArgsConstructor
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private double montant;

    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projet" , nullable = false)
    private Project project;

    @OneToMany(mappedBy = "phase")
    private List<Livrable> livrables =  new ArrayList<>();

    @OneToMany(mappedBy = "phase")
    private List<Facture> factures = new ArrayList<>();


    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Affectation> affectations = new ArrayList<>();

}

