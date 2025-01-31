package org.es.gearx.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "commento")
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CommentID")
    private int idCommento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PostID")
    @JsonBackReference
    private Post post; // Relazione con il post

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorID", nullable = false)
    private Utente author; // Relazione con l'autore del commento

    @Column(name = "CommentTime")
    private LocalTime oraCommento;

    @Column(name = "CommentDate")
    private LocalDate dataCommento;

    @Column(name = "Contenuto")
    private String contenuto;


    @PrePersist
    protected void onCreate() {
        this.dataCommento = LocalDate.now();
        this.oraCommento = LocalTime.now();
    }

    // Costruttori
    public Commento() {}

    public Commento(Post post, Utente author, String contenuto) {
        this.post = post;
        this.author = author;
        this.contenuto = contenuto;
    }

    // Getters e Setters
    public int getIdCommento() {
        return idCommento;
    }

    public void setIdCommento(int idCommento) {
        this.idCommento = idCommento;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public Utente getAuthor() {
        return author;
    }

    public void setAuthor(Utente author) {
        this.author = author;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public LocalDate getDataCommento() {
        return dataCommento;
    }

    public void setDataCommento(LocalDate dataCommento) {
        this.dataCommento = dataCommento;
    }

    public LocalTime getOraCommento() {
        return oraCommento;
    }

    public void setOraCommento(LocalTime oraCommento) {
        this.oraCommento = oraCommento;
    }


}
