package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.fst.projet2societe.dto.FactureDTO;
import ma.fst.projet2societe.services.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@Tag(name = "Gestion des factures", description = "APIs pour gérer les factures")
public class FactureController {

    @Autowired
    private FactureService factureService;

    // POST /api/phases/{phaseId}/facture
    @Operation(summary = "Créer une facture pour une phase terminée")
    @PostMapping("/api/phases/{phaseId}/facture")
    public ResponseEntity<FactureDTO> create(
            @PathVariable Long phaseId,
            @Valid @RequestBody FactureDTO dto) {
        return ResponseEntity.ok(factureService.create(phaseId, dto));
    }

    // GET /api/factures
    @Operation(summary = "Lister toutes les factures")
    @GetMapping("/api/factures")
    public List<FactureDTO> getAll() {
        return factureService.getAll();
    }

    // GET /api/factures/{id}
    @Operation(summary = "Consulter une facture par ID")
    @GetMapping("/api/factures/{id}")
    public ResponseEntity<FactureDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.getById(id));
    }

    // PUT /api/factures/{id}
    @Operation(summary = "Modifier une facture")
    @PutMapping("/api/factures/{id}")
    public ResponseEntity<FactureDTO> update(
            @PathVariable Long id,
             @Valid @RequestBody FactureDTO dto) {
        return ResponseEntity.ok(factureService.update(id, dto));
    }

    // DELETE /api/factures/{id}
    @Operation(summary = "Supprimer une facture")
    @DeleteMapping("/api/factures/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Phases terminées non facturées")
    @GetMapping("/api/factures/terminees-non-facturees")
    public List<FactureDTO> getPhasesTermineesNonFacturees() {
        return factureService.getPhasesTermineesNonFacturees();
    }

    @Operation(summary = "Phases facturées non payées")
    @GetMapping("/api/factures/facturees-non-payees")
    public List<FactureDTO> getPhasesFactureesNonPayees() {
        return factureService.getPhasesFactureesNonPayees();
    }

    @Operation(summary = "Phases payées")
    @GetMapping("/api/factures/payees")
    public List<FactureDTO> getPhasesPayees() {
        return factureService.getPhasesPayees();
    }
}