package com.example.demo.repository;

import com.example.demo.model.UserPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPhotosRepository extends JpaRepository<UserPhotos, Integer> {

    List<UserPhotos> findAllByUserId(int id);
}
