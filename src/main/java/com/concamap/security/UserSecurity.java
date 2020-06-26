package com.concamap.security;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {
    private final UserService userService;

    @Autowired
    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean checkUsername(Authentication authentication, String username) {
        Users usersLogin = userService.findActiveUserByUsername(authentication.getName());
        return usersLogin != null && usersLogin.getUsername().equals(username);
    }
}