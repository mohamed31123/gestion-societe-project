package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.LivrableRequest;
import ma.fst.projet2societe.dto.LivrableResponse;
import ma.fst.projet2societe.entities.Livrable;
import ma.fst.projet2societe.entities.Phase;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.DuplicateResourceException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
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

        if (liv.getPhase() != null) {
            res.setPhaseId(liv.getPhase().getId());
            res.setPhaseLibelle(liv.getPhase().getLibelle());

            if (liv.getPhase().getProject() != null) {
                res.setProjectId(liv.getPhase().getProject().getId());
                res.setProjectNom(liv.getPhase().getProject().getNom());
            }
        }

        if (liv.getChemin() != null) {
            res.setDownloadUrl("/api/livrables/" + liv.getId() + "/download");
        }

        return res;
    }

    private String sauvegarderFichier(MultipartFile file, String sousRepertoire) {
        Path uploadPath = Paths.get(uploadDir, sousRepertoire);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new BusinessException("Impossible de créer le dossier upload");
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
            throw new BusinessException("Erreur lors de la sauvegarde du fichier");
        }

        return filePath.toString();
    }

    @Override
    public LivrableResponse create(Long phaseId, LivrableRequest request, MultipartFile file) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable : " + phaseId));

        // FIX: code is now always required (see LivrableRequest @NotBlank)
        // so we always check uniqueness — no null check needed
        if (livrableRepository.findByCode(request.getCode()).isPresent()) {
            throw new DuplicateResourceException("Code livrable déjà utilisé : " + request.getCode());
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

    @Override
    @Transactional(readOnly = true)
    public List<LivrableResponse> getByPhase(Long phaseId) {
        if (!phaseRepository.existsById(phaseId)) {
            throw new ResourceNotFoundException("Phase introuvable : " + phaseId);
        }
        return livrableRepository.findByPhaseId(phaseId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LivrableResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public LivrableResponse update(Long id, LivrableRequest request, MultipartFile file) {
        Livrable livrable = findOrThrow(id);

        if (request.getCode() != null) {
            if (!request.getCode().equals(livrable.getCode())
                    && livrableRepository.findByCode(request.getCode()).isPresent()) {
                throw new DuplicateResourceException("Code livrable déjà utilisé : " + request.getCode());
            }
            livrable.setCode(request.getCode());
        }

        if (request.getLibelle() != null) {
            livrable.setLibelle(request.getLibelle());
        }

        if (request.getDescription() != null) {
            livrable.setDescription(request.getDescription());
        }

        if (file != null && !file.isEmpty()) {
            if (livrable.getChemin() != null) {
                try {
                    Files.deleteIfExists(Paths.get(livrable.getChemin()));
                } catch (IOException e) {
                    // log only — don't fail the update because the old file is gone
                    System.err.println("Impossible de supprimer l'ancien fichier : " + livrable.getChemin());
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

    @Override
    public void delete(Long id) {
        Livrable livrable = findOrThrow(id);

        if (livrable.getChemin() != null) {
            try {
                Files.deleteIfExists(Paths.get(livrable.getChemin()));
            } catch (IOException e) {
                System.err.println("Impossible de supprimer le fichier : " + livrable.getChemin());
            }
        }

        livrableRepository.delete(livrable);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] download(Long id) {
        Livrable livrable = findOrThrow(id);

        if (livrable.getChemin() == null) {
            throw new BusinessException("Aucun fichier associé à ce livrable");
        }

        try {
            return Files.readAllBytes(Paths.get(livrable.getChemin()));
        } catch (IOException e) {
            throw new BusinessException("Fichier introuvable sur le serveur");
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
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable : " + id));
    }
}
