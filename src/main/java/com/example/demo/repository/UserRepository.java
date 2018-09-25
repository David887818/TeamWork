package com.example.demo.repository;

 import com.example.demo.model.Friend;
 import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;

 import java.util.List;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserById(int id);

    List<User> findAllByNameOrSurname(String name,String surname);
    List<User> findAllByName(String name);

}
