package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserType;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Value("${webSite.pic.url}")
    private String adPicDir;

    @PostMapping("/add")
    public String add(@ModelAttribute User user, ModelMap modelMap) {
        List<User> all = userRepository.findAll();
        boolean userData =false;
        for (User user1 : all) {
            if (user.getEmail().equals(user1.getEmail())){
                userData=true;
                break;
            }
        }

        if (true){
            modelMap.addAttribute("errMessage","user already registered");
            return "index";
        }else {
            user.setUserType(UserType.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            modelMap.addAttribute("reg","You have registered");
            return "index";
        }
    }



    @GetMapping(value = "/adImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("pic_url") String pic_url) throws IOException {
        InputStream in = new FileInputStream(adPicDir + pic_url);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/addImage")
    public String addImages(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("image") MultipartFile multipartFile) {
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
}
