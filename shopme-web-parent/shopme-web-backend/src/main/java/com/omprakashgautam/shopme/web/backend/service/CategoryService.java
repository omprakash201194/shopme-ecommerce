package com.omprakashgautam.shopme.web.backend.service;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 9:09 PM.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> listAll() {
        return repository.findAll();
    }

    public Category save(Category category){
        return repository.save(category);
    }

    public List<Category> listCategoryForForm(){
        List<Category> all = repository.findAll();
        List<Category> categoriesForForm = new ArrayList<>();
        for (Category category : all){
            if (category.getParent() == null) {
                categoriesForForm.add(new Category(category.getId(), category.getName()));

                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    String subCategoryName = "--" + subCategory.getName();
                    categoriesForForm.add(new Category(subCategory.getId(), subCategoryName));
                    listChildren(categoriesForForm, subCategory, 1);
                }
            }
        }
        return categoriesForForm;
    }

    private void listChildren(List<Category> categoriesForForm, Category category, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory : category.getChildren()) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            categoriesForForm.add(new Category(subCategory.getId(), name));
            listChildren(categoriesForForm,subCategory, newSubLevel);
        }
    }
}
