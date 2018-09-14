package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(int id);
    List<Post> findAllByFriendId(int id);
    List<Post> findAllByOrderByDateDesc();
}
