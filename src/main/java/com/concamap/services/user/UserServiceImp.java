package com.concamap.services.user;

import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:config/status.properties")
public class UserServiceImp implements UserService {
    @Value("${user.active}")
    private int activeUserStatus;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users findActiveUserByUsername(String username) {
        return userRepository.findByStatusAndUsername(activeUserStatus, username).orElse(null);
    }

    @Override
    public List<Users> findAllExist() {
        return null;
    }

    @Override
    public List<Users> findAllExist(Sort sort) {
        return null;
    }

    @Override
    public Page<Users> findAllExist(Pageable pageable) {
        return null;
    }

    @Override
    public Users findExistById(int id) {
        return null;
    }

    @Override
    public List<Users> findAllDeleted() {
        return null;
    }

    @Override
    public List<Users> findAllDeleted(Sort sort) {
        return null;
    }

    @Override
    public Page<Post> findAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Users findDeletedById(int id) {
        return null;
    }

    @Override
    public Users save(Users model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
