package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.services.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin("*")
@Tag(name = "Gestion des employés", description = "APIs pour gérer les employés")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @Operation(summary = "Liste tous les employés")
    @GetMapping
    public List<Employe> getAll() {
        return employeService.getAll();
    }

    @Operation(summary = "Trouver un employé par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.getById(id));
    }

    @Operation(summary = "Créer un nouvel employé")
    @PostMapping
    public ResponseEntity<Employe> create(@RequestBody Employe employe) {
        return ResponseEntity.ok(employeService.create(employe));
    }

    @Operation(summary = "Modifier un employé existant")
    @PutMapping("/{id}")
    public ResponseEntity<Employe> update(@PathVariable Long id,
                                          @RequestBody Employe employe) {
        return ResponseEntity.ok(employeService.update(id, employe));
    }

    @Operation(summary = "Supprimer un employé par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister les employés disponibles sur une période",
            description = "Format des dates : YYYY-MM-DD")
    @GetMapping("/disponibles")
    public List<Employe> getDisponibles(@RequestParam String dateDebut,
                                        @RequestParam String dateFin) {
        return employeService.getDisponibles(dateDebut, dateFin);
    }

    @Operation(summary = "Rechercher des employés par nom ou matricule")
    @GetMapping("/search")
    public List<Employe> search(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String matricule) {
        if (matricule != null) {
            return employeRepository.findByMatricule(matricule)
                    .map(List::of).orElse(List.of());
        }
        if (nom != null) {
            return employeRepository.findByNomContaining(nom);
        }
        return employeService.getAll();
    }
}