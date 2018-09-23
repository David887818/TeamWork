package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableAsync
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
                    .email ("greenbot@mail.com")
                    .name ("Green")
                    .surname ("Bot")
                    .password (passwordEncoder.encode ("bot"))
                    .userType (UserType.ADMIN)
                    .gender (Gender.MALE)
                    .userVerify (UserVerify.TRUE)
                    .build ();
            userRepository.save (admin);
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ();
    }
}