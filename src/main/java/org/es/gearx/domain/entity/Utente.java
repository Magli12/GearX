package org.es.gearx.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUtente")
    private int idUtente;

    @Column(name = "Nome")
    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri")
    private String nome;

    @Column(name = "Cognome")
    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri")
    private String cognome;

    @Column(name = "Email", unique = true)
    @Email(message = "Email non valida")
    @NotBlank(message = "L'email non può essere vuota")
    private String email;

    @Column(name = "DataDiNascita")
    @Past(message = "La data di nascita deve essere nel passato")
    private LocalDate dataDiNascita;

    @Column(name = "Sesso")
    @NotBlank(message = "Il sesso non può essere vuoto")
    @Pattern(regexp = "Maschio|Femmina", message = "Il sesso deve essere uno tra: Maschio, Femmina")
    private String sesso;

    @Column(name = "Password")
    @NotBlank(message = "La password non può essere vuota")
    private String password;

    @Column(name = "Username", unique = true)
    @NotBlank(message = "Lo username non può essere vuoto")
    @Size(max = 30, message = "Lo username non può superare i 30 caratteri")
    private String username;

    // Costruttori
    public Utente() {}

    public Utente(String nome, String cognome, String email, LocalDate dataDiNascita, String sesso, String password, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.dataDiNascita = dataDiNascita;
        this.sesso = sesso;
        this.password = password;
        this.username = username;
    }

    // Getters e Setters
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
