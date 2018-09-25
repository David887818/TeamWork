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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FriendPageController {
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

    @GetMapping("/friendsPage/{id}")
    public String findFriendPage(@PathVariable("id") int id) {
        friendUser = userRepository.findUserById (id);
        return "redirect:/friend1Page";
    }

    @GetMapping("/information/{id}")
    public String information(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser ();
        if (user.getId () == id) {
            return "redirect:/userPage";
        } else {
            return "redirect:/friendsPage/" + id;
        }
    }

    @GetMapping("/friend1Page")
    public String friendPage(ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser ();
        boolean requestStatus = false;
        boolean friendStatus = false;
        List<Post> postsList = postRepository.findAllByFriendId (friendUser.getId ());
        for (Post post : postsList) {
            post.setComments (commentRepository.findAllByPostId (post.getId ()));
            post.setLikes (likeRepository.findAllByPostId (post.getId ()));
            for (PostLike like : post.getLikes ()) {
                if (user.getId () == like.getUser ().getId ()) {
                    post.setListStatus (ListStatus.TRUE);
                } else {
                    post.setListStatus (ListStatus.FALSE);
                }
            }
            modelMap.addAttribute ("posts", postsList);
        }
        List<Friend> all1 = friendRepository.findAll ();
        for (Friend friend : all1) {
            if (friend.getUser ().getId () == user.getId () & friend.getFriend ().getId () == friendUser.getId ()) {
                requestStatus = true;
                friendStatus = true;

            }
        }
        List<Request> all = requestRepository.findAll ();
        for (Request request : all) {
            if (request.getTo ().getId () == friendUser.getId ()) {
                requestStatus = true;
            }
        }
        modelMap.addAttribute ("reqStatus", requestStatus);
        modelMap.addAttribute ("friendStatus", friendStatus);
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages (user.getId ());
        List<Friend> allFriends = friendRepository.findAllByUserId (user.getId ());
        List<Notification> notifications = notificationRepository.findAllByToId (user.getId ());
        modelMap.addAttribute ("notifications", notifications);
        modelMap.addAttribute ("userMessages", userMessages);
        modelMap.addAttribute ("allFriends", allFriends);
        modelMap.addAttribute ("us", user);
        modelMap.addAttribute ("friend", friendUser);
        modelMap.addAttribute ("comments", commentList);
        return "friendPage";
    }

    @GetMapping("/deleteFriend/{id}")
    public String deleteFriend(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser ();
        List<Friend> friends = friendRepository.customGetFriend (id, user.getId ());
        for (Friend friend : friends) {
            friendRepository.delete (friend);
        }
        return "redirect:/friendsPage/" + id;
    }
}
