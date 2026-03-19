package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.fst.projet2societe.dto.PhaseReportDTO;
import ma.fst.projet2societe.dto.ProjectReportDTO;
import ma.fst.projet2societe.dto.TableauDeBordDTO;
import ma.fst.projet2societe.services.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/reporting")
@RequiredArgsConstructor
@Tag(name = "Reporting", description = "Tableau de bord et reporting métier")
public class ReportingController {

    private final ReportingService reportingService;

    //  GET /api/reporting/phases/terminees-non-facturees
    @GetMapping("/phases/terminees-non-facturees")
    @Operation(summary = "Phases terminées mais pas encore facturées")
    public ResponseEntity<Page<PhaseReportDTO>> phasesTermineesNonFacturees(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long chefProjectId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin,
            Pageable pageable) {

        return ResponseEntity.ok(
                reportingService.phasesTermineesNonFacturees(
                        projectId, chefProjectId, dateDebut, dateFin, pageable)
        );
    }


    //  GET /api/reporting/phases/facturees-non-payees
    @GetMapping("/phases/facturees-non-payees")
    @Operation(summary = "Phases facturées mais pas encore payées")
    public ResponseEntity<Page<PhaseReportDTO>> phasesFactureesNonPayees(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long chefProjectId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin,
            Pageable pageable) {

        return ResponseEntity.ok(
                reportingService.phasesFactureesNonPayees(
                        projectId, chefProjectId, dateDebut, dateFin, pageable)
        );
    }

    //  GET /api/reporting/phases/payees
    @GetMapping("/phases/payees")
    @Operation(summary = "Phases entièrement payées")
    public ResponseEntity<Page<PhaseReportDTO>> phasesPayees(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long chefProjectId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin,
            Pageable pageable) {

        return ResponseEntity.ok(
                reportingService.phasesPayees(
                        projectId, chefProjectId, dateDebut, dateFin, pageable)
        );
    }

    //  GET /api/reporting/projets/en-cours
    @GetMapping("/projets/en-cours")
    @Operation(summary = "Projects en cours (dateDebut <= aujourd'hui <= dateFin)")
    public ResponseEntity<Page<ProjectReportDTO>> projectsEnCours(
            @RequestParam(required = false) Long chefProjectId,
            @RequestParam(required = false) Long organismeId,
            Pageable pageable) {

        return ResponseEntity.ok(
                reportingService.projectsEnCours(chefProjectId, organismeId, pageable)
        );
    }

    //  GET /api/reporting/projets/clotures
    @GetMapping("/projets/clotures")
    @Operation(summary = "Projects clôturés (dateFin < aujourd'hui)")
    public ResponseEntity<Page<ProjectReportDTO>> projectsClotures(
            @RequestParam(required = false) Long chefProjectId,
            @RequestParam(required = false) Long organismeId,
            Pageable pageable) {

        return ResponseEntity.ok(
                reportingService.projectsClotures(chefProjectId, organismeId, pageable)
        );
    }

    //  GET /api/reporting/tableau-de-bord
    @GetMapping("/tableau-de-bord")
    @Operation(summary = "Tableau de bord global avec toutes les statistiques")
    public ResponseEntity<TableauDeBordDTO> tableauDeBord() {
        return ResponseEntity.ok(reportingService.tableauDeBord());
    }
}