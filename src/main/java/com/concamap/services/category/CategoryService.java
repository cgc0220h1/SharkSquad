package com.concamap.services.category;

import com.concamap.model.Category;
import com.concamap.services.GenericService;

public interface CategoryService extends GenericService<Category> {
    Category findByAnchorName(String anchorName);
}
