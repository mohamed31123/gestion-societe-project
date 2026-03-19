package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.EmployeCreateRequest;
import ma.fst.projet2societe.dto.EmployeDTO;
import ma.fst.projet2societe.services.EmployeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Gestion des employés", description = "APIs pour gérer les employés")
@SecurityRequirement(name = "bearerAuth")
public class EmployeController {

    private final EmployeService employeService;

    @Operation(summary = "Liste tous les employés")
    @GetMapping
    public List<EmployeDTO> getAll() {
        return employeService.getAll();
    }

    @Operation(summary = "Trouver un employé par ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.getById(id));
    }

    @Operation(summary = "Créer un nouvel employé")
    @PostMapping
    public ResponseEntity<EmployeDTO> create(@Valid @RequestBody EmployeCreateRequest request) {
        EmployeDTO dto = new EmployeDTO();
        dto.setMatricule(request.getMatricule());
        dto.setNom(request.getNom());
        dto.setPrenom(request.getPrenom());
        dto.setAdresse(request.getAdresse());
        dto.setTelephone(request.getTelephone());
        dto.setEmail(request.getEmail());
        dto.setLogin(request.getLogin());
        dto.setProfilId(request.getProfilId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeService.create(dto, request.getPassword()));
    }

    @Operation(summary = "Modifier un employé existant")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> update(@PathVariable Long id,
                                             @Valid @RequestBody EmployeDTO dto) {
        return ResponseEntity.ok(employeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un employé par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Employés disponibles sur une période (YYYY-MM-DD)")
    @GetMapping("/disponibles")
    public List<EmployeDTO> getDisponibles(@RequestParam String dateDebut,
                                           @RequestParam String dateFin) {
        return employeService.getDisponibles(dateDebut, dateFin);
    }

    @Operation(summary = "Recherche multi-critères (nom ou matricule)")
    @GetMapping("/search")
    public List<EmployeDTO> search(@RequestParam(required = false) String nom,
                                   @RequestParam(required = false) String matricule) {
        if (matricule != null) {
            return employeService.getAll().stream()
                    .filter(e -> matricule.equalsIgnoreCase(e.getMatricule())).toList();
        }
        if (nom != null) {
            return employeService.getAll().stream()
                    .filter(e -> e.getNom().toLowerCase().contains(nom.toLowerCase())).toList();
        }
        return employeService.getAll();
    }
}
