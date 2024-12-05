package com.teamtreehouse.blog.model;

import com.teamtreehouse.blog.dao.BlogDao;

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
