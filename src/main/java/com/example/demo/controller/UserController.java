package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.Request;
import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Value("${webSite.pic.url}")
    private String adPicDir;


    @PostMapping("/add")
    public String add(@ModelAttribute User user, ModelMap modelMap) {
        User user1 = userRepository.findUserByEmail (user.getEmail ());
        String errMessage = "";
        if (user1 != null) {
            errMessage += "Error";
            modelMap.addAttribute ("errMessage", errMessage);
            return "index";
        }
        errMessage += "Success";
        user.setUserType (UserType.USER);
        user.setPassword (passwordEncoder.encode (user.getPassword ()));
        userRepository.save (user);
        modelMap.addAttribute ("errMessage", errMessage);
        return "index";
    }


    @GetMapping(value = "/adImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("pic_url") String pic_url) throws IOException {
        InputStream in = new FileInputStream (adPicDir + pic_url);
        return IOUtils.toByteArray (in);
    }

    @PostMapping("/addImage")
    public String addProfileImage(@AuthenticationPrincipal UserDetails
                                          userDetails, @RequestParam("image") MultipartFile multipartFile) {
        File dir = new File (adPicDir);
        if (!dir.exists ()) {
            dir.mkdirs ();
        }
        String picName = System.currentTimeMillis () + "_" + multipartFile.getOriginalFilename ();
        try {
            multipartFile.transferTo (new File (dir, picName));
        } catch (IOException e) {
            e.printStackTrace ();

        }
        User user = ((CurrentUser) userDetails).getUser ();
        user.setPic_url (picName);
        userRepository.save (user);
        return "redirect:/userPage";
    }

    @PostMapping("/addImageCover")
    public String addCoverImage(@AuthenticationPrincipal UserDetails
                                        userDetails, @RequestParam("img") MultipartFile multipartFile) {
        File dir = new File (adPicDir);
        if (!dir.exists ()) {
            dir.mkdirs ();
        }
        String picName = System.currentTimeMillis () + "_" + multipartFile.getOriginalFilename ();
        try {
            multipartFile.transferTo (new File (dir, picName));
        } catch (IOException e) {
            e.printStackTrace ();

        }
        User user = ((CurrentUser) userDetails).getUser ();
        user.setPic_url_cover (picName);
        userRepository.save (user);

        return "redirect:/userPage";
    }


    @GetMapping("/requests")
    public String requests(ModelMap map, @SessionAttribute("user") User user) {
        List<Request> allByToId = requestRepository.findAllByToId (user.getId ());
        List<User> friendRequests = new ArrayList<> ();
        for (Request request : allByToId) {

            friendRequests.add (userRepository.getOne (request.getFromId ()));
        }
        map.addAttribute ("friendRequests", friendRequests);
        return "request";
    }

    @GetMapping("/sendRequest")
    public HttpServletResponse sendRequest(HttpServletResponse response, @SessionAttribute("friend") User friend, @SessionAttribute("user") User user) {
        if (!friend.equals (user)) {
            Request request = new Request ();
            request.setToId (friend.getId ());
            request.setFromId (user.getId ());
            Request request1 = requestRepository.findByFromIdAndToId (user.getId (), friend.getId ());
            Request request2 = requestRepository.findByToIdAndFromId (user.getId (), friend.getId ());
            Friend byFriendIdAndUserId = friendRepository.findByFriendIdAndUserId (user.getId (), friend.getId ());
            Friend byUserIdAndFriendId = friendRepository.findByUserIdAndFriendId (user.getId (), friend.getId ());
            if (byFriendIdAndUserId == null && byUserIdAndFriendId == null) {
                if (request1 == null && request2 == null) {
                    requestRepository.save (request);
                }
            }
        }

        return response;
    }

    @GetMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("friendId") int fromId, HttpServletResponse response, @SessionAttribute("user") User user, ModelMap map) {
        Friend friend = new Friend ();
        friend.setUserId (user.getId ());
        friend.setFriendId (fromId);
        friendRepository.save (friend);
        Request request = requestRepository.findByFromIdAndToId (fromId, user.getId ());
        requestRepository.deleteById (request.getId ());
        List<Request> allByToId = requestRepository.findAllByToId (user.getId ());
        List<User> friendRequests = new ArrayList<> ();
        for (Request request1 : allByToId) {

            friendRequests.add (userRepository.getOne (request1.getFromId ()));
        }
        map.addAttribute ("friendRequests", friendRequests);
        return "request";
    }

    @GetMapping("/rejectRequest")
    public String rejectRequest(@RequestParam("friendId") int fromId, HttpServletResponse response, @SessionAttribute("user") User user, ModelMap map) {

        Request request = requestRepository.findByFromIdAndToId (fromId, user.getId ());
        requestRepository.deleteById (request.getId ());
        List<Request> allByToId = requestRepository.findAllByToId (user.getId ());
        List<User> friendRequests = new ArrayList<> ();
        for (Request request1 : allByToId) {

            friendRequests.add (userRepository.getOne (request1.getFromId ()));
        }
        map.addAttribute ("friendRequests", friendRequests);
        return "request";
    }

    @GetMapping("/removeFriend")
    public HttpServletResponse removeFriend(@RequestParam("friendForRemove") int id, HttpServletResponse response, @SessionAttribute("user") User user) {

        Friend friend = friendRepository.customGetFriend (user.getId (), id);
        if (friend != null) {

            friendRepository.delete (friend);
        }
        return response;
    }

    @GetMapping("/removeFriends")
    public String removeFriends(@RequestParam("friendForRemove") int id, @SessionAttribute("user") User user) {

        Friend friend = friendRepository.customGetFriend (user.getId (), id);
        if (friend != null) {

            friendRepository.delete (friend);
        }
        return "redirect:/allFriends";
    }

    @GetMapping("/allFriends")
    public String allFriendsPage(ModelMap map, @SessionAttribute("user") User user) {
        List<Friend> friends = friendRepository.serchAllFriends (user.getId ());
        List<User> userFriends = new LinkedList<> ();

        for (Friend friend : friends) {
            if (friend.getFriendId () == user.getId ()) {
                User one = userRepository.getOne (friend.getId ());
                userFriends.add (one);
            } else {
                User one = userRepository.getOne (friend.getFriendId ());
                userFriends.add (one);
            }
        }

        map.addAttribute ("userFriends", userFriends);
        return "friend";
    }

}
