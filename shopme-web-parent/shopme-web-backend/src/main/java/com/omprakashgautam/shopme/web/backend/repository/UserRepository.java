package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 7:06 PM.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> getUserByEmail(String email);

    public Long countById(Long id);

    @Query("UPDATE User u set u.enabled = ?2 where u.id=?1")
    @Modifying
    public void updateEnabledStatusByid(Long id, boolean enabled);
}
