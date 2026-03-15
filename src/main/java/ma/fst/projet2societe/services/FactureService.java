package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.FactureDTO;
import ma.fst.projet2societe.entities.Facture;
import ma.fst.projet2societe.entities.Phase;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.FactureRepository;
import ma.fst.projet2societe.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    public FactureDTO create(Long phaseId, FactureDTO dto) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase non trouvée"));

        if (!phase.isEtatRealisation()) {
            throw new BusinessException("La phase doit être terminée avant facturation");
        }
        if (phase.isEtatFacturation()) {
            throw new BusinessException("Cette phase est déjà facturée");
        }

        Facture facture = new Facture();
        facture.setCode(dto.getCode());
        facture.setDateFacture(new Date());
        facture.setPhase(phase);

        phase.setEtatFacturation(true);
        phaseRepository.save(phase);

        return mapToDTO(factureRepository.save(facture));
    }

    public List<FactureDTO> getAll() {
        return factureRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public FactureDTO getById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée"));
        return mapToDTO(facture);
    }

    public FactureDTO update(Long id, FactureDTO dto) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée"));

        facture.setCode(dto.getCode());
        facture.setDateFacture(dto.getDateFacture());

        if (dto.getPhaseId() != null) {
            Phase phase = phaseRepository.findById(dto.getPhaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Phase non trouvée"));
            facture.setPhase(phase);
        }

        return mapToDTO(factureRepository.save(facture));
    }

    public void delete(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée"));

        Phase phase = facture.getPhase();
        phase.setEtatFacturation(false);
        phaseRepository.save(phase);
        factureRepository.deleteById(id);
    }

    private FactureDTO mapToDTO(Facture facture) {
        FactureDTO dto = new FactureDTO();
        dto.setId(facture.getId());
        dto.setCode(facture.getCode());
        dto.setDateFacture(facture.getDateFacture());
        if (facture.getPhase() != null) {
            dto.setPhaseId(facture.getPhase().getId());
            dto.setPhaseCode(facture.getPhase().getCode());
            dto.setPhaseLibelle(facture.getPhase().getLibelle());
            dto.setEtatPaiement(facture.getPhase().isEtatPaiement());
        }
        return dto;
    }

    public List<FactureDTO> getPhasesTermineesNonFacturees() {
        return factureRepository.findPhasesTermineesNonFacturees()
                .stream().map(this::mapPhaseToDTO).collect(Collectors.toList());
    }

    public List<FactureDTO> getPhasesFactureesNonPayees() {
        return factureRepository.findPhasesFactureesNonPayees()
                .stream().map(this::mapPhaseToDTO).collect(Collectors.toList());
    }

    public List<FactureDTO> getPhasesPayees() {
        return factureRepository.findPhasesPayees()
                .stream().map(this::mapPhaseToDTO).collect(Collectors.toList());
    }

    private FactureDTO mapPhaseToDTO(Phase phase) {
        FactureDTO dto = new FactureDTO();
        dto.setPhaseId(phase.getId());
        dto.setPhaseCode(phase.getCode());
        dto.setPhaseLibelle(phase.getLibelle());
        dto.setEtatPaiement(phase.isEtatPaiement());
        return dto;
    }
}