package com.concamap.config.security;

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
        Users userslogin = userService.findActiveUserByUsername(authentication.getName());
        return userslogin != null && userslogin.getUsername().equals(username);
    }
}
