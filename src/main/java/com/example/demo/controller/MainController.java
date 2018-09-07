package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private User user;
    private List<Comment> commentList;


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostLikeRepository likeRepository;
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/userPage")
    public String homePage() {
        return "userPage";
    }

    @GetMapping("/indexPage")
    public String indexPage() {
        return "redirect:/homePage";
    }

    @GetMapping("/homePage")
    public String mainPageUser(ModelMap modelMap) {
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            post.setComments(commentRepository.findAllByPostId(post.getId()));
            post.setLikes(likeRepository.findAllByPostId(post.getId()));
        }
        modelMap.addAttribute("posts", postList);
        List<User> userList = userRepository.findAll();
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        modelMap.addAttribute("comments", commentList);
        return "home";
    }

    @PostMapping("/loginUser")
    public String loginUser(@AuthenticationPrincipal UserDetails userDetails, ModelMap modelMap) {
        user = ((CurrentUser) userDetails).getUser();
        if (user.getUserType() == UserType.USER) {
            modelMap.addAttribute("user", user);
            return "redirect:/homePage";
        }
        return "redirect:/indexPage";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam String comment, @RequestParam("post_id") int postId, @RequestParam("user_id") int userId) {
        Comment commment = Comment.builder()
                .comment(comment)
                .post(postRepository.getOne(postId))
                .user(userRepository.getOne(userId))
                .build();
        commentRepository.save(commment);
        commentList = commentRepository.findAllByPostId(postId);
        return "redirect:/homePage";
    }
}
