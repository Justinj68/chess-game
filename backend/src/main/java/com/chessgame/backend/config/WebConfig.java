package com.chessgame.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Autoriser les requêtes venant de http://localhost:5173
        registry.addMapping("/api/**")  // Ajuste l'URL pour ton API
                .allowedOrigins("http://localhost:5173")  // Frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Autoriser les méthodes spécifiques
                .allowedHeaders("*") //"Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", 
                                 //"Authorization", "X-Requested-With", "requestId", "Correlation-Id")  // Spécifie les en-têtes autorisés
                .allowCredentials(true);  // Autoriser les cookies (si nécessaire)
    }
}

