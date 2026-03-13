package ma.fst.projet2societe.controllers;



import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.OrganismeRequest;
import ma.fst.projet2societe.dto.OrganismeResponse;
import ma.fst.projet2societe.service.OrganismeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@RequiredArgsConstructor
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
}