package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.PhaseRequest;
import ma.fst.projet2societe.dto.PhaseResponse;

import java.util.List;

public interface PhaseService {


    PhaseResponse create(Long projetId, PhaseRequest request);
    PhaseResponse update(Long id, PhaseRequest request);
    PhaseResponse findById(Long id);
    List<PhaseResponse> findByProjet(Long projetId);
    void delete(Long id);


    PhaseResponse setRealisation(Long id);   // PATCH /phases/{id}/realisation
    PhaseResponse setFacturation(Long id);   // PATCH /phases/{id}/facturation
    PhaseResponse setPaiement(Long id);      // PATCH /phases/{id}/paiement
}