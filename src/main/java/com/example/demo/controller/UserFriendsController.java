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

import java.util.List;

@Controller
public class UserFriendsController {
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

    @GetMapping("/userFriends/{id}")
    public String userFriends(ModelMap modelMap, @PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        User one = userRepository.getOne(id);
        user = ((CurrentUser) userDetails).getUser();
        boolean requestStatus = false;
        boolean friendStatus = false;
        List<Friend> friendList = friendRepository.findAllByUserId(one.getId());
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
        modelMap.addAttribute("allFriends", allFriends);
        modelMap.addAttribute("friends", friendList);
        modelMap.addAttribute("photos", one);
        modelMap.addAttribute("us", one);
        return "userFriends";
    }
}
