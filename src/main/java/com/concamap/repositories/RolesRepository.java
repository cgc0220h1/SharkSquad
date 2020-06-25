package com.concamap.repositories;

import com.concamap.model.Roles;
import com.concamap.model.Users;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public interface RolesRepository extends PagingAndSortingRepository<Roles, Integer> {
    Optional<Roles> findByStatusAndId(int status, int id);
}