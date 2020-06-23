package com.concamap.services.user;

import com.concamap.model.Roles;
import com.concamap.model.Users;
import com.concamap.services.GenericService;

import java.util.Optional;

public interface UserService extends GenericService<Users> {
    Users findActiveUserByUsername(String username);

    Roles findExistRolesById(int id);
}
