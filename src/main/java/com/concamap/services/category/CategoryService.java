package com.concamap.services.category;

import com.concamap.model.Category;
import com.concamap.services.GenericService;

import java.util.List;

public interface CategoryService extends GenericService<Category> {
    List<Category> findAllByStatus(int status);
}
