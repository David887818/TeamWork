package com.example.demo.controller;

import com.example.demo.model.ListStatus;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CurrentUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/user")
public class UserController {
    private User user;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    PostRepository postRepository;
    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;

    @PostMapping("/addImage")
    public String addProfileImage(@AuthenticationPrincipal UserDetails
                                          userDetails, @RequestParam("image") MultipartFile multipartFile) {
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
        User user = ((CurrentUser) userDetails).getUser();
        user.setPic_url(picName);
        Post post = Post.builder()
                .date(sdf.format(new java.util.Date()))
                .user(user)
                .pic_url(picName)
                .listStatus(ListStatus.FALSE)
                .name(user.getName()+"Add Photo")
                .build();
        postRepository.save(post);
        userRepository.save(user);
        return "redirect:/userPage";
    }

    @PostMapping("/addImageCover")
    public String addCoverImage(@AuthenticationPrincipal UserDetails
                                        userDetails, @RequestParam("img") MultipartFile multipartFile) {
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
        User user = ((CurrentUser) userDetails).getUser();
        user.setPic_url_cover(picName);
        Post post = Post.builder()
                .date(sdf.format(new java.util.Date()))
                .user(user)
                .pic_url(picName)
                .listStatus(ListStatus.FALSE)
                .name(user.getName()+"Add Photo")
                .build();
        postRepository.save(post);
        userRepository.save(user);

        return "redirect:/userPage";
    }

    @GetMapping(value = "/userImg")
    public @ResponseBody
    byte[] userImg(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(adPicDir + picUrl);
        return IOUtils.toByteArray(in);
    }
}
