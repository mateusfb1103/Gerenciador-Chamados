package com.example.chamado.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationEntryPoint customEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(org.springframework.security.config.Customizer.withDefaults()) // Usa sua config de CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        // Listar TODOS os chamados: Apenas Suporte
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/chamados").hasRole("SUPPORT")

                        // Listar chamados DO USUÁRIO: Ambos podem (o suporte pode querer ver o de um user específico)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/chamados/{userId}").hasAnyRole("USER", "SUPPORT")

                        // Criar chamado: Ambos podem (geralmente suporte também abre chamado interno)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/chamados/**").hasAnyRole("USER", "SUPPORT")

                        // Editar e Deletar: APENAS SUPORTE
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/chamados/**").hasRole("SUPPORT")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/chamados/**").hasRole("SUPPORT")

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customEntryPoint)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
