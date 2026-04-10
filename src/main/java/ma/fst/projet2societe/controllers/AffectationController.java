package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.AffectationDTO;
import ma.fst.projet2societe.entities.Affectation;
import ma.fst.projet2societe.services.AffectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Gestion des affectations", description = "APIs pour gérer les affectations employé-phase")
public class AffectationController {


    private final AffectationService affectationService;


    @Operation(summary = "Affecter un employé à une phase")
    @PostMapping("/api/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Affectation> affecter(
            @PathVariable Long phaseId,
            @PathVariable Long employeId,
            @Valid @RequestBody AffectationDTO dto) {
        dto.setPhaseId(phaseId);
        dto.setEmployeId(employeId);
        return ResponseEntity.ok(affectationService.affecter(dto));
    }


    @Operation(summary = "Lister les employés d'une phase")
    @GetMapping("/api/phases/{phaseId}/employes")
    public List<Affectation> getByPhase(@PathVariable Long phaseId) {
        return affectationService.getByPhase(phaseId);
    }

    @Operation(summary = "Lister les phases d'un employé")
    @GetMapping("/api/employes/{employeId}/phases")
    public List<Affectation> getByEmploye(@PathVariable Long employeId) {
        return affectationService.getByEmploye(employeId);
    }

    @Operation(summary = "Modifier une affectation")
    @PutMapping("/api/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Affectation> modifier(
            @PathVariable Long phaseId,
            @PathVariable Long employeId,
           @Valid @RequestBody AffectationDTO dto) {
        return ResponseEntity.ok(affectationService.modifier(employeId, phaseId, dto));
    }

    @Operation(summary = "Supprimer une affectation")
    @DeleteMapping("/api/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long phaseId,
            @PathVariable Long employeId) {
        affectationService.supprimer(employeId, phaseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer une affectation spécifique")
    @GetMapping("/api/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Affectation> getByPhaseAndEmploye(
            @PathVariable Long phaseId,
            @PathVariable Long employeId) {
        return ResponseEntity.ok(affectationService.getByPhaseAndEmploye(phaseId, employeId));
    }
}