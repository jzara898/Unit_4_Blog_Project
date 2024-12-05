package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.List;

public class ImplementBlogDao implements BlogDao {
    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return false;
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return List.of();
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return null;
    }
}
