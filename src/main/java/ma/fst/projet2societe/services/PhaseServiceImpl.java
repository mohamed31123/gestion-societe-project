package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.PhaseRequest;
import ma.fst.projet2societe.dto.PhaseResponse;
import ma.fst.projet2societe.entities.Phase;
import ma.fst.projet2societe.entities.Project;
import ma.fst.projet2societe.repositories.PhaseRepository;
import ma.fst.projet2societe.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final ProjectRepository projetRepository;

    @Override
    public PhaseResponse create(Long projetId, PhaseRequest request) {

        // 1. Vérifier que le projet existe
        Project projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException(
                        "Projet introuvable avec l'id : " + projetId));

        // 2. Contrôle des dates
        validerDates(request, projet);

        // 3. Contrôle du montant (null = pas d'exclusion, c'est un create)
        validerMontant(projetId, request.getMontant(), null);

        // 4. Construction et sauvegarde
        Phase phase = new Phase();
        phase.setCode(request.getCode());
        phase.setLibelle(request.getLibelle());
        phase.setDescription(request.getDescription());
        phase.setDateDebut(request.getDateDebut());
        phase.setDateFin(request.getDateFin());
        phase.setMontant(request.getMontant());
        phase.setEtatRealisation(false);
        phase.setEtatFacturation(false);
        phase.setEtatPaiement(false);
        phase.setProject(projet);

        return mapToResponse(phaseRepository.save(phase));
    }

    @Override
    public PhaseResponse update(Long id, PhaseRequest request) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));

        Project projet = phase.getProject();

        // Contrôle des dates
        validerDates(request, projet);

        // Contrôle du montant (on exclut la phase courante du calcul)
        validerMontant(projet.getId(), request.getMontant(), id);

        phase.setCode(request.getCode());
        phase.setLibelle(request.getLibelle());
        phase.setDescription(request.getDescription());
        phase.setDateDebut(request.getDateDebut());
        phase.setDateFin(request.getDateFin());
        phase.setMontant(request.getMontant());



        return mapToResponse(phaseRepository.save(phase));
    }


    @Override
    public PhaseResponse findById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));
        return mapToResponse(phase);
    }

    @Override
    public List<PhaseResponse> findByProjet(Long projetId) {
        if (!projetRepository.existsById(projetId)) {
            throw new RuntimeException("Projet introuvable avec l'id : " + projetId);
        }
        return phaseRepository.findByProjectId(projetId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));
        phaseRepository.delete(phase);
    }


    @Override
    public PhaseResponse setRealisation(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));

        if (phase.isEtatRealisation()) {
            throw new RuntimeException("La phase est déjà marquée comme réalisée");
        }

        phase.setEtatRealisation(true);
        return mapToResponse(phaseRepository.save(phase));
    }

    @Override
    public PhaseResponse setFacturation(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));

        if (!phase.isEtatRealisation()) {
            throw new RuntimeException(
                    "Impossible de facturer : la phase doit d'abord être réalisée");
        }
        if (phase.isEtatFacturation()) {
            throw new RuntimeException("La phase est déjà facturée");
        }

        phase.setEtatFacturation(true);
        return mapToResponse(phaseRepository.save(phase));
    }

    @Override
    public PhaseResponse setPaiement(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Phase introuvable avec l'id : " + id));

        if (!phase.isEtatFacturation()) {
            throw new RuntimeException(
                    "Impossible de payer : la phase doit d'abord être facturée");
        }
        if (phase.isEtatPaiement()) {
            throw new RuntimeException("La phase est déjà payée");
        }

        phase.setEtatPaiement(true);
        return mapToResponse(phaseRepository.save(phase));
    }




    private void validerDates(PhaseRequest request, Project projet) {

        if (request.getDateDebut().after(request.getDateFin())) {
            throw new RuntimeException(
                    "La date de début de la phase doit être avant sa date de fin");
        }

        if (request.getDateDebut().before(projet.getDateDebut())) {
            throw new RuntimeException(
                    "La date de début de la phase (" + request.getDateDebut() + ")" +
                            " est antérieure à la date de début du projet (" + projet.getDateDebut() + ")");
        }

        if (request.getDateFin().after(projet.getDateFin())) {
            throw new RuntimeException(
                    "La date de fin de la phase (" + request.getDateFin() + ")" +
                            " dépasse la date de fin du projet (" + projet.getDateFin() + ")");
        }
    }

    /**
     * Règle 2 : somme des montants des phases <= montant du projet.
     * excludePhaseId : phase à exclure du calcul lors d'un update (null pour un create).
     */
    private void validerMontant(Long projetId, Double nouveauMontant, Long excludePhaseId) {

        Project projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        double sommeExistante = phaseRepository.findByProjectId(projetId)
                .stream()
                .filter(p -> excludePhaseId == null || !p.getId().equals(excludePhaseId))
                .mapToDouble(Phase::getMontant)
                .sum();

        double totalApres = sommeExistante + nouveauMontant;

        if (totalApres > projet.getMontant()) {
            double restant = projet.getMontant() - sommeExistante;
            throw new RuntimeException(
                    "Le montant de la phase (" + nouveauMontant + ") dépasse le budget restant du projet." +
                            " | Budget total : " + projet.getMontant() +
                            " | Déjà alloué : " + sommeExistante +
                            " | Restant disponible : " + restant);
        }
    }

    private PhaseResponse mapToResponse(Phase phase) {
        PhaseResponse response = new PhaseResponse();
        response.setId(phase.getId());
        response.setCode(phase.getCode());
        response.setLibelle(phase.getLibelle());
        response.setDescription(phase.getDescription());
        response.setDateDebut(phase.getDateDebut());
        response.setDateFin(phase.getDateFin());
        response.setMontant(phase.getMontant());
        response.setEtatRealisation(phase.isEtatRealisation());
        response.setEtatFacturation(phase.isEtatFacturation());
        response.setEtatPaiement(phase.isEtatPaiement());

        if (phase.getProject() != null) {
            response.setProjetId(phase.getProject().getId());
            response.setProjetCode(phase.getProject().getCode());

        }

        return response;
    }
}