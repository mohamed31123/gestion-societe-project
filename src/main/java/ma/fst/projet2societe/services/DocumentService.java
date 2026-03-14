package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.DocumentRequest;
import ma.fst.projet2societe.dto.DocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface DocumentService {

    DocumentResponse create(Long projetId, DocumentRequest request, MultipartFile fichier);
    DocumentResponse update(Long id, DocumentRequest request, MultipartFile fichier);
    DocumentResponse findById(Long id);
    List<DocumentResponse> findByProjet(Long projetId);
    void delete(Long id);
    Resource download(Long id);
}