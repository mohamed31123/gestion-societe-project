package ma.fst.projet2societe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.dto.auth.ChangePasswordRequest;
import ma.fst.projet2societe.dto.auth.LoginRequest;
import ma.fst.projet2societe.dto.auth.LoginResponse;
import ma.fst.projet2societe.dto.auth.MeResponse;
import ma.fst.projet2societe.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Login, profil connecté et changement de mot de passe")
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "Connexion — retourne un token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @Operation(
            summary = "Retourne les informations de l'utilisateur connecté",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getMe(userDetails.getUsername()));
    }


    @Operation(
            summary = "Changer le mot de passe de l'utilisateur connecté",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }
}
