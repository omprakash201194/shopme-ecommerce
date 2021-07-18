package com.omprakashgautam.shopme.web.backend.controller.category;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.omprakashgautam.shopme.web.backend.constants.CategoryConstants.VIEW_ALL_CATEGORIES;

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
}
