package ma.fst.projet2societe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableauDeBordDTO {

    private long totalProjects;
    private long projectsEnCours;
    private long projectsClotures;
    private long projectsAVenir;

    private long totalPhases;
    private long phasesTerminees;
    private long phasesNonTerminees;
    private long phasesTermineesNonFacturees;
    private long phasesFactureesNonPayees;
    private long phasesPayees;

    private Double montantTotalProjects;
    private Double montantTotalPhases;
    private Double montantFacture;
    private Double montantPaye;
    private Double montantRestantAPayer;


    private long totalOrganismes;
    private long totalEmployes;
    private long totalDocuments;
    private long totalLivrables;
}