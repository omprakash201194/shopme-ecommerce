package com.omprakashgautam.shopme.web.backend.controller.category;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.exception.category.CategoryNotFoundException;
import com.omprakashgautam.shopme.web.backend.exception.user.UserNotFoundException;
import com.omprakashgautam.shopme.web.backend.service.CategoryService;
import com.omprakashgautam.shopme.web.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.omprakashgautam.shopme.web.backend.constants.CategoryConstants.*;
import static com.omprakashgautam.shopme.web.backend.constants.UserConstants.REDIRECT_TO_USERS;
import static com.omprakashgautam.shopme.web.backend.constants.UserConstants.VIEW_USERS_FORM;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 9:12 PM.
 */
@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(Model model){
        List<Category> categories = service.listAll();
        model.addAttribute("listCategories", categories);
        return VIEW_ALL_CATEGORIES;
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> listCategories = service.listCategoryForForm();
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle","Create New Category");
        model.addAttribute("listCategories",listCategories);
        return VIEW_CATEGORY_FORM;
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category save = service.save(category);
            String uploadDir = "./category-images/" + save.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(category);
        }

        redirectAttributes.addFlashAttribute("message","The category has been saved successfully.");
        return REDIRECT_TO_CATEGORIES;
    }


    @GetMapping("/categories/edit/{id}")
    public String editCateogry(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try {
            Category category = service.get(id);
            List<Category> listCategories = service.listCategoryForForm();

            model.addAttribute("category",category);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Category ("+category.getName()+")");
            return VIEW_CATEGORY_FORM;
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return REDIRECT_TO_CATEGORIES;
        }
    }
}
