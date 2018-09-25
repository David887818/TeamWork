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
import java.text.SimpleDateFormat;
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
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
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
        boolean requestStatus = false;
        boolean friendStatus = false;
        User one = userRepository.getOne(id);
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        List<Friend> allFriends = friendRepository.findAllByUserId(user.getId());
        List<Notification> notifications = notificationRepository.findAllByToId(user.getId());
        List<Friend> all1 = friendRepository.findAll();
        for (Friend friend : all1) {
            if (friend.getUser().getId() == user.getId() & friend.getFriend().getId() == friendUser.getId()) {
                requestStatus = true;
                friendStatus = true;

            }
        }
        List<Request> all = requestRepository.findAll();
        for (Request request : all) {
            if (request.getTo().getId() == friendUser.getId()) {
                requestStatus = true;
            }
        }
        modelMap.addAttribute("reqStatus", requestStatus);
        modelMap.addAttribute("friendStatus", friendStatus);
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
        Post post = Post.builder()
                .date(sdf.format(new java.util.Date()))
                .user(user)
                .pic_url(picName)
                .listStatus(ListStatus.FALSE)
                .name(user.getName()+"Add Photo")
                .build();
        postRepository.save(post);
        photosRepository.save(photos);
        return "redirect:/userPhotos/" + id;
    }
}
