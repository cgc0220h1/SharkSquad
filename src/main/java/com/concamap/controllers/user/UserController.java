package com.concamap.controllers.user;

import com.concamap.component.post.PostComponent;
import com.concamap.model.*;
import com.concamap.services.comment.CommentService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.sql.Timestamp;
import java.io.File;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
public class UserController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final PostComponent postComponent;

    private final CommentService commentService;

    private final UserService userService;

    private final PostService postService;

    private final Environment env;

    @Autowired
    public UserController(UserService userService, PostService postService, Environment env, PostComponent postComponent, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.env = env;
        this.postComponent = postComponent;
        this.commentService = commentService;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D').replace(' ', '-');
    }

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

    @PostMapping("/login")
    public RedirectView login(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/login");
        }
        return new RedirectView("/");
    }

    @GetMapping("/signup")
    public ModelAndView showSignUp() {
        ModelAndView modelAndView = new ModelAndView("user/signup");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

    @PostMapping("/signup")
    public RedirectView signup(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/signup");
        }
        users.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        users.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        userService.save(users);

        return new RedirectView("/login");
    }

    @GetMapping("/users/{username}")
    public ModelAndView showUser(@PathVariable("username") Users user,
                                 @SessionAttribute("categoryList") List<Category> categoryList,
                                 @SessionAttribute("randomPostList") List<Post> randomPosts) {
        ModelAndView modelAndView = new ModelAndView("home/bio");
        modelAndView.addObject("user", user);
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("randomPostList", randomPosts);
        return modelAndView;
    }

    @GetMapping("/users/{username}/profile")
    public ModelAndView showUserProfile(@PathVariable("username") Users user) {
        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/users/**/profile")
    public RedirectView updateUserProfile(@ModelAttribute("user") Users users) {
        Users usersFound = userService.findExistById(users.getId());
        usersFound.setFirstName(users.getFirstName());
        usersFound.setLastName(users.getLastName());
        usersFound.setPhone(users.getPhone());
        usersFound.setEmail(users.getEmail());
        usersFound.setBio(users.getBio());
        userService.save(usersFound);
        return new RedirectView("/users/" + usersFound.getUsername() + "/profile");
    }

    @GetMapping("users/{username}/posts")
    public ModelAndView showPostByUser(@PathVariable("username") String username, Pageable pageable,
                                       @SessionAttribute("recentPostList") List<Post> recentPosts,
                                       @SessionAttribute("randomPostList") List<Post> randomPosts,
                                       @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("post/filter");
        Page<Post> postPage = postService.findAllByUsers_Username(username, pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("category", categoryList);
        modelAndView.addObject("postPage", postPage);
        modelAndView.addObject("user_name", username);
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("linkPage", "/users/" + username + "/posts");
        return modelAndView;
    }

    @GetMapping("/users/{username}/posts/create")
    public ModelAndView showCreateForm(@PathVariable("username") String username, @SessionAttribute("categoryList") List<Category> categoryList) {
        Users user = userService.findActiveUserByUsername(username);
        Post post = new Post();
        post.setUsers(user);
        ModelAndView modelAndView = new ModelAndView("post/create");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/users/**/posts/create")
    public RedirectView savePost(@ModelAttribute("post") Post post) throws IOException {
        Set<Attachment> attachments = new HashSet<>();
        Attachment attachment = new Attachment();
        String username = post.getUsers().getUsername();
        long now = System.currentTimeMillis();

        MultipartFile multipartFile = post.getMultipartFile();
        String fileName = now + "-" + multipartFile.getOriginalFilename();
        String folderUploadPath = env.getProperty("upload.path");
        assert folderUploadPath != null;
        File folderUpload = new File(folderUploadPath, username);
        System.out.println(folderUpload.getAbsolutePath());
        if (!folderUpload.exists()) {
            if (!folderUpload.mkdirs()) {
                throw new IOException();
            }
        }

        File file = new File(folderUpload, fileName);
        FileCopyUtils.copy(multipartFile.getBytes(), file);
        attachment.setImageLink("/" + username + "/" + fileName);
        attachment.setCreatedDate(new Timestamp(now));
        attachment.setUpdatedDate(new Timestamp(now));
        attachment.setStatus(1);
        attachment.setPost(post);

        attachments.add(attachment);
        post.setAttachments(attachments);
        post.setStatus(1);
        post.setCreatedDate(new Timestamp(now));
        post.setUpdatedDate(new Timestamp(now));
        post.setAnchorName(removeAccent(post.getTitle() + " " + (postService.count() + 1)));
        post.setLikes(0);
        postService.save(post);

        return new RedirectView("/users/" + post.getUsers().getUsername() + "/posts/" + post.getAnchorName());
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/edit")
    public ModelAndView showEditForm(@PathVariable("username") String username, @PathVariable("anchor-name") String anchorName, @SessionAttribute("categoryList") List<Category> categoryList) {
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/users/posts/edit")
    public RedirectView updatePost(@ModelAttribute("post") Post post, @SessionAttribute("categoryList") List<Category> categoryList) {
        int postId = post.getId();
        Post postFound = postService.findExistById(postId);
        Set<Attachment> attachments = postFound.getAttachments();
        Attachment attachment = attachments.iterator().next();
        String username = postFound.getUsers().getUsername();
        long now = System.currentTimeMillis();

        try {
            MultipartFile multipartFile = post.getMultipartFile();
            if (Objects.equals(multipartFile.getOriginalFilename(), "")) {
                postFound.setTitle(post.getTitle());
                postFound.setContent(post.getContent());
                postFound.setUpdatedDate(new Timestamp(now));
                postFound.setAnchorName(removeAccent(post.getTitle() + " " + (postService.count() + 1)));
                postService.save(postFound);
            } else {

                String fileName = now + "-" + multipartFile.getOriginalFilename();
                String folderUploadPath = env.getProperty("upload.path");
                assert folderUploadPath != null;
                File folderUpload = new File(folderUploadPath, username);
                if (!folderUpload.exists()) {
                    if (!folderUpload.mkdirs()) {
                        throw new IOException();
                    }
                }

                File file = new File(folderUpload, fileName);
                FileCopyUtils.copy(multipartFile.getBytes(), file);
                attachment.setImageLink("/" + username + "/" + fileName);
                attachment.setUpdatedDate(new Timestamp(now));
                attachment.setStatus(1);

                postFound.setContent(post.getContent());
                postFound.setUpdatedDate(new Timestamp(now));
                postFound.setAnchorName(removeAccent(post.getTitle() + " " + (postService.count() + 1)));
                postService.save(postFound);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RedirectView("/posts/" + postFound.getAnchorName());
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/delete")
    public RedirectView deletePost(@ModelAttribute("post") Post post, @PathVariable("username") String username, @PathVariable("anchor-name") String anchorName) {
        Post post1 = postService.findExistByAnchorName(anchorName);
        post1.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        postService.delete(post1.getId());
        return new RedirectView("/users/" + post1.getUsers().getUsername() + "/posts");
    }

    @GetMapping("/users/{username}/posts/{anchor-name}")
    public ModelAndView showPostDetail(@PathVariable("anchor-name") String anchorName,
                                 @PathVariable("username") String username,
                                 @SessionAttribute("recentPostList") List<Post> recentPosts,
                                 @SessionAttribute("randomPostList") List<Post> randomPosts,
                                 @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("user/postDetail");
        Post postFound = postService.findExistByAnchorName(anchorName);

        List<Comment> allComment = commentService.findAllExistByPost(postFound);

        modelAndView.addObject("post", postFound);
        modelAndView.addObject("allComment", allComment);
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }
}