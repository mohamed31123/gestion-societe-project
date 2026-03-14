package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.DocumentRequest;
import ma.fst.projet2societe.dto.DocumentResponse;
import ma.fst.projet2societe.entities.Document;
import ma.fst.projet2societe.entities.Project;
import ma.fst.projet2societe.repositories.DocumentRepository;
import ma.fst.projet2societe.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public DocumentResponse create(Long projetId, DocumentRequest request, MultipartFile fichier) {

        Project projet = projectRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException(
                        "Projet introuvable avec l'id : " + projetId));

        Document document = new Document();
        document.setCode(request.getCode());
        document.setLibelle(request.getLibelle());
        document.setDescription(request.getDescteption());
        document.setProjet(projet);

        return mapToResponse(documentRepository.save(document));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public DocumentResponse update(Long id, DocumentRequest request, MultipartFile fichier) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Document introuvable avec l'id : " + id));

        document.setCode(request.getCode());
        document.setLibelle(request.getLibelle());
        document.setDescription(request.getDescteption());

        return mapToResponse(documentRepository.save(document));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public DocumentResponse findById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Document introuvable avec l'id : " + id));
        return mapToResponse(document);
    }

    @Override
    public List<DocumentResponse> findByProjet(Long projetId) {
        if (!projectRepository.existsById(projetId)) {
            throw new RuntimeException("Projet introuvable avec l'id : " + projetId);
        }
        return documentRepository.findByProjetId(projetId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Document introuvable avec l'id : " + id));
        documentRepository.delete(document);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DOWNLOAD
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public Resource download(Long id) {
        // Pas de fichier dans l'entité → non implémenté
        throw new RuntimeException("Téléchargement non disponible : aucun fichier stocké");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MAPPER
    // ─────────────────────────────────────────────────────────────────────────
    private DocumentResponse mapToResponse(Document doc) {
        DocumentResponse response = new DocumentResponse();
        response.setId(doc.getId());
        response.setCode(doc.getCode());
        response.setLibelle(doc.getLibelle());
        response.setDescteption(doc.getDescription());

        if (doc.getProjet() != null) {
            response.setProjetId(doc.getProjet().getId());
            response.setProjetNom(doc.getProjet().getNom());
        }

        return response;
    }
}