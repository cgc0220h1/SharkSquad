package com.concamap.services.category;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.repositories.CategoryRepository;
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
public class CategoryServiceImp implements CategoryService {
    @Value("${entity.exist}")
    private int statusExist;

    @Value("${entity.deleted}")
    private int statusDeleted;


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findByAnchorName(String anchorName) {
        return categoryRepository.findByStatusAndAnchorName(statusExist, anchorName).orElse(null);
    }

    @Override
    public List<Category> findAllExist() {
        List<Category> categoryList = new LinkedList<>();
        for (Category category : categoryRepository.findAllByStatus(statusExist)) {
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public List<Category> findAllExist(Sort sort) {
        List<Category> categoryList = new LinkedList<>();
        for (Category category : categoryRepository.findAllByStatus(statusExist, sort)) {
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public Page<Category> findAllExist(Pageable pageable) {
        return categoryRepository.findAllByStatus(statusExist, pageable);
    }

    @Override
    public Category findExistById(int id) {
        return categoryRepository.findByStatusAndId(statusExist, id).orElse(null);
    }

    @Override
    public List<Category> findAllDeleted() {
        return null;
    }

    @Override
    public List<Category> findAllDeleted(Sort sort) {
        return null;
    }

    @Override
    public Page<Post> findAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Category findDeletedById(int id) {
        return null;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
