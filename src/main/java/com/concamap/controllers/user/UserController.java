package com.concamap.controllers.user;

import com.concamap.model.Attachment;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.security.UserDetailServiceImp;
import com.concamap.services.user.EmailService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.IOException;
import java.sql.Timestamp;
import java.io.File;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Controller
public class UserController {
    private final PostService postService;

    private final Environment env;

    private final UserService userService;
    private final EmailService emailService;

    private final UserDetailServiceImp userDetailServiceImp;

    @ModelAttribute("user")
    public Users user() {
        return userDetailServiceImp.getCurrentUser();
    }

    @Autowired
    public UserController(UserService userService, EmailService emailService, UserDetailServiceImp userDetailServiceImp, PostService postService, Environment env) {
        this.userService = userService;
        this.emailService = emailService;
        this.userDetailServiceImp = userDetailServiceImp;
        this.postService = postService;
        this.env = env;
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
//
//        if (bindingResult.hasFieldErrors()) {
//            modelAndView.setViewName("user/signup");
//        } else {
//
//            users.setStatus(0);
//
//            users.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//            users.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
//            users.setRoles(userService.findExistRolesById(2));
//
//            users.setConfirmationToken(UUID.randomUUID().toString());
//
//            userService.save(users);
//
//            String appUrl = request.getScheme() + "://" + request.getServerName();
//
//            SimpleMailMessage registrationEmail = new SimpleMailMessage();
//            registrationEmail.setTo(users.getEmail());
//            registrationEmail.setSubject("Registration Confirmation");
//            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
//                    + appUrl + ":8080/confirm?token=" + users.getConfirmationToken());
//            registrationEmail.setFrom("sharksquadteam420@gmail.com");
//
//            emailService.sendEmail(registrationEmail);
//
//            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + users.getEmail());
//            modelAndView.setViewName("user/signup");
//        }
//
//
        return modelAndView;
    }

    @GetMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {

//        Users user = userService.findByConfirmationToken(token);
//
//        if (user == null) {
//            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
//        } else {
//            modelAndView.addObject("confirmationToken", user.getConfirmationToken());
//        }
//
//        modelAndView.setViewName("user/confirm");
        return modelAndView;
    }

    @PostMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

//        modelAndView.setViewName("user/confirm");
//
//        Zxcvbn passwordCheck = new Zxcvbn();
//
//        Strength strength = passwordCheck.measure(requestParams.get("password"));
//
//        if (strength.getScore() < 3) {
//            //modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
//            bindingResult.reject("password");
//
//            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
//
//            modelAndView.setViewName("redirect:user/confirm?token=" + requestParams.get("token"));
//            System.out.println(requestParams.get("token"));
//            return modelAndView;
//        }
//
//        Users user = userService.findByConfirmationToken(requestParams.get("token"));
//
//        user.setPassword(requestParams.get("password"));
//
//        user.setStatus(1);
//
//        userService.save(user);
//
//        modelAndView.addObject("successMessage", "Your password has been set!");
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

        return new RedirectView("/posts/" + post.getAnchorName());
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/edit")
    public ModelAndView showEditForm(@PathVariable("username") Users users,
                                     @PathVariable("anchor-name") String anchorName,
                                     @SessionAttribute("categoryList") List<Category> categoryList) {
        Post post = postService.findExistByAnchorNameAndUser(anchorName, users);
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/users/**/posts/edit")
    public RedirectView updatePost(@ModelAttribute("post") Post post) {
        postService.save(post);
        return new RedirectView("/");
    }

    @GetMapping("/users/{username}/posts/{anchor-name}/delete")
    public ModelAndView showDeleteForm(@PathVariable("username") Users users,
                                       @PathVariable("anchor-name") String anchorName) {
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/delete");
        modelAndView.addObject("post", post);
        return modelAndView;
    }

    @PostMapping("/users/**/posts/delete")
    public RedirectView deleteBook(@ModelAttribute("post") Post post) {
        postService.delete(post.getId());
        return new RedirectView("/");
    }
}