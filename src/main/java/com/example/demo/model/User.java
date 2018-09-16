package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "status_online")
    @Enumerated(EnumType.STRING)

    private UserStatus userStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_active")
    private ActiveStatus activeStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private String pic_url;
    @Column
    private String pic_url_cover;

    @Column
    boolean verify;
}

