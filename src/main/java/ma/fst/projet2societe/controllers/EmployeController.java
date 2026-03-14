package ma.fst.projet2societe.controllers;

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
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    // GET /api/employes
    @GetMapping
    public List<Employe> getAll() {
        return employeService.getAll();
    }

    // GET /api/employes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.getById(id));
    }

    // POST /api/employes
    @PostMapping
    public ResponseEntity<Employe> create(@RequestBody Employe employe) {
        return ResponseEntity.ok(employeService.create(employe));
    }

    // PUT /api/employes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Employe> update(@PathVariable Long id,
                                          @RequestBody Employe employe) {
        return ResponseEntity.ok(employeService.update(id, employe));
    }

    // DELETE /api/employes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/employes/disponibles
    @GetMapping("/disponibles")
    public List<Employe> getDisponibles(@RequestParam String dateDebut,
                                        @RequestParam String dateFin) {
        return employeService.getDisponibles(dateDebut, dateFin);
    }

    // GET /api/employes/search
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

