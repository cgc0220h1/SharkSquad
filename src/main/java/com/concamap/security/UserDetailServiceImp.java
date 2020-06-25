package com.concamap.security;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailServiceImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userService.findActiveUserByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException("không tìm thấy người dùng");
        }

        Set<GrantedAuthority> authorities =new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(users.getRoles().getName()));

        return new User(
          users.getUsername(),
          users.getPassword(),
          authorities
        );
    }
}
