package com.omprakashgautam.shopme.web.backend.service;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.exception.category.CategoryNotFoundException;
import com.omprakashgautam.shopme.web.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 9:09 PM.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> listAll() {
        List<Category> rootCategories = repository.findRootCategories(Sort.by("name").ascending());
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category category: rootCategories){
            hierarchicalCategories.add(Category.copyFull(category));
            Set<Category> children = sortSubCategory(category.getChildren());
            for (Category subCategory : children) {
                String subCategoryName = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, subCategoryName));
                listSubHierarchical(hierarchicalCategories, subCategory, 1);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchical(List<Category> hierarchicalCategories, Category category, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory : sortSubCategory(category.getChildren())) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            listSubHierarchical(hierarchicalCategories,subCategory, newSubLevel);
        }
    }

    public Category save(Category category){
        return repository.save(category);
    }

    public List<Category> listCategoryForForm(){
        List<Category> all = repository.findAll(Sort.by("name").ascending());
        List<Category> categoriesForForm = new ArrayList<>();
        for (Category category : all){
            if (category.getParent() == null) {
                categoriesForForm.add(new Category(category.getId(), category.getName()));

                Set<Category> children = sortSubCategory(category.getChildren());
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
        for (Category subCategory : sortSubCategory(category.getChildren())) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            categoriesForForm.add(new Category(subCategory.getId(), name));
            listChildren(categoriesForForm,subCategory, newSubLevel);
        }
    }

    public Category get(Long id) throws CategoryNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find any category with id " + id));
    }

    public String checkUnique(Long id, String name, String alias) {
        boolean isNewCategory = (id == null || id == 0);
        Optional<Category> byName = repository.findByName(name);
        if (isNewCategory) {
            if (byName.isPresent()) {
                return "DuplicateName";
            }
            else {
                Optional<Category> byAlias = repository.findByAlias(alias);
                if (byAlias.isPresent()) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (byName.isPresent() && !byName.get().getId().equals(id)) {
                return "DuplicateName";
            }
            Optional<Category> byAlias = repository.findByAlias(alias);
            if (byAlias.isPresent() && !byAlias.get().getId().equals(id)) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategory(Set<Category> subCategories) {
        TreeSet<Category> categories = new TreeSet<>(Comparator.comparing(Category::getName));
        categories.addAll(subCategories);
        return categories;
    }
}
