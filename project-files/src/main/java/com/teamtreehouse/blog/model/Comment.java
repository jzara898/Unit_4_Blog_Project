package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.*;
import java.util.Date;

public class Comment {
    private String author;
    private String content;
    private Date createdAt;

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = new Date();
    }

    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public Date getCreatedAt() { return createdAt; }
}
