package ma.fst.projet2societe.repositories;

import ma.fst.projet2societe.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {


}
