package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")
public class Post implements Comparable<Post>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "created_date")
    private String date;
    @Column
    private String pic_url;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Comment> comments;
    @OneToMany
    private List<PostLike> likes;
    @Enumerated(EnumType.STRING)
    private ListStatus listStatus;
    @ManyToOne
    private User friend;

        @Override
    public int compareTo(Post o) {
        return date.compareTo(o.date);
    }
}
