package com.mony.infra.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final SecurityFilter securityFilter;

    // Injeção de dependência no construtor
    public SecurityConfig(UserDetailsService userDetailsService, SecurityFilter securityFilter) {
        this.userDetailsService = userDetailsService;
        this.securityFilter = securityFilter;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para configurar o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    // Método de configuração do filtro de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Desativa o CSRF para APIs RESTful
                .cors(Customizer.withDefaults())       // Configura o CORS com os padrões
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Define sessão stateless
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/users/login", "/api/users/register","/api/users/validate-otp").permitAll() // Permite acesso livre a login e registro
                                .anyRequest().authenticated() // Requer autenticação para qualquer outra solicitação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  // Adiciona o filtro customizado antes do filtro de autenticação padrão
                .build();
    }
}
