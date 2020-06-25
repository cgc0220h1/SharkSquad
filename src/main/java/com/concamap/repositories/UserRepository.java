package com.concamap.repositories;

import com.concamap.model.Users;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Users, Integer> {
    Optional<Users> findByStatusAndUsername(int status, @NotNull(message = "username cannot be empty") @Size(min = 5, max = 30) @Pattern(regexp = "^[a-z0-9_-]{3,16}$") String username);

    Optional<Users> findByStatusAndId(int status, int id);

    Users findByEmail(String email);

    Users findByConfirmationToken(String confirmationToken);
}
