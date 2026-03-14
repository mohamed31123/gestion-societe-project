package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.OrganismeRequest;
import ma.fst.projet2societe.dto.OrganismeResponse;

import java.util.List;

public interface OrganismeService {

    OrganismeResponse create(OrganismeRequest request);

    OrganismeResponse update(Long id, OrganismeRequest request);

    OrganismeResponse findById(Long id);

    List<OrganismeResponse> findAll();

    void delete(Long id);

    OrganismeResponse findByNom(String nom);
    OrganismeResponse findByCode(String code);
    OrganismeResponse findByNomContact(String nomContact);

}