package com.mony.infra.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;  // Certifique-se que esta importação está correta

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Verifica se o token é válido (aqui você adicionaria a lógica real de validação de token)
    private boolean isValidToken(String token) {
        return true;  // Lógica para validar o token (verificar assinatura JWT ou outros critérios)
    }

    // Cria o objeto de autenticação com base no token válido
    private Authentication getAuthentication(String token) {
        // Aqui você pode retornar um UsernamePasswordAuthenticationToken com as informações do usuário
        return new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // Extrai o token JWT do cabeçalho
            String jwtToken = token.substring(7);

            // Se o token for válido, cria a autenticação
            if (isValidToken(jwtToken)) {
                // Aqui você pode adicionar um usuário ao contexto de segurança (autenticação)
                Authentication authentication = getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
