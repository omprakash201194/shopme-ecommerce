package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Brand;
import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.brand.BrandRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest(showSql = false)
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandRepositoryTest {

    public static final String LOGO = "brand-logo.png";
    @Autowired
    private BrandRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateBrand() {
        Brand apple = new Brand("Apple", LOGO);
        Brand savedBrand = repository.save(apple);

        Assertions.assertThat(savedBrand.getId()).isGreaterThan(0L);
    }

    @Test
    public void testCreateMoreBrands() {
        Brand microsoft = new Brand("Microsoft", LOGO);
        Brand ibm = new Brand("IBM", LOGO);
        repository.saveAll(List.of(microsoft, ibm));
    }

    @Test
    public void testBrandCategories() {
        Category computers = testEntityManager.find(Category.class, 3L);
        Category desktops = testEntityManager.find(Category.class, 5L);
        Brand dell = new Brand("Dell", LOGO);
        Set<Category> categories = Set.of(computers, desktops);
        dell.setCategories(categories);

        Brand savedBrand = repository.save(dell);
        Assertions.assertThat(savedBrand.getId()).isGreaterThan(0L);
        Assertions.assertThat(savedBrand.getCategories().size()).isEqualTo(categories.size());
    }

    @Test
    public void testGetBrands() {
        List<Brand> all = repository.findAll();
        Assertions.assertThat(all.size()).isGreaterThan(0);
        all.forEach(System.out::println);
    }

    @Test
    public void testGetBrandById() {
        Optional<Brand> byId = repository.findById(4L);
        Assertions.assertThat(byId).isNotNull();
        System.out.println(byId.get());
    }

    @Test
    public void testUpdateBrandById() {
        Category computers = testEntityManager.find(Category.class, 3L);
        Optional<Brand> byId = repository.findById(1L);
        byId.ifPresent(brand -> brand.getCategories().add(computers));

        Brand s = byId.get();
        Brand saved = repository.save(s);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getCategories().contains(computers)).isTrue();
        System.out.println(saved);
    }

    @Test
    public void tesDeleteBrandById() {
        Optional<Brand> byId = repository.findById(2L);
        if (byId.isPresent()) {
            repository.deleteById(2L);
        }
        List<Brand> all = repository.findAll();
        Assertions.assertThat(all.contains(byId.get())).isFalse();
    }
}