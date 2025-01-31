package org.es.gearx.domain.repository;

import org.es.gearx.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.idPost < :lastId ORDER BY p.idPost DESC")
    List<Post> findNextPosts(@Param("lastId") int lastId, Pageable pageable);

    @Query("SELECT MAX(p.idPost) FROM Post p")
    int findMaxId(); 
}




