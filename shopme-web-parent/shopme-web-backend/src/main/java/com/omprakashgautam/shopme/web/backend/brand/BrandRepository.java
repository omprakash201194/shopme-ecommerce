package com.omprakashgautam.shopme.web.backend.brand;

import com.omprakashgautam.shopme.commons.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gautam
 * Created on 28-Jul-21 at 4:20 PM.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
