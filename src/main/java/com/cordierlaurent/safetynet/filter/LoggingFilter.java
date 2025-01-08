package com.cordierlaurent.safetynet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // Récupérer l'URL de base
        String url = req.getRequestURL().toString();

        // Récupérer les paramètres de requête
        String queryString = req.getQueryString();

        // Construire l'URL complète avec les paramètres
        String fullUrl = (queryString != null) ? url + "?" + queryString : url;

        String method = req.getMethod();

        log.info("Requête reçue : [{}] {}", method, fullUrl);

        // Continuer la chaîne de filtres.
        chain.doFilter(request, response);  
    }
}
