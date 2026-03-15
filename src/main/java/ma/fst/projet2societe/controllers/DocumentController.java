package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.DocumentRequest;
import ma.fst.projet2societe.dto.DocumentResponse;
import ma.fst.projet2societe.services.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Gestion des documents d'un projet")
public class DocumentController {

    private final DocumentService documentService;

    //  POST /api/projects/{projectId}/documents

    @PostMapping(value = "/api/projects/{projetcId}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> create(
            @PathVariable Long projectId,
            @RequestParam("code") String code,
            @RequestParam("libelle") String libelle,
            @RequestParam(value = "descteption", required = false) String descteption,
            @RequestPart(value = "fichier", required = false) MultipartFile fichier) {

        DocumentRequest request = new DocumentRequest();
        request.setCode(code);
        request.setLibelle(libelle);
        request.setDescteption(descteption);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.create(projectId, request, fichier));
    }

    // GET /api/projets/{projetId}/documents
    @GetMapping("/api/projects/{projectId}/documents")
    @Operation(summary = "Lister tous les documents d'un project")
    public ResponseEntity<List<DocumentResponse>> findByProject(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(documentService.findByProject(projectId));
    }

    //  GET /api/documents/{id}
    @GetMapping("/api/documents/{id}")
    @Operation(summary = "Récupérer un document par son id")
    public ResponseEntity<DocumentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    // ── PUT /api/documents/{id}
    @PutMapping(value = "/api/documents/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modifier un document")
    public ResponseEntity<DocumentResponse> update(
            @PathVariable Long id,
            @Valid @RequestParam("document") DocumentRequest request,
            @RequestPart(value = "fichier", required = false) MultipartFile fichier) {

        return ResponseEntity.ok(documentService.update(id, request, fichier));
    }

    //  DELETE /api/documents/{id}
    @DeleteMapping("/api/documents/{id}")
    @Operation(summary = "Supprimer un document et son fichier")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  GET /api/documents/{id}/download
    @GetMapping("/api/documents/{id}/download")
    @Operation(summary = "Télécharger le fichier d'un document")
    public ResponseEntity<Resource> download(@PathVariable Long id) {

        DocumentResponse doc = documentService.findById(id);
        Resource resource = documentService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getNomFichier() + "\"")
                .body(resource);
    }
}