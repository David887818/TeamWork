package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UsersMessage;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class SettingController {
    private User user;
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostLikeRepository likeRepository;

    @Autowired
    UserMessageRepository userMessageRepository;
    @Autowired
    UserPhotosRepository photosRepository;
    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;

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
    public String changeNameAndSurname(@RequestParam("name") String name, @RequestParam("surname") String surname) {
        user.setName(name);
        user.setSurname(surname);
        userRepository.save(user);
        return "redirect:/settings";
    }

    @PostMapping("/setEmail")
    public String setEmail(@RequestParam("email") String email) {
        user.setEmail(email);
        return "redirect:/settings";
    }
    @PostMapping("/setPass")
    public String setPass(@RequestParam("old_password") String old_password,@RequestParam("password") String password) {
        if (user.getPassword().equals(passwordEncoder.encode(old_password))){
            user.setPassword(passwordEncoder.encode(password));
        }
        return "redirect:/settings";
    }

    @PostMapping("/setPic")
    public String setPic(@RequestParam("pic") MultipartFile file) {
        File dir = new File(adPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();

        }
        user.setPic_url(picName);
        return "redirect:/settings";
    }

    @PostMapping("/setCovPic")
    public String setCovPic(@RequestParam("cover_pic") MultipartFile file) {
        File dir = new File(adPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();

        }
        user.setPic_url_cover(picName);
        return "redirect:/settings";
    }
}
