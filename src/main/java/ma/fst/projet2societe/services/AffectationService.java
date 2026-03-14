package ma.fst.projet2societe.services;

import ma.fst.projet2societe.entities.*;
import ma.fst.projet2societe.dto.AffectationDTO;
import ma.fst.projet2societe.repositories.AffectationRepository;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AffectationService {

    @Autowired
    private AffectationRepository affectationRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    // Affecter un employé à une phase
    public Affectation affecter(AffectationDTO dto) {
        // Vérifier que l'employé existe
        Employe employe = employeRepository.findById(dto.getEmployeId())
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        // Vérifier que la phase existe
        Phase phase = phaseRepository.findById(dto.getPhaseId())
                .orElseThrow(() -> new RuntimeException("Phase non trouvée"));

        // Vérifier pas de doublon
        AffectationId affectationId = new AffectationId(dto.getEmployeId(), dto.getPhaseId());
        if (affectationRepository.existsById(affectationId)) {
            throw new RuntimeException("Affectation déjà existante");
        }

        // creation de l'affictation
        Affectation affectation = new Affectation();
        affectation.setId(affectationId);
        affectation.setEmploye(employe);
        affectation.setPhase(phase);
        affectation.setDatedebut(dto.getDatedebut());
        affectation.setDatefin(dto.getDatefin());

        return affectationRepository.save(affectation);
    }

    // liste employe phase
    public List<Affectation> getByPhase(Long phaseId) {
        return affectationRepository.findByPhaseId(phaseId);
    }

    // liste phases employe
    public List<Affectation> getByEmploye(Long employeId) {
        return affectationRepository.findByEmployeId(employeId);
    }

    // supprimer l'affectation
    public void supprimer(Long employeId, Long phaseId) {
        AffectationId affectationId = new AffectationId(employeId, phaseId);
        if (!affectationRepository.existsById(affectationId)) {
            throw new RuntimeException("Affectation non trouvée");
        }
        affectationRepository.deleteById(affectationId);
    }

    // modifier l'affictation
    public Affectation modifier(Long employeId, Long phaseId, AffectationDTO dto) {
        AffectationId affectationId = new AffectationId(employeId, phaseId);
        Affectation existing = affectationRepository.findById(affectationId)
                .orElseThrow(() -> new RuntimeException("Affectation non trouvée"));
        existing.setDatedebut(dto.getDatedebut());
        existing.setDatefin(dto.getDatefin());
        return affectationRepository.save(existing);
    }
    public Affectation getByPhaseAndEmploye(Long phaseId, Long employeId) {
        return affectationRepository.findByPhaseIdAndEmployeId(phaseId, employeId)
                .orElseThrow(() -> new RuntimeException("Affectation non trouvée"));
    }
}