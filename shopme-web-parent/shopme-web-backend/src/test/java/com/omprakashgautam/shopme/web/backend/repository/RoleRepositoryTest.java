package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateAdminRole(){
        Role roleAdmin = new Role("Admin", "Manage Everything");
        Role savedRole = repo.save(roleAdmin);
        Assertions.assertThat(savedRole.getId()).isGreaterThan(0L);
    }

    @Test
    public void testCreateOtherRoles(){
        Role salesRole = new Role("Salesperson", "Manage product, price, customer, shipping" +
                "orders and sales report");

        Role editorRole = new Role("Editor", "Manage categories, brands, products, " +
                "articles and menus");

        Role shipperRole = new Role("Shipper", "View products, view orders and update order status");

        Role assistantRole = new Role("Assistant", "Manage questions and reviews");

        repo.saveAll(List.of(salesRole, editorRole, shipperRole, assistantRole));
    }
}