package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run (DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = userRepository.findUserByEmail ("admin@mail.com");
        if (user == null) {
            User admin = User.builder ()
                    .email ("admin@mail.com")
                    .name ("admin")
                    .surname ("admin")
                    .password (passwordEncoder.encode ("admin"))
                    .userType (UserType.ADMIN)
                    .gender (Gender.MALE)
                    .build ();
            userRepository.save (admin);
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ();
    }
}