package com.example.demo.repository;


import com.example.demo.model.Notification;
import com.example.demo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByToId(int toId);

}
