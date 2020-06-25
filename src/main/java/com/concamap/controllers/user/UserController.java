package com.concamap.controllers.user;

import com.concamap.component.file.FileComponent;
import com.concamap.component.post.PostComponent;
import com.concamap.model.Attachment;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.sql.Timestamp;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PropertySource({"classpath:config/homepage.properties", "classpath:config/post.properties"})
public class UserController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    @Value("${cover.image.default-source}")
    private String imageLink;

    private final PostComponent postComponent;

    private final UserService userService;

    private final PostService postService;


    private final FileComponent fileComponent;

    @Autowired
    public UserController(UserService userService, PostService postService, PostComponent postComponent, FileComponent fileComponent) {
        this.userService = userService;
        this.postService = postService;
        this.postComponent = postComponent;
        this.fileComponent = fileComponent;
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
    public ModelAndView showPostByUser(@PathVariable("username") Users users, Pageable pageable,
                                       @SessionAttribute("recentPostList") List<Post> recentPosts,
                                       @SessionAttribute("randomPostList") List<Post> randomPosts,
                                       @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("post/filter");
        Page<Post> postPage = postService.findAllByUsers(users, pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("postPage", postPage);
        modelAndView.addObject("message", users.getUsername());
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("linkPage", "/users/" + users.getUsername() + "/posts");
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
        Users users = post.getUsers();
        long now = System.currentTimeMillis();

        MultipartFile multipartFile = post.getMultipartFile();
        if (!multipartFile.isEmpty()) {
            File uploadedFile = fileComponent.copyFile(multipartFile, users);
            imageLink = fileComponent.getImageLink(uploadedFile);
        }
        attachment.setImageLink(imageLink);
        attachment.setCreatedDate(new Timestamp(now));
        attachment.setUpdatedDate(new Timestamp(now));
        attachment.setPost(post);
        attachments.add(attachment);

        post.setAttachments(attachments);
        post.setCreatedDate(new Timestamp(now));
        post.setUpdatedDate(new Timestamp(now));
        post.setAnchorName(postComponent.toAnchorName(post.getTitle()));
        postService.save(post);

        return new RedirectView("/posts/" + post.getAnchorName());
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/edit")
    public ModelAndView showEditForm(@PathVariable("username") Users users, @PathVariable("anchor-name") String anchorName, @SessionAttribute("categoryList") List<Category> categoryList) {
        Post post = postService.findExistByAnchorNameAndUser(anchorName, users);
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/users/**/posts/**/edit")
    public RedirectView updatePost(@ModelAttribute("post") Post post, @SessionAttribute("categoryList") List<Category> categoryList) throws IOException {
        int postId = post.getId();
        Post postFound = postService.findExistById(postId);
        Set<Attachment> attachments = postFound.getAttachments();
        Attachment attachment = attachments.iterator().next();
        Users users = postFound.getUsers();
        long now = System.currentTimeMillis();

        MultipartFile multipartFile = post.getMultipartFile();
        if (!multipartFile.isEmpty()) {
            File uploadedFile = fileComponent.copyFile(multipartFile, users);
            imageLink = fileComponent.getImageLink(uploadedFile);
            attachment.setImageLink(imageLink);
            attachment.setUpdatedDate(new Timestamp(now));
        }

        postFound.setTitle(post.getTitle());
        postFound.setContent(post.getContent());
        postFound.setUpdatedDate(new Timestamp(now));
        postFound.setAnchorName(postComponent.toAnchorName(post.getTitle()));
        postService.save(postFound);

        return new RedirectView("/posts/" + postFound.getAnchorName());
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/delete")
    public RedirectView deletePost(@PathVariable("username") Users users, @PathVariable("anchor-name") String anchorName) {
        Post postFound = postService.findExistByAnchorNameAndUser(anchorName, users);
        postFound.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        postService.delete(postFound.getId());
        return new RedirectView("/users/" + postFound.getUsers().getUsername() + "/posts");
    }
}