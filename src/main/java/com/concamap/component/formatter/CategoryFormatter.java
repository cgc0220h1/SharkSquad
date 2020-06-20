package com.concamap.component.formatter;

import com.concamap.model.Category;
import com.concamap.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CategoryFormatter implements Formatter<Category> {
    private final CategoryService categoryService;

    @Autowired
    public CategoryFormatter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Category parse(String text, Locale locale) {
        return categoryService.findByAnchorName(text);
    }

    @Override
    public String print(Category object, Locale locale) {
        return null;
    }
}
