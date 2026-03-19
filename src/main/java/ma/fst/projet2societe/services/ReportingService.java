package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.PhaseReportDTO;
import ma.fst.projet2societe.dto.ProjectReportDTO;
import ma.fst.projet2societe.dto.TableauDeBordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ReportingService {

    Page<PhaseReportDTO> phasesTermineesNonFacturees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin,
            Pageable pageable);

    Page<PhaseReportDTO> phasesFactureesNonPayees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin,
            Pageable pageable);

    Page<PhaseReportDTO> phasesPayees(
            Long projectId, Long chefProjectId,
            Date dateDebut, Date dateFin,
            Pageable pageable);

    Page<ProjectReportDTO> projectsEnCours(
            Long chefProjectId, Long organismeId,
            Pageable pageable);

    Page<ProjectReportDTO> projectsClotures(
            Long chefProjectId, Long organismeId,
            Pageable pageable);

    TableauDeBordDTO tableauDeBord();
}