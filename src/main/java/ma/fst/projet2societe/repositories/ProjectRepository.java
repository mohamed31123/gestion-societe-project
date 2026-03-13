package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
      public Project findByCode(String code);

}
