package org.es.gearx.domain.repository;

import org.es.gearx.domain.entity.Commento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Commento, Integer> {

    // Query per trovare i commenti per un dato postId e con la paginazione per lastId
    @Query("SELECT c FROM Commento c WHERE c.post.idPost = :postId AND c.idCommento < :lastId ORDER BY c.idCommento DESC")
    List<Commento> findCommentsByPostId(@Param("postId") int postId,
                                        @Param("lastId") int lastId,
                                        Pageable pageable);

    int countByPost_idPostAndIdCommentoLessThan(int postId, int lastId);
}


