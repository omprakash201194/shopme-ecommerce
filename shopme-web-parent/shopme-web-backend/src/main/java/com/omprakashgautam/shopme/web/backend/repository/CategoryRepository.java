package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 5:57 PM.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
    List<Category> findRootCategories(Sort sort);

    Optional<Category> findByName(String name);

    Optional<Category> findByAlias(String alias);

    @Query("UPDATE Category c set c.enabled = ?2 where c.id=?1")
    @Modifying
    void updateEnabledStatusByid(Long id, boolean status);
}
