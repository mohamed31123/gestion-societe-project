package ma.fst.projet2societe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "profil")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String libelle;
    @JsonIgnore
    @OneToMany(mappedBy = "profil")
    private List<Employe> employees;
}