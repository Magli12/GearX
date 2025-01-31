package org.es.gearx.web.controller;

import org.es.gearx.domain.entity.Commento;
import org.es.gearx.domain.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/commenti")
public class CommentController {

    @Autowired
    private CommentService commentoService;

    // Endpoint per l'aggiunta di un nuovo commento
    @PostMapping
    public ResponseEntity<Commento> aggiungiCommento(@RequestBody Commento commento,
                                                     @RequestParam int userId) {
        try {
            Commento nuovoCommento = commentoService.aggiungiCommento(commento, userId);
            return ResponseEntity.status(201).body(nuovoCommento);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    // Recupera i commenti di un post
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public String getComments(@RequestParam int postId,
                              @RequestParam(defaultValue = "0") int lastId,
                              @RequestParam(defaultValue = "3") int size,
                              Model model) {

        System.out.println("Parametri ricevuti - postId: " + postId + ", lastId: " + lastId + ", size: " + size);

        // Recupera i commenti dalla service
        List<Commento> commenti = commentoService.findCommentsByPostId(postId, lastId, size);
        System.out.println("Numero di commenti recuperati: " + commenti.size());

        model.addAttribute("commenti", commenti);

        return "commentsXml.xml";
    }

    // Endpoint per controllare se ci sono altri commenti per il post
    @GetMapping("/hasMore")
    @ResponseBody
    public ResponseEntity<Boolean> hasMoreComments(@RequestParam int postId, @RequestParam int lastId) {
        try {
            // Verifica se esistono commenti con ID maggiore di lastId
            System.out.println("PostId: " + postId + ", lastId: " + lastId);
            boolean hasMore = commentoService.hasMoreComments(postId, lastId);
            System.out.println("Ci sono altri commenti? " + hasMore);
            return ResponseEntity.ok(hasMore);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }
    }


}
