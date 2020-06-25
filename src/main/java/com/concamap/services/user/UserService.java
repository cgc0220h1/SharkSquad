package com.concamap.services.user;

import com.concamap.model.Users;
import com.concamap.services.GenericService;

public interface UserService extends GenericService<Users> {
    Users findActiveUserByUsername(String username);

}
