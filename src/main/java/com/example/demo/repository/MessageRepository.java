package com.example.demo.repository;

import com.example.demo.model.Message;
import com.example.demo.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    Integer countByToIdAndMessageStatus(int id, MessageStatus messageStatus);

    @Query(value = "select * from message where (from_id=:fromId and to_id=:toId) or (from_id=:toId and to_id=:fromId)", nativeQuery = true)
    List<Message> customGetMessagesByUserAndFriend(@Param("fromId") int fromId, @Param("toId") int toId);

    @Query(value = "select message from Message message where to_id=:toId")
    List<Message> customGetMessagesByToId(@Param("toId") int toId);
}
