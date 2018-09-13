package com.example.demo.model;

import java.util.Comparator;

public class PostComparator implements Comparator<Post> {
    
    @Override
    public int compare(Post o1, Post o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

}
