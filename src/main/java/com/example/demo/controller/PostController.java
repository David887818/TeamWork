package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
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

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Value("${webSite.pic.url}")
    private String adPicDir;

    @PostMapping("/addPost")
    public String addAdvertise(@ModelAttribute Post post,
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
        post.setPic_url(picName);
        postRepository.save(post);
        return "redirect:/homePage";
    }

    @GetMapping(value = "/adImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("pic_url") String pic_url) throws IOException {
        InputStream in = new FileInputStream(adPicDir + pic_url);
        return IOUtils.toByteArray(in);
    }
}
