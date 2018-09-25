package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.util.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostLikeRepository likeRepository;
    @Autowired
    UserMessageRepository userMessageRepository;
    @Autowired
    UserPhotosRepository photosRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServiceImpl emailService;
    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;

    @PostMapping("/add")
    public String add(@ModelAttribute User user, ModelMap modelMap) {
        User user1 = userRepository.findUserByEmail (user.getEmail ());
        String errMessage = "";
        if (user1 != null) {
            errMessage += "Error";
            modelMap.addAttribute ("errMessage", errMessage);
            return "index";
        }
        errMessage += "Success";
        user.setPassword (passwordEncoder.encode (user.getPassword ()));
        user.setUserType (UserType.USER);
        user.setToken (UUID.randomUUID ().toString ());
        userRepository.save (user);
        String url = String.format ("http://localhost:8080/verify?token=%s&email=%s", user.getToken (), user.getEmail ());
        String text = String.format ("Dear %s Thank you, you have successfully registered to our Team, Please visit by link in order to activate your profile. %s", user.getName (), url);
        emailService.sendSimpleMessage (user.getEmail (), "Welcome", text);
        modelMap.addAttribute ("errMessage", errMessage);
        return "index";
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verify(@RequestParam("token") String token, @RequestParam("email") String email) {
        User oneByEmail = userRepository.findUserByEmail (email);
        if (oneByEmail != null) {
            if (oneByEmail.getToken () != null && oneByEmail.getToken ().equals (token)) {
                oneByEmail.setToken (null);
                oneByEmail.setUserVerify (UserVerify.TRUE);
                userRepository.save (oneByEmail);
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/verifyError", method = RequestMethod.GET)
    public String verifyError() {
        return "verifyError";
    }

}