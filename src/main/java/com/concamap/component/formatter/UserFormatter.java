package com.concamap.component.formatter;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class UserFormatter implements Formatter<Users> {
    private final UserService userService;

    @Autowired
    public UserFormatter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Users parse(String username, Locale locale) {
        return userService.findActiveUserByUsername(username);
    }

    @Override
    public String print(Users object, Locale locale) {
        return null;
    }
}
