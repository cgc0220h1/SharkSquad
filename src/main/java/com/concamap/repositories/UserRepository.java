package com.concamap.repositories;

import com.concamap.model.Users;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<Users, Integer> {
    Optional<Users> findByStatusAndUsername(int status, @NotNull(message = "username cannot be empty") @Size(min = 5, max = 30) @Pattern(regexp = "^[a-z0-9_-]{3,16}$") String username);
}
