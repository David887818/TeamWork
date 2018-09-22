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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class MainController {
    private User user;
    private User friendUser;
    private User messageUser;
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
            if (user1.getId() == user.getId()) {
                userList.remove(user);
            }
        }
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("us", user);
        modelMap.addAttribute("posts", postList);
        modelMap.addAttribute("comments", commentList);
        return "userPage";
    }


    @GetMapping("/userPhotos/{id}")
    public String userPhotos(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        photos = photosRepository.findAllByUserId(id);
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("photos", photos);
        modelMap.addAttribute("us", user);
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

    @GetMapping("/userFriends/{id}")
    public String userFriends(ModelMap modelMap, @PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        User one = userRepository.getOne(id);
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", userList);
        modelMap.addAttribute("photos", one);
        modelMap.addAttribute("us", user);
        return "userFriends";
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
    return "redirect:/message/"+user.getId();
    }


        @GetMapping("/friend1Page")
    public String friendPage(ModelMap modelMap) {
        boolean requestStatus = false;
        List<Post> postsList = postRepository.findAllByFriendId(friendUser.getId());
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
            if (user1.getId() == user.getId()) {
                userList.remove(user);
            }
        }

        List<Friend> all1 = friendRepository.findAll();
        for (Friend friend : all1) {
            if (friend.getUserId() == user.getId() & friend.getFriendId() == friendUser.getId()) {
                requestStatus = true;
            }
        }
        List<Request> all = requestRepository.findAll();
        for (Request request : all) {
            if (request.getTo().getId() == friendUser.getId()) {
                requestStatus = true;
            }
        }
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("reqStatus", requestStatus);
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
            if (user1.getId() == user.getId()) {
                userList.remove(user);
            }
        }

        List<Request> requests = requestRepository.findAllByToId(user.getId());

        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());

        List<Notification> notifications = notificationRepository.findAllByToId(user.getId());

        modelMap.addAttribute("notifications", notifications);
        modelMap.addAttribute("requests", requests);
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
        Notification notification=Notification.builder()
                .notStatus(NotificationStatus.COMMENT)
                .from(userRepository.getOne(userId))
                .to(postRepository.getOne(postId).getUser())
                .build();
        notificationRepository.save(notification);
        return "redirect:/homePage";
    }
}
