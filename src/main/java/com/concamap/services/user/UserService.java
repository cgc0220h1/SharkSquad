package com.concamap.services.user;

import com.concamap.model.Roles;
import com.concamap.model.Users;
import com.concamap.services.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService extends GenericService<Users> {
    Users findActiveUserByUsername(String username);

    Roles findExistRolesById(int id);

    Users findByEmail(String email);

    Users findByConfirmationToken(String confirmationToken);

    Users findById(int id);

    @Override
    Page<Users> findAllExist(Pageable pageable);
}
