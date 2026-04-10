package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.entities.*;
import ma.fst.projet2societe.dto.AffectationDTO;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.DuplicateResourceException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.AffectationRepository;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.repositories.PhaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AffectationService {

    private final AffectationRepository affectationRepository;
    private final EmployeRepository employeRepository;
    private final PhaseRepository phaseRepository;

    public Affectation affecter(AffectationDTO dto) {

        Employe employe = employeRepository.findById(dto.getEmployeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + dto.getEmployeId()));

        Phase phase = phaseRepository.findById(dto.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase non trouvée : " + dto.getPhaseId()));

        AffectationId affectationId = new AffectationId(dto.getEmployeId(), dto.getPhaseId());
        if (affectationRepository.existsById(affectationId)) {
            throw new DuplicateResourceException("Affectation déjà existante");
        }

        if (dto.getDatedebut().after(dto.getDatefin())) {
            throw new BusinessException("Date début > date fin");
        }

        if (dto.getDatedebut().before(phase.getDateDebut()) ||
                dto.getDatefin().after(phase.getDateFin())) {
            throw new BusinessException("Dates hors phase");
        }

        Affectation affectation = new Affectation();
        affectation.setEmploye(employe);
        affectation.setPhase(phase);
        affectation.setDatedebut(dto.getDatedebut());
        affectation.setDatefin(dto.getDatefin());


        return affectationRepository.save(affectation);
    }

    public List<Affectation> getByPhase(Long phaseId) {
        return affectationRepository.findByPhaseId(phaseId);
    }

    public List<Affectation> getByEmploye(Long employeId) {
        return affectationRepository.findByEmployeId(employeId);
    }

    public void supprimer(Long employeId, Long phaseId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        if (!affectationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Affectation non trouvée");
        }
        affectationRepository.deleteById(id);
    }

    public Affectation modifier(Long employeId, Long phaseId, AffectationDTO dto) {
        AffectationId id = new AffectationId(employeId, phaseId);
        Affectation existing = affectationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Affectation non trouvée"));

        existing.setDatedebut(dto.getDatedebut());
        existing.setDatefin(dto.getDatefin());

        return affectationRepository.save(existing);
    }

    public Affectation getByPhaseAndEmploye(Long phaseId, Long employeId) {
        return affectationRepository.findByPhaseIdAndEmployeId(phaseId, employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Affectation non trouvée"));
    }
}