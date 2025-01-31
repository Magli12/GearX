package org.es.gearx.security;

import org.es.gearx.domain.entity.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private int userId; // ID utente
    private String username;
    private String password;

    // Getter per l'ID
    public int getUserId() {
        return userId;
    }

    public CustomUserDetails(Utente user) {
        this.userId = user.getIdUtente();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
