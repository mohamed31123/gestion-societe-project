package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.FactureDTO;
import ma.fst.projet2societe.entities.Facture;
import ma.fst.projet2societe.entities.Phase;
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

    // créer une facture pour une phase terminée
    public FactureDTO create(Long phaseId, FactureDTO dto) {

        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase non trouvée"));

        // phase doit être terminée
        if (!phase.isEtatRealisation()) {
            throw new RuntimeException("La phase doit être terminée avant facturation");
        }

        //phase déjà facturée
        if (phase.isEtatFacturation()) {
            throw new RuntimeException("Cette phase est déjà facturée");
        }

        // créer la facture
        Facture facture = new Facture();
        facture.setCode(dto.getCode());
        facture.setDateFacture(new Date());
        facture.setPhase(phase);

        // mettre à jour l'état facturation de la phase
        phase.setEtatFacturation(true);
        phaseRepository.save(phase);

        return mapToDTO(factureRepository.save(facture));
    }

    // lister toutes les factures
    public List<FactureDTO> getAll() {
        return factureRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // consulter une facture
    public FactureDTO getById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));
        return mapToDTO(facture);
    }

    // modifier une facture
    public FactureDTO update(Long id, FactureDTO dto) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        // modifier tous les champs
        facture.setCode(dto.getCode());
        facture.setDateFacture(dto.getDateFacture());

        // modifier la phase si elle change
        if (dto.getPhaseId() != null) {
            Phase phase = phaseRepository.findById(dto.getPhaseId())
                    .orElseThrow(() -> new RuntimeException("Phase non trouvée"));
            facture.setPhase(phase);
        }

        return mapToDTO(factureRepository.save(facture));
    }

    // supprimer une facture
    public void delete(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        // remettre etatFacturation à false
        Phase phase = facture.getPhase();
        phase.setEtatFacturation(false);
        phaseRepository.save(phase);

        factureRepository.deleteById(id);
    }

    // mapper entité vers DTO
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

    // mapper pour Phase
    private FactureDTO mapPhaseToDTO(Phase phase) {
        FactureDTO dto = new FactureDTO();
        dto.setPhaseId(phase.getId());
        dto.setPhaseCode(phase.getCode());
        dto.setPhaseLibelle(phase.getLibelle());
        dto.setEtatPaiement(phase.isEtatPaiement());
        return dto;
    }
}