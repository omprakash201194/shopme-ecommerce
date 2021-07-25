package com.omprakashgautam.shopme.web.backend.controller.category;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.exception.category.CategoryNotFoundException;
import com.omprakashgautam.shopme.web.backend.service.CategoryService;
import com.omprakashgautam.shopme.web.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import static com.omprakashgautam.shopme.web.backend.constants.CommonConstants.*;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 9:12 PM.
 */
@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model){
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = ASC;
        }
        List<Category> categories = service.listAll(sortDir);
        model.addAttribute("listCategories", categories);
        String reverseSortDir = sortDir.equalsIgnoreCase(ASC) ? DESC : ASC;
        model.addAttribute("reverseSortDir", reverseSortDir);
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

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (" + category.getName() + ")");
            return VIEW_CATEGORY_FORM;
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_TO_CATEGORIES;
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryStatus(@PathVariable("id") Long id, @PathVariable("status") boolean status
            , RedirectAttributes redirectAttributes) {
        service.updateCategoryStatus(id, status);
        String statusMessage = status ? ENABLED : DISABLED;
        redirectAttributes.addFlashAttribute("message", "The category with id [" + id + "] has been " + statusMessage + " successfully");
        return REDIRECT_TO_CATEGORIES;
    }
}
