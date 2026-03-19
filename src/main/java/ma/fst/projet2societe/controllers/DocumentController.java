package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Gestion des documents d'un projet")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;

    // POST /api/projets/{projetId}/documents
    @Operation(summary = "Créer un document pour un projet")
    @PostMapping(value = "/api/projets/{projetId}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> create(
            @PathVariable Long projetId,
            @RequestParam("code")                          String code,
            @RequestParam("libelle")                       String libelle,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart(value = "fichier",      required = false) MultipartFile fichier) {

        DocumentRequest request = new DocumentRequest();
        request.setCode(code);
        request.setLibelle(libelle);
        request.setDescription(description);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.create(projetId, request, fichier));
    }

    // GET /api/projets/{projetId}/documents
    @GetMapping("/api/projets/{projetId}/documents")
    @Operation(summary = "Lister tous les documents d'un projet")
    public ResponseEntity<List<DocumentResponse>> findByProject(@PathVariable Long projetId) {
        return ResponseEntity.ok(documentService.findByProject(projetId));
    }

    // GET /api/documents/{id}
    @GetMapping("/api/documents/{id}")
    @Operation(summary = "Récupérer un document par ID")
    public ResponseEntity<DocumentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    // PUT /api/documents/{id}
    @PutMapping(value = "/api/documents/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modifier un document")
    public ResponseEntity<DocumentResponse> update(
            @PathVariable Long id,
            @RequestParam("code")                          String code,
            @RequestParam("libelle")                       String libelle,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart(value = "fichier",      required = false) MultipartFile fichier) {

        DocumentRequest request = new DocumentRequest();
        request.setCode(code);
        request.setLibelle(libelle);
        request.setDescription(description);

        return ResponseEntity.ok(documentService.update(id, request, fichier));
    }

    // DELETE /api/documents/{id}
    @DeleteMapping("/api/documents/{id}")
    @Operation(summary = "Supprimer un document")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/documents/{id}/download
    @GetMapping("/api/documents/{id}/download")
    @Operation(summary = "Télécharger le fichier d'un document")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        DocumentResponse doc      = documentService.findById(id);
        Resource         resource = documentService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getNomFichier() + "\"")
                .body(resource);
    }
}
