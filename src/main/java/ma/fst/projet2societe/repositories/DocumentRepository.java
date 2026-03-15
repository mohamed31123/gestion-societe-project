package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByProjectId(Long projectId);

}