package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.fst.projet2societe.dto.LivrableRequest;
import ma.fst.projet2societe.dto.LivrableResponse;
import ma.fst.projet2societe.service.LivrableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Livrables", description = "Gestion des livrables d'une phase")
public class LivrableController {

    private final LivrableService livrableService;


    @PostMapping(value = "/api/phases/{phaseId}/livrables", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "création d'un livrable")
    public ResponseEntity<LivrableResponse> create(
            @PathVariable Long phaseId,
            @org.springdoc.core.annotations.ParameterObject
            @ModelAttribute LivrableRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        LivrableResponse response = livrableService.create(phaseId, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    @GetMapping("/api/phases/{phaseId}/livrables")
    @Operation(summary = "trouver la liste de tous les livrables")
    public ResponseEntity<List<LivrableResponse>> getByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.getByPhase(phaseId));
    }


    @GetMapping("/api/livrables/{id}")
    @Operation(summary = "trouver un livrable par son id")
    public ResponseEntity<LivrableResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(livrableService.getById(id));
    }


    @PutMapping(value = "/api/livrables/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "modification d'un livrable par son id")
    public ResponseEntity<LivrableResponse> update(
            @PathVariable Long id,
            @org.springdoc.core.annotations.ParameterObject
            @ModelAttribute LivrableRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        LivrableResponse response = livrableService.update(id, request, file);
        return ResponseEntity.ok(response);
    }




    @DeleteMapping("/api/livrables/{id}")
    @Operation(summary = "supression d'un livrable par son id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livrableService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/api/livrables/{id}/download")
    @Operation(summary = "telechargement du fichier")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        byte[] data = livrableService.download(id);
        String contentType = livrableService.getContentType(id);
        String nomFichier = livrableService.getNomFichier(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nomFichier + "\"")
                .body(data);
    }
}
