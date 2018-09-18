package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UsersMessage;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SettingController {
    private User user;
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

    @GetMapping("/settings")
    public String settings(ModelMap modelMap, @AuthenticationPrincipal UserDetails userdetails) {
        user = ((CurrentUser) userdetails).getUser();
        List<User> userList = userRepository.findAll();
        List<User> cornerList = userRepository.findAll();
        for (User user1 : cornerList) {
            if (user1.getId() == user.getId()) {
                userList.remove(user);
            }
        }
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        return "settingPage";
    }
    @PostMapping("/setName")
    public String changeNameAndSurname(@RequestParam("name") String name,@RequestParam("surname") String surname){
       user.setName(name);
//       user.setId(id);
       user.setSurname(surname);
       userRepository.save(user);
        return "redirect:/settings";
    }

    @PostMapping("/setEmail")
    public String setEmail(@RequestParam("email") String email,@RequestParam("user_id") int id){
        return "";
    }
}
