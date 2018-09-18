package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    private User user;
    private User friendUser;
    private User messageUser;
    private List<Comment> commentList;
    private List<Post> postList;

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

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/friendsPage/{id}")
    public String findFriendPage(@PathVariable("id") int id) {
        friendUser = userRepository.findUserById(id);
        return "redirect:/friend1Page";
    }

    @GetMapping("/userPage")
    public String homePage(ModelMap modelMap) {
        postList = postRepository.findAllByOrderByDateDesc();
        for (Post post : postList) {
            post.setComments(commentRepository.findAllByPostId(post.getId()));
            post.setLikes(likeRepository.findAllByPostId(post.getId()));
            for (PostLike like : post.getLikes()) {
                if (user.getId() == like.getUser().getId()) {
                    post.setListStatus(ListStatus.TRUE);
                } else {
                    post.setListStatus(ListStatus.FALSE);
                }
            }
            List<Post> postList = postRepository.findAllByUserId(user.getId());
            modelMap.addAttribute("userPost", postList);
        }
        List<User> userList = userRepository.findAll();
        List<User> cornerList = userRepository.findAll();
        for (User user1 : cornerList) {
            if (user1.getId() == user.getId()){
                userList.remove(user);
            }
        }
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        modelMap.addAttribute("posts", postList);
        modelMap.addAttribute("comments", commentList);
        return "userPage";
    }

    @GetMapping("/settings")
    public String settings(ModelMap modelMap) {
        List<User> userList = userRepository.findAll();
        List<User> cornerList = userRepository.findAll();
        for (User user1 : cornerList) {
            if (user1.getId() == user.getId()){
                userList.remove(user);
            }
        }
        List<UsersMessage> userMessages= userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        return "settingPage";
    }


    @GetMapping("/userPhotos/{id}")
    public String userPhotos(ModelMap modelMap,@PathVariable("id") int id,@AuthenticationPrincipal UserDetails userDetails) {
        User one = userRepository.getOne(id);
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages= userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("photos", one);
        modelMap.addAttribute("us", user);
        return "userPhotos";
    }
    @GetMapping("/userFriends/{id}")
    public String userFriends(ModelMap modelMap,@PathVariable("id") int id,@AuthenticationPrincipal UserDetails userDetails) {
        User one = userRepository.getOne(id);
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages= userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("photos", one);
        modelMap.addAttribute("us", user);
        return "userFriends";
    }

    @GetMapping("/message/{id}")
    public String findMessagePage(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        messageUser = userRepository.findUserById(id);
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages= userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("users", userList);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("messageUser", messageUser);
        return "messagePage";
    }

    @GetMapping("/friend1Page")
    public String friendPage(ModelMap modelMap) {
        List <Post> postsList = postRepository.findAllByFriendId(friendUser.getId());
        for (Post post : postsList) {
            post.setComments(commentRepository.findAllByPostId(post.getId()));
            post.setLikes(likeRepository.findAllByPostId(post.getId()));
            for (PostLike like : post.getLikes()) {
                if (user.getId() == like.getUser().getId()) {
                    post.setListStatus(ListStatus.TRUE);
                } else {
                    post.setListStatus(ListStatus.FALSE);
                }
            }
            modelMap.addAttribute("posts", postsList);
        }
        List<User> userList = userRepository.findAll();
        List<User> cornerList = userRepository.findAll();
        for (User user1 : cornerList) {
            if (user1.getId() == user.getId()){
                userList.remove(user);
            }
        }
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        modelMap.addAttribute("friend", friendUser);
        modelMap.addAttribute("comments", commentList);
        return "friendPage";
    }

    @GetMapping("/indexPage")
    public String indexPage() {
        return "redirect:/homePage";
    }

    @GetMapping("/homePage")
    public String mainPageUser(ModelMap modelMap) {
        postList = postRepository.findAllByOrderByDateDesc();
        for (Post post : postList) {
            post.setComments(commentRepository.findAllByPostId(post.getId()));
            post.setLikes(likeRepository.findAllByPostId(post.getId()));
            for (PostLike like : post.getLikes()) {
                if (user.getId() == like.getUser().getId()) {
                    post.setListStatus(ListStatus.TRUE);
                } else {
                    post.setListStatus(ListStatus.FALSE);
                }
            }
        }
        List<User> userList = userRepository.findAll();
        List<User> cornerList = userRepository.findAll();
        for (User user1 : cornerList) {
            if (user1.getId() == user.getId()){
                userList.remove(user);
            }
        }
        List<UsersMessage> userMessages= userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("posts", postList);
        modelMap.addAttribute("userMessages", userMessages);
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
