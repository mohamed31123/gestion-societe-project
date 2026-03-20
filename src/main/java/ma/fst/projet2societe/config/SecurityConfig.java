package ma.fst.projet2societe.config;

import lombok.RequiredArgsConstructor;
import ma.fst.projet2societe.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    // -------------------------------------------------------
    // FIX #1: Global CORS bean — covers ALL controllers.
    // Previously only 5 out of 10 controllers had @CrossOrigin("*"),
    // meaning PhaseController, ProjectController, LivrableController,
    // OrganismeController, ReportingController would all FAIL from a browser.
    // -------------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // FIX: attach CORS
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // Employes
                        .requestMatchers(HttpMethod.POST,   "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/employes/**").hasAnyRole("ADMIN", "DIRECTEUR")

                        // Organismes
                        .requestMatchers(HttpMethod.POST,   "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN", "DIRECTEUR")

                        // FIX #2: was "/api/projets/**" — controller uses "/api/projects"
                        .requestMatchers(HttpMethod.POST,   "/api/projects/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/projects/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/projects/**").hasAnyRole("SECRETAIRE", "ADMIN", "DIRECTEUR", "CHEF_PROJET")

                        // Phases (nested under /api/projets/{id}/phases AND standalone /api/phases)
                        .requestMatchers(HttpMethod.POST,   "/api/projets/*/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN", "COMPTABLE")
                        .requestMatchers(HttpMethod.DELETE, "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR", "COMPTABLE")
                        .requestMatchers(HttpMethod.GET,    "/api/projets/*/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR", "COMPTABLE")

                        // Affectations
                        .requestMatchers("/api/phases/*/employes/**").hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR")

                        // Livrables
                        .requestMatchers(HttpMethod.POST,   "/api/phases/*/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR")

                        // Documents
                        .requestMatchers(HttpMethod.POST,   "/api/projets/*/documents/**").hasAnyRole("SECRETAIRE", "CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/documents/**").hasAnyRole("SECRETAIRE", "CHEF_PROJET", "ADMIN", "DIRECTEUR")
                        .requestMatchers(HttpMethod.DELETE, "/api/documents/**").hasAnyRole("ADMIN")

                        // Factures
                        .requestMatchers("/api/factures/**", "/api/phases/*/facture/**").hasAnyRole("COMPTABLE", "ADMIN", "DIRECTEUR")

                        // Reporting
                        .requestMatchers("/api/reporting/**").hasAnyRole("DIRECTEUR", "ADMIN", "COMPTABLE")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}