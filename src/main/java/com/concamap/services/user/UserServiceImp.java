package com.concamap.services.user;

import com.concamap.model.Post;
import com.concamap.model.Roles;
import com.concamap.model.Users;
import com.concamap.repositories.RolesRepository;
import com.concamap.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:config/status.properties")
public class UserServiceImp implements UserService {
    @Value("${user.active}")
    private int activeUser;

    @Value("${user.non-active}")
    private int nonActiveUser;

    @Value("${user.deleted}")
    private int deletedUser;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Users findActiveUserByUsername(String username) {
        return userRepository.findByStatusAndUsername(activeUser, username).orElse(null);
    }

    @Override
    public Roles findExistRolesById(int id) {
        return rolesRepository.findByStatusAndId(1,id).orElse(null);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    @Override
    public Users findById(int id) {
        return userRepository.findById(id).orElse(null);
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
        return userRepository.findAllByStatusIsNot(deletedUser, pageable);
    }

    @Override
    public Users findExistById(int id) {
        return userRepository.findByStatusAndId(activeUser, id).orElse(null);
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
    public Users save(Users users) {
        return userRepository.save(users);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
