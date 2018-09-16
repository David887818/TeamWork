package com.example.demo.repository;

import com.example.demo.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    List<PostLike> findAllByPostId(int id);
}
