package com.teamtreehouse.blog.model;
import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.*;


public class BlogEntry {
    private String title;
    private String content;
    private Date createdAt;
    private String slug;
    private List<String> tags;
    private List<Comment> comments;
    private int id;
    private static int nextId = 1;

    public BlogEntry(String title, String content, String tagString) {
        this.id = nextId++;
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();

        // Process tags
        if (tagString != null && !tagString.trim().isEmpty()) {
            String[] tagArray = tagString.split(",");
            for (String tag : tagArray) {
                tags.add(tag.trim());
            }
        }

        // Create slug
        try {
            Slugify slugify = new Slugify();
            this.slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
            this.slug = id + "-" + title.toLowerCase().replace(' ', '-');
        }
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreatedAt() { return createdAt; }
    public String getSlug() { return slug; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<Comment> getComments() { return comments; }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public void updateFromForm(String title, String content, String tagString) {
        this.title = title;
        this.content = content;
        this.tags.clear();
        if (tagString != null && !tagString.trim().isEmpty()) {
            String[] tagArray = tagString.split(",");
            for (String tag : tagArray) {
                tags.add(tag.trim());
            }
        }
    }
}
