package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.LivrableRequest;
import ma.fst.projet2societe.dto.LivrableResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LivrableService {


    LivrableResponse create(Long phaseId, LivrableRequest request, MultipartFile file);

    List<LivrableResponse> getByPhase(Long phaseId);


    LivrableResponse getById(Long id);


    LivrableResponse update(Long id, LivrableRequest request, MultipartFile file);

    void delete(Long id);

    // Retourne les bytes du fichier pour le telechargement
    byte[] download(Long id);

    String getContentType(Long id);
    String getNomFichier(Long id);
}