package ma.fst.projet2societe.service;

import ma.fst.projet2societe.dto.LivrableRequest;
import ma.fst.projet2societe.dto.LivrableResponse;
import ma.fst.projet2societe.entities.Livrable;
import ma.fst.projet2societe.entities.Phase;
import ma.fst.projet2societe.repositories.LivrableRepository;
import ma.fst.projet2societe.repositories.PhaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LivrableServiceImpl implements LivrableService {

    private final LivrableRepository livrableRepository;
    private final PhaseRepository phaseRepository;

    @Value("${app.upload.dir:uploads/livrables}")
    private String uploadDir;


    private LivrableResponse toResponse(Livrable liv) {

        LivrableResponse res = new LivrableResponse();

        res.setId(liv.getId());
        res.setCode(liv.getCode());
        res.setLibelle(liv.getLibelle());
        res.setDescription(liv.getDescription());
        res.setChemin(liv.getChemin());
        res.setNomFichier(liv.getNomFichier());
        res.setContentType(liv.getContentType());
        res.setTailleFichier(liv.getTailleFichier());
        res.setFichierPresent(liv.getChemin() != null);

        // Phase
        if (liv.getPhase() != null) {

            res.setPhaseId(liv.getPhase().getId());
            res.setPhaseLibelle(liv.getPhase().getLibelle());

            // Project
            if (liv.getPhase().getProject() != null) {
                res.setProjectId(liv.getPhase().getProject().getId());
                res.setProjectNom(liv.getPhase().getProject().getNom());
            }
        }

        // url download
        if (liv.getChemin() != null) {
            res.setDownloadUrl("/api/livrables/" + liv.getId() + "/download");
        }

        return res;
    }

    // sauvegarde fichier
    private String sauvegarderFichier(MultipartFile file, String sousRepertoire) {

        Path uploadPath = Paths.get(uploadDir, sousRepertoire);

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier upload", e);
        }

        String originalFilename = file.getOriginalFilename();

        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueName = UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(uniqueName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde fichier", e);
        }

        return filePath.toString();
    }

    // create
    @Override
    public LivrableResponse create(Long phaseId, LivrableRequest request, MultipartFile file) {

        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase introuvable : " + phaseId));

        // verification code null
        if (request.getCode() != null) {
            if (livrableRepository.findByCode(request.getCode()).isPresent()) {
                throw new RuntimeException("Code livrable déjà utilisé : " + request.getCode());
            }
        }

        Livrable livrable = new Livrable();

        livrable.setCode(request.getCode());
        livrable.setLibelle(request.getLibelle());
        livrable.setDescription(request.getDescription());
        livrable.setPhase(phase);

        if (file != null && !file.isEmpty()) {

            String chemin = sauvegarderFichier(file, "phase-" + phaseId);

            livrable.setChemin(chemin);
            livrable.setNomFichier(file.getOriginalFilename());
            livrable.setContentType(file.getContentType());
            livrable.setTailleFichier(file.getSize());
        }

        return toResponse(livrableRepository.save(livrable));
    }


    // GET BY PHASE
    @Override
    @Transactional(readOnly = true)
    public List<LivrableResponse> getByPhase(Long phaseId) {

        if (!phaseRepository.existsById(phaseId)) {
            throw new RuntimeException("Phase introuvable : " + phaseId);
        }

        return livrableRepository.findByPhaseId(phaseId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    @Override
    @Transactional(readOnly = true)
    public LivrableResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    // UPDATE
    @Override
    public LivrableResponse update(Long id, LivrableRequest request, MultipartFile file) {

        Livrable livrable = findOrThrow(id);

        // UPDATE Code
        if (request.getCode() != null) {

            if (!request.getCode().equals(livrable.getCode())
                    && livrableRepository.findByCode(request.getCode()).isPresent()) {
                throw new RuntimeException("Code livrable déjà utilisé : " + request.getCode());
            }

            livrable.setCode(request.getCode());
        }

        // UPDATE Libelle
        if (request.getLibelle() != null) {
            livrable.setLibelle(request.getLibelle());
        }

        // UPDATE Description
        if (request.getDescription() != null) {
            livrable.setDescription(request.getDescription());
        }

        // UPDATE File
        if (file != null && !file.isEmpty()) {

            if (livrable.getChemin() != null) {
                try {
                    Files.deleteIfExists(Paths.get(livrable.getChemin()));
                } catch (IOException e) {
                    System.err.println("Impossible supprimer ancien fichier");
                }
            }

            String chemin = sauvegarderFichier(file, "phase-" + livrable.getPhase().getId());

            livrable.setChemin(chemin);
            livrable.setNomFichier(file.getOriginalFilename());
            livrable.setContentType(file.getContentType());
            livrable.setTailleFichier(file.getSize());
        }

        return toResponse(livrableRepository.save(livrable));
    }


    // Delete
    @Override
    public void delete(Long id) {

        Livrable livrable = findOrThrow(id);

        if (livrable.getChemin() != null) {
            try {
                Files.deleteIfExists(Paths.get(livrable.getChemin()));
            } catch (IOException e) {
                System.err.println("Impossible supprimer fichier");
            }
        }

        livrableRepository.delete(livrable);
    }

    // Download
    @Override
    @Transactional(readOnly = true)
    public byte[] download(Long id) {

        Livrable livrable = findOrThrow(id);

        if (livrable.getChemin() == null) {
            throw new RuntimeException("Aucun fichier pour ce livrable");
        }

        try {
            return Files.readAllBytes(Paths.get(livrable.getChemin()));
        } catch (IOException e) {
            throw new RuntimeException("Fichier introuvable", e);
        }
    }

    @Override
    public String getContentType(Long id) {
        return findOrThrow(id).getContentType();
    }

    @Override
    public String getNomFichier(Long id) {
        return findOrThrow(id).getNomFichier();
    }


    private Livrable findOrThrow(Long id) {
        return livrableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livrable introuvable : " + id));
    }
}
