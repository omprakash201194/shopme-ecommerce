package com.omprakashgautam.shopme.web.backend.brand;

import com.omprakashgautam.shopme.commons.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gautam
 * Created on 28-Jul-21 at 6:29 PM.
 */
@Service
public class BrandService {
    @Autowired
    private BrandRepository repository;

    public List<Brand> listAll() {
        return repository.findAll();
    }

    public Brand save(Brand brand) {
        return repository.save(brand);
    }
}
