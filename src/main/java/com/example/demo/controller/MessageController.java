package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class MessageController {
    private User user;
    private User friendUser;
    private User messageUser;
    private User searchedUser;
    private List<Comment> commentList;
    private List<Post> postList;
    private List<UserPhotos> photos;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");

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
    @Autowired
    MessageRepository  messageRepository;

    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;

    @GetMapping("/message/{id}")
    public String findMessagePage(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
        if (id != 0) {
            messageUser = userRepository.findUserById(id);
            modelMap.addAttribute("messageUser", messageUser);
        }
        user = ((CurrentUser) userDetails).getUser();
        List<User> userList = userRepository.findAll();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        List<Message> messages = messageRepository.customGetMessagesByUserAndFriend(user.getId(),id);
        modelMap.addAttribute("messages", messages);
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("users", userList);
        modelMap.addAttribute("user", user);
        return "messagePage";
    }

    @PostMapping(value = "/allMessages/{id}")
    public String Submit(ModelMap modelMap,@PathVariable("id") int id,@AuthenticationPrincipal UserDetails userDetails) {
        if (id != 0) {
            messageUser = userRepository.findUserById(id);
            modelMap.addAttribute("messageUser", messageUser);
        }
        user = ((CurrentUser) userDetails).getUser();
        List<UsersMessage> userMessages = userMessageRepository.getUserMessages(user.getId());
        List<Message> messages = messageRepository.customGetMessagesByUserAndFriend(user.getId(),id);
        modelMap.addAttribute("messages", messages);
        modelMap.addAttribute("userMessages", userMessages);
        modelMap.addAttribute("user", user);
        return "allMessages";
    }

    @GetMapping("/messagePage")
    public String messagePage(@AuthenticationPrincipal UserDetails userDetails) {
        user = ((CurrentUser) userDetails).getUser();
        return "redirect:/message/" + user.getId();
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public String saveHosting(@RequestParam("id") int id, @RequestParam("text") String text,@AuthenticationPrincipal UserDetails userDetails){
        user = ((CurrentUser) userDetails).getUser();
        Message message=Message.builder()
                .from(user)
                .to(userRepository.getOne(id))
                .text(text)
                .date(sdf.format(new java.util.Date()))
                .build();
        messageRepository.save(message);
        return "allMessages";
    }
}
