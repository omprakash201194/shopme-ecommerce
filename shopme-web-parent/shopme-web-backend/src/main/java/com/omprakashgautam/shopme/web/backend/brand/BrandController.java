package com.omprakashgautam.shopme.web.backend.brand;

import com.omprakashgautam.shopme.commons.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.omprakashgautam.shopme.web.backend.brand.BrandConstants.VIEW_ALL_BRANDS;

/**
 * @author gautam
 * Created on 28-Jul-21 at 6:30 PM.
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping("/brands")
    public String listFirstPage(Model model) {
        List<Brand> listBrands = service.listAll();
        model.addAttribute("listBrands", listBrands);
        return VIEW_ALL_BRANDS;
    }
}
