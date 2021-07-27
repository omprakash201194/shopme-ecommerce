package com.omprakashgautam.shopme.web.backend.category;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.reports.IExporter;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.omprakashgautam.shopme.web.backend.category.CategoryConstants.*;
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
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = ASC;
        }
        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> categories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
        String reverseSortDir = sortDir.equalsIgnoreCase(ASC) ? DESC : ASC;

        long startCount = (long) (pageNum - 1) * PAGE_SIZE + 1;
        long totalElements = pageInfo.getTotalElements();
        if (startCount > totalElements) {
            startCount = totalElements;
        }
        long endCount = startCount + PAGE_SIZE - 1;
        if (endCount > totalElements) {
            endCount = totalElements;
        }

        model.addAttribute("listCategories", categories);
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("items", categories.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
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

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            String categoryDir = "/categories-images/" + id;
            FileUploadUtil.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message", "The category with id [" + id + "] has been deleted successfully");
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return REDIRECT_TO_CATEGORIES;
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> categories = service.listCategoryForForm();
        IExporter<Category> exporter = new CategoryCSVExporter();
        exporter.export(categories, response);
    }
}
