package com.concamap.services.category;

import com.concamap.model.Category;
import com.concamap.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class CategoryServiceImpTest {
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class); //Làm giả tầng Repo

    @BeforeEach
    public void init() {
        Category category = new Category();
        category.setTitle("Dân Tổ");
        doReturn(Optional.of(category)).when(categoryRepository).findById(1);

        Iterable<Category> categoryList = Collections.singletonList(category);
        doReturn(categoryList).when(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Kiểm tra repo trả về Category với id là 1")
    public void whenFindById1_thenReturnCategory() {
        String name = "Dân Tổ";
        Optional<Category> category = categoryRepository.findById(1);
        assertEquals(name, category.get().getTitle());
    }
}