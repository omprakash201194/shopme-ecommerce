package com.omprakashgautam.shopme.web.backend.user;

import com.omprakashgautam.shopme.commons.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 5:03 PM.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
