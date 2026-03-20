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

import java.time.LocalDate;
import java.util.List;

@Service
// FIX: was using @Autowired field injection — switched to constructor injection with @RequiredArgsConstructor
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

        // 1. Pas de doublon
        AffectationId affectationId = new AffectationId(dto.getEmployeId(), dto.getPhaseId());
        if (affectationRepository.existsById(affectationId)) {
            throw new DuplicateResourceException("Affectation déjà existante pour cet employé et cette phase");
        }

        // 2. Cohérence des dates
        if (dto.getDatedebut().after(dto.getDatefin())) {
            throw new BusinessException("La date début doit être avant la date fin");
        }

        // 3. Dates incluses dans la phase
        if (dto.getDatedebut().before(phase.getDateDebut()) ||
                dto.getDatefin().after(phase.getDateFin())) {
            throw new BusinessException("Les dates d'affectation doivent être incluses dans la période de la phase");
        }

        // 4. Employé disponible sur la période
        LocalDate debut = dto.getDatedebut().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = dto.getDatefin().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        List<Employe> disponibles = employeRepository.findEmployesDisponibles(debut, fin);
        boolean estDisponible = disponibles.stream()
                .anyMatch(e -> e.getId().equals(dto.getEmployeId()));
        if (!estDisponible) {
            throw new BusinessException("Cet employé n'est pas disponible sur la période demandée");
        }

        Affectation affectation = new Affectation();
        affectation.setId(affectationId);
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
        AffectationId affectationId = new AffectationId(employeId, phaseId);
        if (!affectationRepository.existsById(affectationId)) {
            throw new ResourceNotFoundException("Affectation non trouvée");
        }
        affectationRepository.deleteById(affectationId);
    }

    public Affectation modifier(Long employeId, Long phaseId, AffectationDTO dto) {
        AffectationId affectationId = new AffectationId(employeId, phaseId);
        Affectation existing = affectationRepository.findById(affectationId)
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
