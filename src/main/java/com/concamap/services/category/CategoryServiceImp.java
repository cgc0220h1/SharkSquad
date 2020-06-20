package com.concamap.services.category;

import com.concamap.model.Category;
import com.concamap.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllByStatus(int status) {
        List<Category> categoryList = new LinkedList<>();
        Sort sort = Sort.by("title").ascending();
        for (Category category : categoryRepository.findAllByStatusIs(status, sort)) {
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public Page<Category> findAllByStatus(int status, Pageable pageable) {
        return null;
    }

    @Override
    public Category findByIdAndStatus(int id, int status) {
        return null;
    }

    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
