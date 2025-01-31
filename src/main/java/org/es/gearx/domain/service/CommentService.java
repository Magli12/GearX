package org.es.gearx.domain.service;

import org.es.gearx.domain.repository.CommentRepository;
import org.es.gearx.domain.repository.PostRepository;
import org.es.gearx.domain.repository.UtenteRepository;
import org.es.gearx.domain.entity.Commento;
import org.es.gearx.domain.entity.Post;
import org.es.gearx.domain.entity.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentoRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    // Aggiungere un nuovo commento
    @Transactional
    public Commento aggiungiCommento(Commento commento, int userId) {
        // Recupera l'utente
        Utente utente = utenteRepository.findByIdUtente(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Recupera il post
        Post post = postRepository.findById(commento.getPost().getIdPost())
                .orElseThrow(() -> new RuntimeException("Post non trovato"));

        // Imposta l'utente e il post
        commento.setAuthor(utente);
        commento.setPost(post);

        if (commento.getContenuto() == null || commento.getContenuto().trim().isEmpty()) {
            throw new IllegalArgumentException("Il contenuto del commento non può essere vuoto.");
        }

        // Salva e restituisci il commento
        return commentoRepository.save(commento);
    }

    public List<Commento> findCommentsByPostId(int postId, int lastId, int size) {
        Pageable pageable = PageRequest.of(0, size); // Pagina 0 (primi risultati) e la dimensione
        return commentoRepository.findCommentsByPostId(postId, lastId, pageable);
    }

    // Verifica se ci sono più commenti per il post
    public boolean hasMoreComments(int postId, int lastId) {
        return commentoRepository.countByPost_idPostAndIdCommentoLessThan(postId, lastId) > 0;
    }





}

