package com.teamtreehouse.blog;
import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.ImplementBlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import com.teamtreehouse.blog.model.NotFoundException;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        staticFileLocation("/public");
        BlogDao dao = new ImplementBlogDao();

        // Add sample posts with debug output
        System.out.println("Adding sample blog posts...");
        dao.addEntry(new BlogEntry("First Post", "This is my first blog post!  Thank you for reading.  That's all for now, Goodbye!", "java,coding"));
        dao.addEntry(new BlogEntry("Welcome to my Blog", "Thanks for visiting my blog.  This blog will feature posts about my journey to becoming a software engineer.  Stay tuned for more.", "welcome"));
        dao.addEntry(new BlogEntry("Java Programming", "Java is a great language.  It's similar to C++.", "java,programming"));

        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

        // Homepage route with debug
        get("/", (req, res) -> {
            System.out.println("Homepage route hit");
            Map<String, Object> model = new HashMap<>();
            List<BlogEntry> entries = dao.findAllEntries();
            System.out.println("Number of entries: " + entries.size());
            for(BlogEntry entry : entries) {
                System.out.println("Entry: " + entry.getTitle());
            }
            model.put("entries", entries);
            return engine.render(new ModelAndView(model, "index.hbs"));
        });

        // New post route with debug
        get("/admin/new", (req, res) -> {
            System.out.println("New post route hit");
            Map<String, Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model, "entry-form.hbs"));
        });


        // Before filter
        before("/admin/*", (req, res) -> {
            if (req.cookie("admin") == null) {
                res.redirect("/password");
                halt();
            }
        });


        // Blog entry detail
        get("/entry/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                BlogEntry entry = dao.findEntryBySlug(req.params("slug"));
                model.put("entry", entry);
                return engine.render(new ModelAndView(model, "detail.hbs"));
            } catch (NotFoundException e) {
                res.status(404);
                model.put("message", e.getMessage());
                return engine.render(new ModelAndView(model, "404.hbs"));
            }
        });

        // Password page
        get("/password", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return engine.render(new ModelAndView(model, "password.hbs"));
        });

        // Password verification
        post("/password", (req, res) -> {
            if ("admin".equals(req.queryParams("password"))) {
                res.cookie("admin", "true", 3600);
                res.redirect("/admin/new");
                return null;
            }
            res.redirect("/password");
            return null;
        });

        // Create new entry
        post("/admin/new", (req, res) -> {
            String title = req.queryParams("title");
            String content = req.queryParams("entry");
            String tags = req.queryParams("tags");

            BlogEntry entry = new BlogEntry(title, content, tags);
            dao.addEntry(entry);

            res.redirect("/");
            return null;
        });

        // Edit entry form
        get("/admin/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                BlogEntry entry = dao.findEntryBySlug(req.params("slug"));
                model.put("entry", entry);
                return engine.render(new ModelAndView(model, "entry-form.hbs"));
            } catch (NotFoundException e) {
                res.status(404);
                model.put("message", e.getMessage());
                return engine.render(new ModelAndView(model, "404.hbs"));
            }
        });

        // Update entry
        post("/admin/edit/:slug", (req, res) -> {
            try {
                BlogEntry entry = dao.findEntryBySlug(req.params("slug"));
                entry.updateFromForm(
                        req.queryParams("title"),
                        req.queryParams("entry"),
                        req.queryParams("tags")
                );
                dao.updateEntry(entry);
                res.redirect("/entry/" + entry.getSlug());
            } catch (NotFoundException e) {
                res.status(404);
                halt(404);
            }
            return null;
        });

        // Delete entry
        post("/admin/delete/:slug", (req, res) -> {
            try {
                if (dao.deleteEntry(req.params("slug"))) {
                    res.redirect("/");
                } else {
                    throw new NotFoundException("Blog entry", req.params("slug"));
                }
            } catch (NotFoundException e) {
                res.status(404);
                halt(404);
            }
            return null;
        });

        // Add comment
        post("/entry/:slug/comment", (req, res) -> {
            try {
                BlogEntry entry = dao.findEntryBySlug(req.params("slug"));
                String name = req.queryParams("name");
                String commentContent = req.queryParams("comment");

                Comment comment = new Comment(name, commentContent);
                entry.addComment(comment);

                res.redirect("/entry/" + entry.getSlug());
            } catch (NotFoundException e) {
                res.status(404);
                halt(404);
            }
            return null;
        });
    }
}