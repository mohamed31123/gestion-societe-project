package ma.fst.projet2societe.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.OrganismeRequest;
import ma.fst.projet2societe.dto.OrganismeResponse;
import ma.fst.projet2societe.services.OrganismeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@RequiredArgsConstructor
@Tag(name = "Organismes", description = "Gestion des organismes clients")
public class OrganismeController {


    private final OrganismeService organismeService;

    @PostMapping
    @Operation(summary = "creer un organisme")
    public OrganismeResponse create(@Valid @RequestBody OrganismeRequest request) {
        return organismeService.create(request);
    }

    @GetMapping
    @Operation(summary = "trouver toutes les organismes")

    public List<OrganismeResponse> getAll() {
        return organismeService.findAll();
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "trouver un organisme par son nom")




    public OrganismeResponse findByNom(@PathVariable String nom) {
        return organismeService.findByNom(nom);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "trouver un organisme par son code")

    public OrganismeResponse findByCode(@PathVariable String code) {
        return organismeService.findByCode(code);
    }

    @GetMapping("/contact/{contact}")
    @Operation(summary = "trouver un contact")
    public OrganismeResponse findByContact(@PathVariable String contact) {
        return organismeService.findByNomContact(contact);
    }
    @DeleteMapping
    @Operation(summary = "supprimer une organisme")
    public void deleteById(@RequestParam Long id) {
         organismeService.delete(id);
    }
    //adding postMapping to update organisme
    @PutMapping("update/{id}")
    @Operation(summary = "creer des organismes")
    public OrganismeResponse update(@PathVariable Long id, @Valid @RequestBody OrganismeRequest request) {
        return organismeService.update(id, request);

    }
}