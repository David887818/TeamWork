package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/post")
public class PostController {
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-mm");
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Value("${webSite.pic.url}")
    private String adPicDir;

    @PostMapping("/addPost")
    public String addAdvertise(@ModelAttribute Post post,@RequestParam("user_id") int id,

                               @RequestParam("image") MultipartFile multipartFile) {
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
        post.setUser(userRepository.getOne(id));
        post.setPic_url(picName);
        post.setDate(sdf.format(new java.util.Date()));
        postRepository.save(post);
        return "redirect:/homePage";
    }

    @GetMapping(value = "/userImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(adPicDir + picUrl);
        return IOUtils.toByteArray(in);
    }
}
