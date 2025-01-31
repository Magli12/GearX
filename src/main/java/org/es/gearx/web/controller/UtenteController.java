package org.es.gearx.web.controller;

import org.es.gearx.domain.entity.Utente;
import org.es.gearx.domain.repository.UtenteRepository;
import org.es.gearx.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UtenteController {
    private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);

    @Autowired
    private UtenteRepository utenteRepository;

    private Optional<CustomUserDetails> getAuthenticatedUserDetails(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return Optional.empty();
        }
        return Optional.of((CustomUserDetails) authentication.getPrincipal());
    }

    @GetMapping("/me")
    public ResponseEntity<Integer> getLoggedUserId(Authentication authentication) {
        return getAuthenticatedUserDetails(authentication)
                .map(userDetails -> {
                    int userId = userDetails.getUserId();
                    logger.info("Utente loggato: {}", userId);
                    return ResponseEntity.ok(userId);
                })
                .orElseGet(() -> {
                    logger.warn("Utente non autenticato o principal non valido");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }

    // Endpoint per visualizzare il profilo dell'utente loggato
    @GetMapping("/profile")
    public ResponseEntity<Utente> getLoggedUser(Authentication authentication) {
        return getAuthenticatedUserDetails(authentication)
                .flatMap(userDetails -> utenteRepository.findByIdUtente(userDetails.getUserId()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}