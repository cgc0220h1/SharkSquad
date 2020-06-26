package com.concamap.controllers.user;

import com.concamap.component.file.FileComponent;
import com.concamap.component.post.PostComponent;
import com.concamap.model.Attachment;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.security.UserDetailServiceImp;
import com.concamap.services.user.EmailService;
import com.concamap.model.*;
import com.concamap.services.comment.CommentService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
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

    private final CommentService commentService;

    private final UserService userService;

    private final EmailService emailService;

    private final UserDetailServiceImp userDetailServiceImp;

    private final PostService postService;


    private final FileComponent fileComponent;

    @Autowired
    public UserController(UserService userService, PostService postService, PostComponent postComponent, FileComponent fileComponent, EmailService emailService, UserDetailServiceImp userDetailServiceImp, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.postComponent = postComponent;
        this.commentService = commentService;
        this.fileComponent = fileComponent;
        this.emailService = emailService;
        this.userDetailServiceImp = userDetailServiceImp;
    }

    @ModelAttribute("user")
    public Users user() {
        return userDetailServiceImp.getCurrentUser();
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

    @GetMapping("/logout")
    public RedirectView logout() {
        return new RedirectView("/");
    }

    @GetMapping("/403")
    public String findNotFound() {
        return "error/error403";
    }

    @GetMapping("/signup")
    public ModelAndView showSignUp(Users users) {
        ModelAndView modelAndView = new ModelAndView("user/signup");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(ModelAndView modelAndView, @Validated @ModelAttribute("users") Users users, BindingResult bindingResult, HttpServletRequest request) {

        Users userExists = userService.findByEmail(users.getEmail());

        if (userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("user/signup");
            bindingResult.reject("email");
        }

        if (bindingResult.hasFieldErrors()) {
            modelAndView.setViewName("user/signup");
        } else {

            users.setStatus(0);

            users.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            users.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            users.setRoles(userService.findExistRolesById(2));

            users.setConfirmationToken(UUID.randomUUID().toString());

            userService.save(users);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(users.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + ":8080/confirm?token=" + users.getConfirmationToken());
            registrationEmail.setFrom("sharksquadteam420@gmail.com");

            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + users.getEmail());
            modelAndView.setViewName("user/signup");
        }


        return modelAndView;
    }

    @GetMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {

        Users user = userService.findByConfirmationToken(token);

        if (user == null) {
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else {
            modelAndView.addObject("confirmationToken", user.getConfirmationToken());
        }

        modelAndView.setViewName("user/confirm");
        return modelAndView;
    }

    @PostMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

        modelAndView.setViewName("user/confirm");

        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(requestParams.get("password"));

        if (strength.getScore() < 3) {
            //modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
            bindingResult.reject("password");

            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

            modelAndView.setViewName("redirect:user/confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }

        Users user = userService.findByConfirmationToken(requestParams.get("token"));

        user.setPassword(requestParams.get("password"));

        user.setStatus(1);

        userService.save(user);

        modelAndView.addObject("successMessage", "Your password has been set!");
        return modelAndView;
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

        return new RedirectView("/users/" + post.getUsers().getUsername() + "/posts/" + post.getAnchorName());
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