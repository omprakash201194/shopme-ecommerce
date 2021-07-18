package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 5:57 PM.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
