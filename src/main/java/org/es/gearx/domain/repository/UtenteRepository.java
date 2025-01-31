package org.es.gearx.domain.repository;

import org.es.gearx.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Trova un utente per username
    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByIdUtente(int id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

