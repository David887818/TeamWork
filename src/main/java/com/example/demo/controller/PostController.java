package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    private User user;
    private List<Comment> commentList;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostLikeRepository likeRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Value(value = "${TeamWork.post.pic.url}")
    private String adPicDir;

    @PostMapping("/addLike")
    public String addLike(@RequestParam("user_id") int user_id, @RequestParam("post_id") int post_id) {
        PostLike likes = PostLike.builder()
                .post(postRepository.getOne(post_id))
                .user(userRepository.getOne(user_id))
                .build();
        likeRepository.save(likes);
        Notification notification=Notification.builder()
                .notStatus(NotificationStatus.LIKE)
                .from(userRepository.getOne(user_id))
                .to(postRepository.getOne(post_id).getUser())
                .build();
        notificationRepository.save(notification);
        return "redirect:/homePage";
    }

    @PostMapping("/disLike")
    public String disLike(@RequestParam("user_id") int user_id, @RequestParam("post_id") int post_id) {
        List<PostLike> all = likeRepository.findAll();
        for (PostLike like : all) {
            if (like.getUser().getId() == user_id && like.getPost().getId() == post_id) {
                likeRepository.deleteById(like.getId());
            }
        }
        return "redirect:/homePage";
    }


    @PostMapping("/addPost")
    public String addAdvertise(@ModelAttribute Post post, @RequestParam("user_id") int id,
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
        post.setListStatus(ListStatus.FALSE);
        postRepository.save(post);
        return "redirect:/homePage";
    }

    @PostMapping("/addPostFromUserPage")
    public String addAdvertiseFromUserPage(@ModelAttribute Post post, @RequestParam("user_id") int id,
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
        post.setListStatus(ListStatus.FALSE);
        postRepository.save(post);
        return "redirect:/userPage";
    }

    @PostMapping("/addPostFromFriendPage")
    public String addAdvertiseFromFriendPage(@ModelAttribute Post post, @RequestParam("user_id") int id, @RequestParam("friend_id") int f_id,
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
        post.setFriend(userRepository.getOne(f_id));
        post.setPic_url(picName);
        post.setDate(sdf.format(new java.util.Date()));
        post.setListStatus(ListStatus.FALSE);
        postRepository.save(post);
        return "redirect:/friend1Page";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("comment_id") int comment_id) {
        commentRepository.deleteById(comment_id);
        return "redirect:/homePage";
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
        Notification notification = Notification.builder()
                .notStatus(NotificationStatus.COMMENT)
                .from(userRepository.getOne(userId))
                .to(postRepository.getOne(postId).getUser())
                .build();
        notificationRepository.save(notification);
        return "redirect:/homePage";
    }

    @GetMapping(value = "/userImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(adPicDir + picUrl);
        return IOUtils.toByteArray(in);
    }


}
