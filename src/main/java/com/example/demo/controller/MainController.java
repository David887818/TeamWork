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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
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

    @GetMapping("/")
    public String mainPage() {
        return "index";
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
        List<Friend> friendList = friendRepository.findAllByUserId(user.getId());
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", friendList);
        modelMap.addAttribute("us", user);
        modelMap.addAttribute("posts", postList);
        modelMap.addAttribute("comments", commentList);
        return "userPage";
    }

    @GetMapping("/message/{id}")
    public String findMessagePage(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        if (id != 0) {
            messageUser = userRepository.findUserById(id);
            modelMap.addAttribute("messageUser", messageUser);
        }
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("users", userList);
        modelMap.addAttribute("user", user);
        return "messagePage";
    }

    @GetMapping("/messagePage")
    public String messagePage(@AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        return "redirect:/message/" + user.getId();
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
        List<Friend> allFriends = friendRepository.findAllByUserId(user.getId());
        List<Request> requests = requestRepository.findAllByToId(user.getId());
        List<Notification> notifications = notificationRepository.findAllByToId(user.getId());
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("notifications", notifications);
        modelMap.addAttribute("requests", requests);
        modelMap.addAttribute("posts", postList);
        modelMap.addAttribute("friends", allFriends);
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

    @GetMapping("/search")
    public String search(@RequestParam("text") String string,ModelMap modelMap){
        List<User> users = userRepository.findAll();
        String[] split = string.split(" ");
        for (User user : users) {
            if (split[0].equals(user.getName()) && split[1].equals(user.getSurname())) {
                modelMap.addAttribute("searched",user);
                return "searchResult";
            }
        }
        return "redirect:/home";
    }

}
