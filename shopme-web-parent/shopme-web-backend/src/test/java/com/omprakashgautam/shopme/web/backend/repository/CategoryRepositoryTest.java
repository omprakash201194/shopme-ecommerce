package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.category.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@DataJpaTest(showSql = false)
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Electronic","electronics", "default-category.png");
        Category savedCategory = categoryRepository.save(category);

        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0L);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(7L);
        Category subCategory = new Category("iPhone","iphone","default.png",parent);
        Category savedCategory = categoryRepository.save(subCategory);

        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0L);
    }

    @Test
    public void testGetCategory(){
        Category byId = categoryRepository.findById(1L).get();
        System.out.println(byId.getName());
        Set<Category> children = byId.getChildren();
        for (Category child : children) {
            System.out.println(child.getName());
        }
    }

    @Test
    public void testPrintCategoryHierarchy(){
        List<Category> all = categoryRepository.findAll();
        for (Category category : all){
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category category, int subLevel) {
        int newSubLevel = subLevel + 1;
        for (Category subCategory : category.getChildren()) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(System.out::println);
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = categoryRepository.findByName(name).get();

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String alias = "electronics";
        Category category = categoryRepository.findByAlias(alias).get();

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getAlias()).isEqualTo(alias);
    }

}