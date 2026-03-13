package ma.fst.projet2societe.repositories;


import ma.fst.projet2societe.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    public Employe findByMatricule(String matricule);
    public Employe findByLogin(String code);


}
