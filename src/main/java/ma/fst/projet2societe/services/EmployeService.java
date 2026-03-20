package ma.fst.projet2societe.services;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.EmployeDTO;
import ma.fst.projet2societe.entities.Employe;
import ma.fst.projet2societe.entities.Profil;
import ma.fst.projet2societe.exceptions.BusinessException;
import ma.fst.projet2societe.exceptions.DuplicateResourceException;
import ma.fst.projet2societe.exceptions.ResourceNotFoundException;
import ma.fst.projet2societe.repositories.EmployeRepository;
import ma.fst.projet2societe.repositories.ProfilRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final ProfilRepository profilRepository;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeDTO> getAll() {
        return employeRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public EmployeDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    public EmployeDTO create(EmployeDTO dto, String rawPassword) {
        if (employeRepository.findByMatricule(dto.getMatricule()).isPresent())
            throw new DuplicateResourceException("Matricule déjà existant : " + dto.getMatricule());
        if (employeRepository.findByLogin(dto.getLogin()).isPresent())
            throw new DuplicateResourceException("Login déjà existant : " + dto.getLogin());
        if (employeRepository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateResourceException("Email déjà existant : " + dto.getEmail());

        Employe employe = toEntity(dto);
        employe.setPassword(passwordEncoder.encode(rawPassword));
        return toDTO(employeRepository.save(employe));
    }

    public EmployeDTO update(Long id, EmployeDTO dto) {
        Employe existing = findOrThrow(id);

        if (!existing.getMatricule().equals(dto.getMatricule()) &&
                employeRepository.findByMatricule(dto.getMatricule()).isPresent())
            throw new DuplicateResourceException("Matricule déjà utilisé : " + dto.getMatricule());

        if (!existing.getLogin().equals(dto.getLogin()) &&
                employeRepository.findByLogin(dto.getLogin()).isPresent())
            throw new DuplicateResourceException("Login déjà utilisé : " + dto.getLogin());

        if (!existing.getEmail().equals(dto.getEmail()) &&
                employeRepository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateResourceException("Email déjà utilisé : " + dto.getEmail());

        existing.setMatricule(dto.getMatricule());
        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setTelephone(dto.getTelephone());
        existing.setEmail(dto.getEmail());
        existing.setLogin(dto.getLogin());
        existing.setAdresse(dto.getAdresse());

        if (dto.getProfilId() != null) {
            Profil profil = profilRepository.findById(dto.getProfilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable : " + dto.getProfilId()));
            existing.setProfil(profil);
        }

        return toDTO(employeRepository.save(existing));
    }

    public void delete(Long id) {
        if (!employeRepository.existsById(id))
            throw new ResourceNotFoundException("Employé non trouvé : " + id);
        employeRepository.deleteById(id);
    }

    public List<EmployeDTO> getDisponibles(String dateDebut, String dateFin) {
        LocalDate debut = LocalDate.parse(dateDebut);
        LocalDate fin   = LocalDate.parse(dateFin);
        if (debut.isAfter(fin))
            throw new BusinessException("dateDebut doit être avant dateFin");
        return employeRepository.findEmployesDisponibles(debut, fin)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // FIX: extracted search into service using repository queries — no more in-memory filtering
    public List<EmployeDTO> search(String nom, String matricule) {
        if (matricule != null && !matricule.isBlank()) {
            return employeRepository.findByMatricule(matricule)
                    .map(e -> List.of(toDTO(e)))
                    .orElse(List.of());
        }
        if (nom != null && !nom.isBlank()) {
            return employeRepository.findByNomContaining(nom)
                    .stream().map(this::toDTO).collect(Collectors.toList());
        }
        return getAll();
    }

    // ---- helpers ----
    private Employe findOrThrow(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + id));
    }

    private Employe toEntity(EmployeDTO dto) {
        Employe e = new Employe();
        e.setMatricule(dto.getMatricule());
        e.setNom(dto.getNom());
        e.setPrenom(dto.getPrenom());
        e.setTelephone(dto.getTelephone());
        e.setEmail(dto.getEmail());
        e.setLogin(dto.getLogin());
        e.setAdresse(dto.getAdresse());
        if (dto.getProfilId() != null) {
            profilRepository.findById(dto.getProfilId()).ifPresent(e::setProfil);
        }
        return e;
    }

    public EmployeDTO toDTO(Employe e) {
        EmployeDTO dto = new EmployeDTO();
        dto.setId(e.getId());
        dto.setMatricule(e.getMatricule());
        dto.setNom(e.getNom());
        dto.setPrenom(e.getPrenom());
        dto.setTelephone(e.getTelephone());
        dto.setEmail(e.getEmail());
        dto.setLogin(e.getLogin());
        dto.setAdresse(e.getAdresse());
        if (e.getProfil() != null) dto.setProfilId(e.getProfil().getId());
        return dto;
    }
}
