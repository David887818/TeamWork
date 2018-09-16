package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.Message;
import com.example.demo.model.MessageStatus;
import com.example.demo.model.User;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Controller
@SessionAttributes("friendIdForMessage")
public class MessageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FriendRepository friendRepository;

    @GetMapping("/messages")
    public String messagesPage(ModelMap map, @SessionAttribute("user") User user) {
        List<Friend> friends = friendRepository.serchAllFriends (user.getId ());
        List<User> userFriends = new LinkedList<> ();

        for (Friend friend : friends) {
            if (friend.getFriendId () == user.getId ()) {
                User one = userRepository.getOne (friend.getUserId ());
                userFriends.add (one);
            } else {
                User one = userRepository.getOne (friend.getFriendId ());
                userFriends.add (one);
            }

        }

        map.addAttribute ("emptyMessage", new Message ());
        map.addAttribute ("userFriends", userFriends);
        return "message";
    }

    @GetMapping("/getMessages")
    public String getMessages(@RequestParam("id") String id, ModelMap map, @SessionAttribute("user") User user) {
        int friendId = Integer.parseInt (id);
        List<Message> messages = messageRepository.customGetMessagesByUserAndFriend (user.getId (), friendId);
        for (Message message : messages) {
            if ((message.getToId () == user.getId ()) && message.getMessageStatus ().equals (MessageStatus.NEW)) {
                message.setMessageStatus (MessageStatus.OLD);
                messageRepository.save (message);
            }
        }
        map.addAttribute ("chat", messages);

        return "messageAjax";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute("emptyMessage") Message message,
                              @SessionAttribute("user") User user,
                              @SessionAttribute("friendIdForMessage") User friend, ModelMap map) throws IOException {


      if (!message.getText ().equals ("")) {
            message.setToId (friend.getId ());
            message.setMessageStatus (MessageStatus.NEW);
            messageRepository.save (message);
        }

        List<Message> messages = messageRepository.customGetMessagesByUserAndFriend (user.getId (), friend.getId ());
        for (Message message111 : messages) {
            if ((message111.getToId () == user.getId ()) && message111.getMessageStatus ().equals (MessageStatus.NEW)) {
                message111.setMessageStatus (MessageStatus.OLD);
                messageRepository.save (message111);
            }
        }

        map.addAttribute ("chat", messages);
        map.addAttribute ("friendIdForMessage", userRepository.getOne (friend.getId ()));
        return "messageAjax";

    }

    @GetMapping("/getFriendProfileMessage")
    public String getFriendProfileMessage(@RequestParam("id2") String id, ModelMap map) {
        int idd = Integer.parseInt (id);
        User one = userRepository.getOne (idd);
        map.addAttribute ("friendIdForMessage", one);
        return "friendProfileMessage";
    }
}
