package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.DocumentRequest;
import ma.fst.projet2societe.dto.DocumentResponse;
import ma.fst.projet2societe.entities.Document;
import ma.fst.projet2societe.entities.Project;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.DocumentRepository;
import ma.fst.projet2societe.repositories.ProjectRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    @Override
    public DocumentResponse create(Long projectId, DocumentRequest request, MultipartFile fichier) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Projet introuvable avec l'id : " + projectId));
        Document document = new Document();
        document.setCode(request.getCode());
        document.setLibelle(request.getLibelle());
        document.setDescription(request.getDescteption());
        document.setProject(project);
        return mapToResponse(documentRepository.save(document));
    }

    @Override
    public DocumentResponse update(Long id, DocumentRequest request, MultipartFile fichier) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document introuvable avec l'id : " + id));
        document.setCode(request.getCode());
        document.setLibelle(request.getLibelle());
        document.setDescription(request.getDescteption());
        return mapToResponse(documentRepository.save(document));
    }

    @Override
    public DocumentResponse findById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document introuvable avec l'id : " + id));
        return mapToResponse(document);
    }

    @Override
    public List<DocumentResponse> findByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projet introuvable avec l'id : " + projectId);
        }
        return documentRepository.findByProjectId(projectId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document introuvable avec l'id : " + id));
        documentRepository.delete(document);
    }

    @Override
    public Resource download(Long id) {
        throw new BusinessException("Téléchargement non disponible : aucun fichier stocké");
    }

    private DocumentResponse mapToResponse(Document doc) {
        DocumentResponse response = new DocumentResponse();
        response.setId(doc.getId());
        response.setCode(doc.getCode());
        response.setLibelle(doc.getLibelle());
        response.setDescteption(doc.getDescription());
        if (doc.getProject() != null) {
            response.setProjectId(doc.getProject().getId());
            response.setProjectNom(doc.getProject().getNom());
        }
        return response;
    }
}