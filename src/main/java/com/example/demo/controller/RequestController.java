package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.Request;
import com.example.demo.model.User;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class RequestController {
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
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    FriendRepository friendRepository;

    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;


    @GetMapping("/addRequest/{id}")
    public String addRequest(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        Request request = Request.builder()
                .from(user)
                .to(userRepository.getOne(id))
                .build();
        requestRepository.save(request);
        return "redirect:/friendsPage/" + id;
    }

    @GetMapping("/acceptRequest/{id}/{req_id}")
    public String acceptRequest(@PathVariable("id") int id, @PathVariable("req_id") int req_id, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        Friend friend = Friend.builder()
                .user(user)
                .friend(userRepository.getOne(id))
                .build();
        Friend friend1 = Friend.builder()
                .user(userRepository.getOne(id))
                .friend(user)
                .build();
        friendRepository.save(friend);
        friendRepository.save(friend1);
        requestRepository.deleteById(req_id);
        return "redirect:/homePage";
    }

    @GetMapping("/rejectRequest/{id}")
    public String rejectRequest(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        requestRepository.deleteById(id);
        return "redirect:/homePage";
    }

}
