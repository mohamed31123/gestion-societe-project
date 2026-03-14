package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.PhaseRequest;
import ma.fst.projet2societe.dto.PhaseResponse;
import ma.fst.projet2societe.service.PhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Phases", description = "Gestion des phases d'un projet")
public class PhaseController {

    private final PhaseService phaseService;

    // POST /api/projets/{projetId}/phases
    @PostMapping("/api/projets/{projetId}/phases")
    @Operation(summary = "Créer une phase dans un projet")
    public ResponseEntity<PhaseResponse> create(
            @PathVariable Long projetId,
            @Valid @RequestBody PhaseRequest request) {

        PhaseResponse response = phaseService.create(projetId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //  GET /api/projets/{projetId}/phases
    @GetMapping("/api/projets/{projetId}/phases")
    @Operation(summary = "Lister toutes les phases d'un projet")
    public ResponseEntity<List<PhaseResponse>> findByProjet(
            @PathVariable Long projetId) {

        return ResponseEntity.ok(phaseService.findByProjet(projetId));
    }

    //  GET /api/phases/{id}
    @GetMapping("/api/phases/{id}")
    @Operation(summary = "Récupérer une phase par son id")
    public ResponseEntity<PhaseResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(phaseService.findById(id));
    }

    //  PUT /api/phases/{id}
    @PutMapping("/api/phases/{id}")
    @Operation(summary = "Modifier une phase (dates et montant revalidés)")
    public ResponseEntity<PhaseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PhaseRequest request) {

        return ResponseEntity.ok(phaseService.update(id, request));
    }

    //  DELETE /api/phases/{id}
    @DeleteMapping("/api/phases/{id}")
    @Operation(summary = "Supprimer une phase")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        phaseService.delete(id);
        return ResponseEntity.noContent().build();   // 204
    }

    //  PATCH /api/phases/{id}/realisation
    @PatchMapping("/api/phases/{id}/realisation")
    @Operation(summary = "Marquer la phase comme réalisée (étape 1/3)")
    public ResponseEntity<PhaseResponse> setRealisation(
            @PathVariable Long id) {

        return ResponseEntity.ok(phaseService.setRealisation(id));
    }

    //  PATCH /api/phases/{id}/facturation
    @PatchMapping("/api/phases/{id}/facturation")
    @Operation(summary = "Marquer la phase comme facturée (étape 2/3 — nécessite réalisation)")
    public ResponseEntity<PhaseResponse> setFacturation(
            @PathVariable Long id) {

        return ResponseEntity.ok(phaseService.setFacturation(id));
    }

    // PATCH /api/phases/{id}/paiement
    @PatchMapping("/api/phases/{id}/paiement")
    @Operation(summary = "Marquer la phase comme payée (étape 3/3 — nécessite facturation)")
    public ResponseEntity<PhaseResponse> setPaiement(
            @PathVariable Long id) {

        return ResponseEntity.ok(phaseService.setPaiement(id));
    }
}