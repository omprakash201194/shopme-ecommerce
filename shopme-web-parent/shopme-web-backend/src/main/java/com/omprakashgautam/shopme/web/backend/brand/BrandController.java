package com.omprakashgautam.shopme.web.backend.brand;

import com.omprakashgautam.shopme.commons.entity.Brand;
import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.category.CategoryService;
import com.omprakashgautam.shopme.web.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.omprakashgautam.shopme.web.backend.brand.BrandConstants.*;

/**
 * @author gautam
 * Created on 28-Jul-21 at 6:30 PM.
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService service;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage(Model model) {
        List<Brand> listBrands = service.listAll();
        model.addAttribute("listBrands", listBrands);
        return VIEW_ALL_BRANDS;
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        List<Category> listCategories = categoryService.listCategoryForForm();
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");
        model.addAttribute("listCategories", listCategories);
        return VIEW_BRANDS_FORM;
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand save = service.save(brand);
            String uploadDir = "./brand-logos/" + save.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
        return REDIRECT_TO_BRANDS;
    }
}
