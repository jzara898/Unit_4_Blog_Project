package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import com.teamtreehouse.blog.model.NotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ImplementBlogDao implements BlogDao {
    private List<BlogEntry> entries = new ArrayList<>();

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return entries.add(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return entries.stream()
                .filter(entry -> entry.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Blog entry", slug));
    }

    @Override
    public boolean deleteEntry(String slug) {
        BlogEntry entry = findEntryBySlug(slug);
        return entries.remove(entry);
    }

    @Override
    public boolean updateEntry(BlogEntry updatedEntry) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSlug().equals(updatedEntry.getSlug())) {
                entries.set(i, updatedEntry);
                return true;
            }
        }
        throw new NotFoundException("Blog entry", updatedEntry.getSlug());
    }
}
