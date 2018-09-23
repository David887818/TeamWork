package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repository.FriendRepository;
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

@Controller
@RequestMapping("/user")
public class UserController {
    private User user;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;


    @PostMapping("/add")
    public String add(@ModelAttribute User user, ModelMap modelMap) {
        User user1 = userRepository.findUserByEmail(user.getEmail());
        String errMessage = "";
        if (user1 != null) {
            errMessage += "Error";
            modelMap.addAttribute("errMessage", errMessage);
            return "index";
        }
        errMessage += "Success";
        user.setUserType(UserType.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        modelMap.addAttribute("errMessage", errMessage);

        return "index";
    }

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
