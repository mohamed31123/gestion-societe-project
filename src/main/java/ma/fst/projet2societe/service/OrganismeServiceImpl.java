package ma.fst.projet2societe.service;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.OrganismeRequest;
import ma.fst.projet2societe.dto.OrganismeResponse;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.entities.Organisme;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.repositories.OrganismeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public  class OrganismeServiceImpl implements OrganismeService {

    private final OrganismeRepository organismeRepository;
    private final EmployeRepository employeRepository;


    public OrganismeResponse create(OrganismeRequest request) {

        Employe employe = employeRepository.findById(request.getIdEmploye())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        Organisme organisme = new Organisme();

        organisme.setCode(request.getCode());
        organisme.setNom(request.getNom());
        organisme.setNomEmail(request.getNomEmail());
        organisme.setNomContact(request.getNomContact());
        organisme.setSiteWeb(request.getSiteWeb());
        organisme.setTelephone(request.getTelephone());
        organisme.setEmploye(employe);

        Organisme saved = organismeRepository.save(organisme);

        return mapToResponse(saved);
    }


    public OrganismeResponse update(Long id, OrganismeRequest request) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable"));

        Employe employe = employeRepository.findById(request.getIdEmploye())
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        organisme.setCode(request.getCode());
        organisme.setNom(request.getNom());
        organisme.setNomEmail(request.getNomEmail());
        organisme.setNomContact(request.getNomContact());
        organisme.setSiteWeb(request.getSiteWeb());
        organisme.setTelephone(request.getTelephone());
        organisme.setEmploye(employe);

        Organisme updated = organismeRepository.save(organisme);

        return mapToResponse(updated);
    }


    public OrganismeResponse findById(Long id) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable"));

        return mapToResponse(organisme);
    }

    @Override
    public List<OrganismeResponse> findAll() {

        return organismeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable"));
        organismeRepository.delete(organisme);
    }

    private OrganismeResponse mapToResponse(Organisme organisme) {

        OrganismeResponse response = new OrganismeResponse();

        response.setId(organisme.getId());
        response.setCode(organisme.getCode());
        response.setNom(organisme.getNom());
        response.setNomEmail(organisme.getNomEmail());
        response.setNomContact(organisme.getNomContact());
        response.setSiteWeb(organisme.getSiteWeb());
        response.setTelephone(organisme.getTelephone());

        if (organisme.getEmploye() != null) {
            response.setIdEmploye(organisme.getEmploye().getId());
        }

        return response;
    }



    @Override
    public OrganismeResponse findByNom(String nom) {
        return null;
    }

    @Override
    public OrganismeResponse findByCode(String code) {
        return null;
    }

    @Override
    public OrganismeResponse findByNomContact(String nomContact) {
        return null;
    }
}