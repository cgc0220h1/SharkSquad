package com.concamap.controllers.user;

import com.concamap.model.Attachment;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.security.UserDetailServiceImp;
import com.concamap.services.user.EmailService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
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
import java.text.Normalizer;
import java.util.*;
import java.sql.Timestamp;
import java.util.regex.Pattern;

@Controller
@PropertySource("classpath:config/status.properties")
public class UserController {
    private final UserService userService;
    private EmailService emailService;

    private final UserDetailServiceImp userDetailServiceImp;

    @Value("1")
    private int activeStatus;

    @Value("2")
    private int nonActiveStatus;

    @Value("0")
    private int deletedStatus;

    @Autowired
    public UserController(UserService userService, EmailService emailService, UserDetailServiceImp userDetailServiceImp, PostService postService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userDetailServiceImp = userDetailServiceImp;
        this.postService = postService;
    }

    private final PostService postService;

    @Autowired
    Environment env;

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

//        Users userExists = userService.findByEmail(users.getEmail());
//
//        if (userExists != null) {
//            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
//            modelAndView.setViewName("user/signup");
//            bindingResult.reject("email");
//        }

        if (bindingResult.hasFieldErrors()) {
            modelAndView.setViewName("user/signup");
        } else {

            users.setStatus(nonActiveStatus);

            users.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            users.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            users.setRoles(userService.findExistRolesById(2));

            users.setConfirmationToken(UUID.randomUUID().toString());

            userService.save(users);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(users.getEmail());
            registrationEmail.setSubject("REGISTRATION CONFIRMATION");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + ":8888/confirm?token=" + users.getConfirmationToken());
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

        Users user = userService.findByConfirmationToken(requestParams.get("confirmationToken"));

       if (user.getPassword().equals(requestParams.get("password"))) {
           modelAndView.addObject("successMessage", "Your account and password has been confirm!");
           user.setStatus(activeStatus);
           userService.save(user);
       } else {
           modelAndView.addObject("errorMessage", "Your password is wrong");

           String token = user.getConfirmationToken();
           modelAndView.addObject("confirmationToken", token);
       }

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
    public ModelAndView showDeleteForm(@PathVariable("username") String username, @PathVariable("anchor-name") String anchorName) {
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/delete");
        modelAndView.addObject("post", post);
        return modelAndView;
    }

    @PostMapping("/users/{id}/posts/delete")
    public RedirectView deleteBook(@ModelAttribute("post") Post post) {
        postService.delete(post.getId());
        return new RedirectView("/");
    }
}