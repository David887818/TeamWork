package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
    @NotEmpty(message = "name can not be empty")
    private String name;
    @Column
    private String surname;
    @Column
    @Email(message = "please input valid email")
    @NotEmpty(message = "email can not be empty")
    private String email;
    @Column
    @NotEmpty(message = "password can not be empty")
    private String password;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private String pic_url;
    @Column
    private String pic_url_cover;
    @Enumerated(EnumType.STRING)
    private UserVerify userVerify;
    @Column
    private String token;

}

