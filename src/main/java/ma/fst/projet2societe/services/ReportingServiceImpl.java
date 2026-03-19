package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.PhaseReportDTO;
import ma.fst.projet2societe.dto.ProjectReportDTO;
import ma.fst.projet2societe.dto.TableauDeBordDTO;
import ma.fst.projet2societe.entities.Phase;
import ma.fst.projet2societe.entities.Project;
import ma.fst.projet2societe.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final PhaseRepository     phaseRepository;
    private final ProjectRepository   projectRepository;
    private final OrganismeRepository organismeRepository;
    private final EmployeRepository   employeRepository;
    private final DocumentRepository  documentRepository;
    private final LivrableRepository  livrableRepository;

    // le mapping de Phase vers PhaseReportDTO
    private PhaseReportDTO toPhaseReport(Phase phase) {
        PhaseReportDTO dto = new PhaseReportDTO();
        dto.setId(phase.getId());
        dto.setCode(phase.getCode());
        dto.setLibelle(phase.getLibelle());
        dto.setDescription(phase.getDescription());
        dto.setDateDebut(phase.getDateDebut());
        dto.setDateFin(phase.getDateFin());
        dto.setMontant(phase.getMontant());
        dto.setEtatRealisation(phase.isEtatRealisation());
        dto.setEtatFacturation(phase.isEtatFacturation());
        dto.setEtatPaiement(phase.isEtatPaiement());

        if (phase.getProject() != null) {
            Project project = phase.getProject();
            dto.setProjectId(project.getId());
            dto.setProjectNom(project.getNom());
            dto.setProjectCode(project.getCode());

            if (project.getEmploye() != null) {
                dto.setChefProjectId(project.getEmploye().getId());
                dto.setChefProjectNom(project.getEmploye().getNom());
                dto.setChefProjectPrenom(project.getEmploye().getPrenom());
            }
            if (project.getOrganisme() != null) {
                dto.setOrganismeNom(project.getOrganisme().getNom());
            }
        }

        dto.setNombreLivrables(
                phase.getLivrables() != null ? phase.getLivrables().size() : 0
        );

        return dto;
    }

    // le mapping de Project vers ProjectReportDTO

    private ProjectReportDTO toProjectReport(Project project) {
        ProjectReportDTO dto = new ProjectReportDTO();
        dto.setId(project.getId());
        dto.setCode(project.getCode());
        dto.setNom(project.getNom());
        dto.setDescription(project.getDescription());
        dto.setDateDebut(project.getDateDebut());
        dto.setDateFin(project.getDateFin());
        dto.setMontant(project.getMontant());

        if (project.getEmploye() != null) {
            dto.setChefProjectId(project.getEmploye().getId());
            dto.setChefProjectNom(project.getEmploye().getNom());
            dto.setChefProjectPrenom(project.getEmploye().getPrenom());
        }

        if (project.getOrganisme() != null) {
            dto.setOrganismeId(project.getOrganisme().getId());
            dto.setOrganismeNom(project.getOrganisme().getNom());
        }

        if (project.getPhases() != null && !project.getPhases().isEmpty()) {
            int total     = project.getPhases().size();
            int terminees = (int) project.getPhases().stream()
                    .filter(Phase::isEtatRealisation).count();
            int facturees = (int) project.getPhases().stream()
                    .filter(Phase::isEtatFacturation).count();
            int payees    = (int) project.getPhases().stream()
                    .filter(Phase::isEtatPaiement).count();

            double montantPhases  = project.getPhases().stream()
                    .mapToDouble(Phase::getMontant).sum();

            double montantFacture = project.getPhases().stream()
                    .filter(Phase::isEtatFacturation)
                    .mapToDouble(Phase::getMontant).sum();

            double montantPaye    = project.getPhases().stream()
                    .filter(Phase::isEtatPaiement)
                    .mapToDouble(Phase::getMontant).sum();

            dto.setNombrePhases(total);
            dto.setPhasesTerminees(terminees);
            dto.setPhasesFacturees(facturees);
            dto.setPhasesPaees(payees);
            dto.setMontantTotalPhases(montantPhases);
            dto.setMontantFacture(montantFacture);
            dto.setMontantPaye(montantPaye);
            dto.setMontantRestant(montantPhases - montantPaye);

            dto.setTauxRealisation(total > 0 ? (terminees * 100.0 / total) : 0);
            dto.setTauxFacturation(total > 0 ? (facturees * 100.0 / total) : 0);
            dto.setTauxPaiement   (total > 0 ? (payees    * 100.0 / total) : 0);
        }

        return dto;
    }

    //  les phases terminees non facturees
    @Override
    public Page<PhaseReportDTO> phasesTermineesNonFacturees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin, Pageable pageable) {

        return phaseRepository
                .findTermineesNonFacturees(projectId, chefProjectId, dateDebut, dateFin, pageable)
                .map(this::toPhaseReport);
    }

    //  les phases facturees non payees
    @Override
    public Page<PhaseReportDTO> phasesFactureesNonPayees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin, Pageable pageable) {

        return phaseRepository
                .findFactureesNonPayees(projectId, chefProjectId, dateDebut, dateFin, pageable)
                .map(this::toPhaseReport);
    }

    //  les phases payees
    @Override
    public Page<PhaseReportDTO> phasesPayees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin, Pageable pageable) {

        return phaseRepository
                .findPayees(projectId, chefProjectId, dateDebut, dateFin, pageable)
                .map(this::toPhaseReport);
    }


    //  les projects en cours
    @Override
    public Page<ProjectReportDTO> projectsEnCours(
            Long chefProjectId, Long organismeId, Pageable pageable) {

        return projectRepository
                .findEnCours(new Date(), chefProjectId, organismeId, pageable)
                .map(this::toProjectReport);
    }

    //  les projects clôtures
    @Override
    public Page<ProjectReportDTO> projectsClotures(
            Long chefProjectId, Long organismeId, Pageable pageable) {

        return projectRepository
                .findClotures(new Date(), chefProjectId, organismeId, pageable)
                .map(this::toProjectReport);
    }


    //  le tableau de bord
    @Override
    public TableauDeBordDTO tableauDeBord() {
        Date today = new Date();

        TableauDeBordDTO dto = new TableauDeBordDTO();

        // les projects
        dto.setTotalProjects(projectRepository.count());
        dto.setProjectsEnCours(projectRepository.countEnCours(today));
        dto.setProjectsClotures(projectRepository.countClotures(today));
        dto.setProjectsAVenir(projectRepository.countAVenir(today));

        // les phases
        dto.setTotalPhases(phaseRepository.count());
        dto.setPhasesTerminees(phaseRepository.countByEtatRealisationTrue());
        dto.setPhasesNonTerminees(phaseRepository.countByEtatRealisationFalse());
        dto.setPhasesTermineesNonFacturees(
                phaseRepository.countByEtatRealisationTrueAndEtatFacturationFalse());
        dto.setPhasesFactureesNonPayees(
                phaseRepository.countByEtatFacturationTrueAndEtatPaiementFalse());
        dto.setPhasesPayees(phaseRepository.countByEtatPaiementTrue());

        // les montants
        dto.setMontantTotalProjects(projectRepository.sumMontantTotal());
        dto.setMontantTotalPhases(phaseRepository.sumMontantTotal());
        dto.setMontantFacture(phaseRepository.sumMontantFacture());
        dto.setMontantPaye(phaseRepository.sumMontantPaye());
        dto.setMontantRestantAPayer(
                (dto.getMontantFacture() != null ? dto.getMontantFacture() : 0)
                        - (dto.getMontantPaye()    != null ? dto.getMontantPaye()    : 0)
        );

        // les organismes
        dto.setTotalOrganismes(organismeRepository.count());
        dto.setTotalEmployes(employeRepository.count());
        dto.setTotalDocuments(documentRepository.count());
        dto.setTotalLivrables(livrableRepository.count());

        return dto;
    }
}