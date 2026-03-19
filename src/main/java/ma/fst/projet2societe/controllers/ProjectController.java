package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.fst.projet2societe.dto.ProjectRequest;
import ma.fst.projet2societe.dto.ProjectResponse;
import ma.fst.projet2societe.dto.ProjectResume;
import ma.fst.projet2societe.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projets", description = "Gestion des projets")
public class ProjectController {

    private final ProjectService projectService;

    // POST /api/projects
    @PostMapping
    @Operation(summary = "créer un project")
    public ResponseEntity<ProjectResponse> create(
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectService.create(request));
    }

    // PUT /api/projects/{id}
    @PutMapping("/{id}")
    @Operation(summary = "modifier un project par son id")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    // GET /api/projects/{id}
    @GetMapping("/{id}")
    @Operation(summary = "trouver un project par son id")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    // GET /api/projects
    @GetMapping
    @Operation(summary = "trouver tous les projets")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "supprimer un project par son id")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/projects/{id}/resume
    @GetMapping("/{id}/resume")
    @Operation(summary = "trouver le resume d'un project")
    public ResponseEntity<ProjectResume> getResume(
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.getResume(id));
    }
}