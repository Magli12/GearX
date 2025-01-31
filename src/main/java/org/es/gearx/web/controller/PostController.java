package org.es.gearx.web.controller;

import org.es.gearx.domain.service.PostService;
import org.es.gearx.domain.entity.Post;
import org.es.gearx.web.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;  //logica di business

    // Endpoint per creare un nuovo post
    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostDto postDto,
                                           @RequestParam(required = false) Long userId) {
        try {
            Post createdPost = postService.createPost(postDto, userId);
            return ResponseEntity.status(201).body(createdPost);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }


    @GetMapping(value = "/generateXml", produces = MediaType.APPLICATION_XML_VALUE)
    public String generateXml(@RequestParam(defaultValue = "0") int lastId,
                              @RequestParam(defaultValue = "3") int size,
                              Model model) {

        // Recupera il massimo ID dal database
        int maxId = postService.getMaxId();
        if (lastId == 0) {
            lastId = maxId +1;
        }

        // Recupera i post dal servizio
        List<Post> posts = postService.findNextPosts(lastId, size);

        // Aggiungi i post al modello
        model.addAttribute("posts", posts);

        // Restituisci il nome della vista Thymeleaf
        return "postsXml.xml";  // Il template Thymeleaf genererà l'XML
    }




}


