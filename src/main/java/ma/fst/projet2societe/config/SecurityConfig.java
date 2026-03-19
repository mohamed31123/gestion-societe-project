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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // -------------------------------------------------------
    // Endpoints publics (pas besoin de token)
    // -------------------------------------------------------
    private static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ----- Public -----
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // ----- Employes : ADMIN seulement -----
                        .requestMatchers(HttpMethod.POST,   "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/employes/**").hasAnyRole("ADMIN", "DIRECTEUR")

                        // ----- Organismes : SECRETAIRE + ADMIN -----
                        .requestMatchers(HttpMethod.POST,   "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/organismes/**").hasAnyRole("SECRETAIRE", "ADMIN", "DIRECTEUR")

                        // ----- Projets : SECRETAIRE + ADMIN, lecture DIRECTEUR + CHEF_PROJET -----
                        .requestMatchers(HttpMethod.POST,   "/api/projets/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/projets/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projets/**").hasAnyRole("SECRETAIRE", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/projets/**")
                        .hasAnyRole("SECRETAIRE", "ADMIN", "DIRECTEUR", "CHEF_PROJET")

                        // ----- Phases : CHEF_PROJET, lecture DIRECTEUR -----
                        .requestMatchers(HttpMethod.POST,   "/api/phases/**", "/api/projets/*/phases/**")
                        .hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN", "COMPTABLE")
                        .requestMatchers(HttpMethod.DELETE, "/api/phases/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/phases/**")
                        .hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR", "COMPTABLE")

                        // ----- Affectations : CHEF_PROJET -----
                        .requestMatchers("/api/phases/*/employes/**")
                        .hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR")

                        // ----- Livrables : CHEF_PROJET -----
                        .requestMatchers(HttpMethod.POST,   "/api/livrables/**", "/api/phases/*/livrables/**")
                        .hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/livrables/**").hasAnyRole("CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/livrables/**")
                        .hasAnyRole("CHEF_PROJET", "ADMIN", "DIRECTEUR")

                        // ----- Documents : SECRETAIRE + CHEF_PROJET -----
                        .requestMatchers(HttpMethod.POST,   "/api/documents/**", "/api/projets/*/documents/**")
                        .hasAnyRole("SECRETAIRE", "CHEF_PROJET", "ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/documents/**")
                        .hasAnyRole("SECRETAIRE", "CHEF_PROJET", "ADMIN", "DIRECTEUR")
                        .requestMatchers(HttpMethod.DELETE, "/api/documents/**").hasAnyRole("ADMIN")

                        // ----- Factures : COMPTABLE -----
                        .requestMatchers("/api/factures/**", "/api/phases/*/facture/**")
                        .hasAnyRole("COMPTABLE", "ADMIN", "DIRECTEUR")

                        // ----- Reporting : DIRECTEUR + ADMIN -----
                        .requestMatchers("/api/reporting/**").hasAnyRole("DIRECTEUR", "ADMIN", "COMPTABLE")

                        // Tout le reste : authentifié
                        //Temporairement pour tester
                        .anyRequest().permitAll()
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
