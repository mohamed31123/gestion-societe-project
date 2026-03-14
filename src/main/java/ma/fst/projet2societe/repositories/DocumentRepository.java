package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Document;
import ma.fst.projet2societe.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByProjetId(Long idProject);

}