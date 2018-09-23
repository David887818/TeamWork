package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class UserPhotosController {
    private User user;
    private User friendUser;
    private User messageUser;
    private User searchedUser;
    private List<Comment> commentList;
    private List<Post> postList;
    private List<UserPhotos> photos;

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
    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;
    @GetMapping("/userPhotos/{id}")
    public String userPhotos(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        photos = photosRepository.findAllByUserId(id);
        User one = userRepository.getOne(id);
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        List<Friend> allFriends = friendRepository.findAllByUserId(user.getId());
        List<Notification> notifications = notificationRepository.findAllByToId(user.getId());
        modelMap.addAttribute("notifications", notifications);
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", allFriends);
        modelMap.addAttribute("photos", photos);
        modelMap.addAttribute("us", one);
        return "userPhotos";
    }

    @PostMapping("/addUserPhotos")
    public String addUserPhotos(@RequestParam("user_id") int id, @RequestParam("image") MultipartFile multipartFile) {
        File dir = new File(adPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();

        }
        UserPhotos photos = UserPhotos.builder()
                .user(userRepository.getOne(id))
                .pic_url(picName)
                .build();
        photosRepository.save(photos);
        return "redirect:/userPhotos/" + id;
    }
}
