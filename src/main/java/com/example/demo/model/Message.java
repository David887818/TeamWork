package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String text;
    @Column(name = "from_id")
    private int fromId;
    @Column(name = "to_id")
    private int toId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus messageStatus;
    @Column
    private String timestamp;
}
