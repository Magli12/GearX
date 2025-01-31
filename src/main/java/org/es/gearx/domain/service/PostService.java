package org.es.gearx.domain.service;


import org.es.gearx.domain.repository.PostRepository;
import org.es.gearx.domain.repository.UtenteRepository;
import org.es.gearx.domain.entity.Post;
import org.es.gearx.domain.entity.Utente;
import org.es.gearx.web.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;


import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UtenteRepository userRepository;

    @Transactional
    public Post createPost(PostDto postDto, Long userId) {
        // Gestione dell'utente
        Utente author = getUserById(userId);

        // Crea un nuovo oggetto Post
        Post post = new Post();
        post.setContenuto(postDto.getContent());
        post.setAuthor(author);

        // Salva il post nel database
        return postRepository.save(post);
    }

    private Utente getUserById(Long userId) {
        if (userId == null) {
            // Cerca l'utente default con ID 1)
            return userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Utente di default non trovato."));
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public List<Post> findNextPosts(int lastId, int size) {
        List<Post> posts = postRepository.findNextPosts(lastId, PageRequest.of(0, size));
        return posts;
    }

    public int getMaxId() {
        return postRepository.findMaxId();
    }

}



