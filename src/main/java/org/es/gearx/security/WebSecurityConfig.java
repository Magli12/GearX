package org.es.gearx.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/html/login.html", "/html/register.html", "/css/**", "/img/**").permitAll() // Permetti accesso alle risorse statiche
                        .requestMatchers("/register").permitAll() // Endpoint per la registrazione
                        .anyRequest().authenticated() // Tutte le altre richieste richiedono autenticazione
                )
                .formLogin(form -> form
                        .loginPage("/html/login.html").permitAll() // Pagine di login servite come statiche
                        .loginProcessingUrl("/login") // Endpoint POST per il login
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/html/login.html?error=true") // Pagina di errore
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/html/login.html").permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disabilita CSRF, se necessario
        return http.build();
    }
}

