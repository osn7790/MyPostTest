package com.example.mypost.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user u ORDER BY p.id DESC")
    Page<Post> findAllJoinUser(Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user u WHERE p.id = :id")
    Optional<Post> findByIdJoinUser(@Param("id") Long id);

}
