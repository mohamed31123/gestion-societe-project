package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.ProjectRequest;
import ma.fst.projet2societe.dto.ProjectResponse;
import ma.fst.projet2societe.dto.ProjectResume;
import ma.fst.projet2societe.entities.*;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.DuplicateResourceException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganismeRepository organismeRepository;
    private final EmployeRepository employeRepository;

    private ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setCode(project.getCode());
        response.setNom(project.getNom());
        response.setDescription(project.getDescription());
        response.setDateDebut(project.getDateDebut());
        response.setDateFin(project.getDateFin());
        response.setMontant(project.getMontant());

        if (project.getOrganisme() != null) {
            response.setOrganismeId(project.getOrganisme().getId());
            response.setOrganismeNom(project.getOrganisme().getNom());
        }

        if (project.getEmploye() != null) {
            response.setChefProjectId(project.getEmploye().getId());
            response.setChefProjectNom(project.getEmploye().getNom());
            response.setChefProjectPrenom(project.getEmploye().getPrenom());
        }

        response.setNombrePhases(
                project.getPhases() != null ? project.getPhases().size() : 0
        );

        return response;
    }

    private ProjectResume toResume(Project project) {
        ProjectResume dto = new ProjectResume();
        dto.setId(project.getId());
        dto.setCode(project.getCode());
        dto.setNom(project.getNom());
        dto.setDateDebut(project.getDateDebut());
        dto.setDateFin(project.getDateFin());
        dto.setMontant(project.getMontant());

        if (project.getPhases() != null) {
            dto.setNombrePhases(project.getPhases().size());
            dto.setPhasesTerminees((int) project.getPhases().stream()
                    .filter(Phase::isEtatRealisation).count());
            dto.setPhasesFacturees((int) project.getPhases().stream()
                    .filter(Phase::isEtatFacturation).count());
            dto.setPhasesPaees((int) project.getPhases().stream()
                    .filter(Phase::isEtatPaiement).count());
        }

        if (project.getOrganisme() != null)
            dto.setOrganismeNom(project.getOrganisme().getNom());

        if (project.getEmploye() != null) {
            dto.setChefProjectNom(project.getEmploye().getNom());
            dto.setChefProjectPrenom(project.getEmploye().getPrenom());
        }

        return dto;
    }

    @Override
    public ProjectResponse create(ProjectRequest request) {

        if (projectRepository.findByCode(request.getCode()).isPresent())
            throw new DuplicateResourceException("Code projet déjà utilisé : " + request.getCode());

        if (request.getDateDebut().after(request.getDateFin()))
            throw new BusinessException("La date de début doit être avant la date de fin");

        // FIX: use ResourceNotFoundException
        Organisme organisme = organismeRepository.findById(request.getOrganismeId())
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable : " + request.getOrganismeId()));

        Employe chefProjet = employeRepository.findById(request.getChefProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable : " + request.getChefProjectId()));

        Project project = new Project();
        project.setCode(request.getCode());
        project.setNom(request.getNom());
        project.setDescription(request.getDescription());
        project.setDateDebut(request.getDateDebut());
        project.setDateFin(request.getDateFin());
        project.setMontant(request.getMontant());
        project.setOrganisme(organisme);
        project.setEmploye(chefProjet);

        return toResponse(projectRepository.save(project));
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id));

        if (!project.getCode().equals(request.getCode()) &&
                projectRepository.findByCode(request.getCode()).isPresent())
            throw new DuplicateResourceException("Code projet déjà utilisé : " + request.getCode());

        if (request.getDateDebut().after(request.getDateFin()))
            throw new BusinessException("La date de début doit être avant la date de fin");

        Organisme organisme = organismeRepository.findById(request.getOrganismeId())
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable : " + request.getOrganismeId()));

        Employe chefProject = employeRepository.findById(request.getChefProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable : " + request.getChefProjectId()));

        project.setCode(request.getCode());
        project.setNom(request.getNom());
        project.setDescription(request.getDescription());
        project.setDateDebut(request.getDateDebut());
        project.setDateFin(request.getDateFin());
        project.setMontant(request.getMontant());
        project.setOrganisme(organisme);
        project.setEmploye(chefProject);

        return toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        return toResponse(
                projectRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResume getResume(Long id) {
        return toResume(
                projectRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id))
        );
    }

    @Override
    public void delete(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id));

        // FIX: use BusinessException
        if (project.getPhases() != null && !project.getPhases().isEmpty())
            throw new BusinessException("Impossible de supprimer un projet qui contient des phases");

        projectRepository.delete(project);
    }
}
