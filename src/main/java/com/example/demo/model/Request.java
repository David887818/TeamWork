package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Request {

    @Id
    @Column
    @GeneratedValue
    private int id;
    @Column(name = "from_id")
    private int fromId;
    @Column(name = "to_id")
    private int toId;
}
