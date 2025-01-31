package org.es.gearx.security;

import jakarta.validation.Valid;
import org.es.gearx.domain.entity.Utente;
import org.es.gearx.domain.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    @Autowired
    private UtenteRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute Utente user, BindingResult result) {
        logger.info("Entrato nel metodo di registrazione.");

        // Controlla se l'username è già utilizzato
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("Username già utilizzato.");
            return "redirect:/html/register.html?usernameError=true";
        }

        // Controlla se l'email è già utilizzata
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email già utilizzata.");
            return "redirect:/html/register.html?emailError=true";
        }

        // Controlla eventuali errori di validazione
        if (result.hasErrors()) {
            logger.error("Errori di validazione: " + result.getAllErrors());
            return "redirect:/html/register.html";
        }

        // Salva l'utente
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("Utente salvato nel database.");
        return "redirect:/html/login.html";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Utente autenticato, reindirizzamento a /home");
            return "redirect:/html/home.html";
        }
        logger.info("Utente non autenticato, mostrando pagina di login");
        return "redirect:/html/login.html";
    }

    @GetMapping("/home")
    public String home() {
        logger.info("Accesso alla home.");
        return "redirect:/html/home.html";
    }

}

