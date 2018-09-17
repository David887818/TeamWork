package com.example.demo.repository;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.model.UsersMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UsersMessage, Integer> {

    @Query(value = "SELECT * FROM `user_message` WHERE `from_id`=:id", nativeQuery = true)
    List<UsersMessage> getUserMessages(@Param("id") int id);

}
