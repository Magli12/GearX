package org.es.gearx.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PostID")
    private int idPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorID", nullable = false)
    private Utente author;

    @Column(name = "PostTime", nullable = false)
    private LocalTime ora;

    @Column(name = "PostDate", nullable = false)
    private LocalDate data;

    @Column(name = "Contenuto", nullable = false)
    private String contenuto;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commento> commenti;

    // Costruttori
    public Post() {}

    public Post(Utente author, LocalTime ora, LocalDate data, String contenuto) {
        this.author = author;
        this.ora = ora;
        this.data = data;
        this.contenuto = contenuto;
    }

    // Getters e Setters
    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public Utente getAuthor() {
        return author;
    }

    public void setAuthor(Utente author) {
        this.author = author;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public List<Commento> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    // Metodo per settare la data e l'ora correnti nel momento della creazione
    @PrePersist
    protected void onCreate() {
        this.data = LocalDate.now();
        this.ora = LocalTime.now();
    }
}
