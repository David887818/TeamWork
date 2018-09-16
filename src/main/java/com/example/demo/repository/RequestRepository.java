package com.example.demo.repository;

import com.example.demo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findByFromIdAndToId(int fromId, int toId);

    Request findByToIdAndFromId(int toId, int fromId);

    List<Request> findAllByToId(int toId);
}


