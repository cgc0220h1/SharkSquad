package com.concamap.controllers.user;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Attachment;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
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

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
public class UserController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final UserService userService;

    private final PostService postService;

    private final PostComponent postComponent;

    @Autowired
    Environment env;

    @Autowired
    public UserController(UserService userService, PostService postService, PostComponent postComponent) {
        this.userService = userService;
        this.postService = postService;
        this.postComponent = postComponent;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D').replace(' ', '-');
    }

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

  /*  @PostMapping("/login")
    public RedirectView login(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/login");
        }
        return new RedirectView("/");
    }*/

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
                                       @SessionAttribute("categoryList") List<Category> categoryList){
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

    @PostMapping("/users/posts/create")
    public RedirectView savePost(@ModelAttribute("post") Post post) {
        try {
            setAttachmentsForPost(post);
            post.setStatus(1);
            post.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            post.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            post.setAnchorName(removeAccent(post.getTitle() +" "+ (postService.count() + 1)));
            post.setLikes(0);

            postService.save(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RedirectView("/posts/" + post.getAnchorName());
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
        try {
            setAttachmentsForPost(post);
            post.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            post.setStatus(1);
            post.setAnchorName(removeAccent(post.getTitle() +" "+ (postService.count() + 1)));

            postService.save(post);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new RedirectView("/posts/" + post.getAnchorName());
    }

    private void setAttachmentsForPost(@ModelAttribute("post") Post post) throws IOException {
        MultipartFile multipartFile;
        String fileName;
        String fileUpload;
        File file;
        multipartFile = post.getMultipartFile();
        fileName = multipartFile.getOriginalFilename();
        fileUpload = env.getProperty("upload.path").toString();
        file = new File(fileUpload, fileName);
        FileCopyUtils.copy(multipartFile.getBytes(), file);

        Set<Attachment> attachments = new HashSet<>();
        Attachment attachment = new Attachment();

        attachment.setImageLink("/uploadFile/" +fileName);
        attachment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        attachment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        attachment.setStatus(1);
        attachment.setPost(post);
        attachments.add(attachment);

        post.setAttachments(attachments);
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/delete")
    public ModelAndView showDeleteForm(@PathVariable("username") String username, @PathVariable("anchor-name") String anchorName, @SessionAttribute("categoryList") List<Category> categoryList) {
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/delete");
        modelAndView.addObject("post", post);
        modelAndView.addObject("anchorName", anchorName);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @GetMapping("/users/posts/{anchor-name}/delete")
    public RedirectView deletePost(@ModelAttribute("post") Post post, @PathVariable("anchor-name") String anchorName) {
        Post post1 = postService.findExistByAnchorName(anchorName);
        post1.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        postService.delete(post1.getId());
        return new RedirectView("/");
    }
}