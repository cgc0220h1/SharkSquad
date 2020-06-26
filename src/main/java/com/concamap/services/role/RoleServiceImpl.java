package com.concamap.services.role;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Roles;
import com.concamap.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
@Service
@PropertySource("classpath:config/status.properties")
public class RoleServiceImpl implements RoleService {

    @Value("${user.active}")
    private int activeStatus;

    @Value("2")
    private int nonActiveStatus;

    @Value("${user.deleted}")
    private int deletedStatus;

    private RolesRepository rolesRepository;

    @Autowired
    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public List<Roles> findAllExist() {
        List<Roles> rolesList = new LinkedList<>();
        for (Roles roles : rolesRepository.findAllByStatus(activeStatus)) {
            rolesList.add(roles);
        }
        return rolesList;
    }

    @Override
    public List<Roles> findAllExist(Sort sort) {
        return null;
    }

    @Override
    public Page<Roles> findAllExist(Pageable pageable) {
        return null;
    }

    @Override
    public Roles findExistById(int id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Roles> findAllDeleted() {
        return null;
    }

    @Override
    public List<Roles> findAllDeleted(Sort sort) {
        return null;
    }

    @Override
    public Page<Post> findAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Roles findDeletedById(int id) {
        return null;
    }

    @Override
    public Roles save(Roles model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
