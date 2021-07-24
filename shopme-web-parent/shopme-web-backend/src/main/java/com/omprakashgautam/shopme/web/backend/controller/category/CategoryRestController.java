package com.omprakashgautam.shopme.web.backend.controller.category;

import com.omprakashgautam.shopme.web.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author omprakash gautam
 * Created on 13-Jul-21 at 4:41 PM.
 */
@RestController
public class CategoryRestController {
    @Autowired
    private CategoryService service;

    @PostMapping("/categories/check-unique")
    public String checkUniqueCategory(@Param("id") Long id, @Param("name") String name, @Param("alias") String alias){
        return service.checkUnique(id, name, alias);
    }
}
