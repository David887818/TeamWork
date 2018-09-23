package com.example.demo.repository;

 import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserById(int id);

    List<User> findUsersById(int id);


}
