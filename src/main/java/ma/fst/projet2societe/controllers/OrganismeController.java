package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.OrganismeRequest;
import ma.fst.projet2societe.dto.OrganismeResponse;
import ma.fst.projet2societe.services.OrganismeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@RequiredArgsConstructor
@Tag(name = "Organismes", description = "Gestion des organismes clients")
public class OrganismeController {

    private final OrganismeService organismeService;

    // POST /api/organismes
    @PostMapping
    @Operation(summary = "Créer un organisme")
    public ResponseEntity<OrganismeResponse> create(@Valid @RequestBody OrganismeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organismeService.create(request));
    }

    // GET /api/organismes
    @GetMapping
    @Operation(summary = "Lister tous les organismes")
    public List<OrganismeResponse> getAll() {
        return organismeService.findAll();
    }

    // GET /api/organismes/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Trouver un organisme par ID")
    public ResponseEntity<OrganismeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(organismeService.findById(id));
    }

    // PUT /api/organismes/{id}   — FIX: was PUT /api/organismes/update/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un organisme")
    public ResponseEntity<OrganismeResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody OrganismeRequest request) {
        return ResponseEntity.ok(organismeService.update(id, request));
    }

    // DELETE /api/organismes/{id}   — FIX: was DELETE /api/organismes?id=X
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un organisme")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organismeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/organismes/search?nom=X&code=Y
    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom ou code")
    public ResponseEntity<OrganismeResponse> search(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String contact) {
        if (nom != null)     return ResponseEntity.ok(organismeService.findByNom(nom));
        if (code != null)    return ResponseEntity.ok(organismeService.findByCode(code));
        if (contact != null) return ResponseEntity.ok(organismeService.findByNomContact(contact));
        return ResponseEntity.badRequest().build();
    }
}