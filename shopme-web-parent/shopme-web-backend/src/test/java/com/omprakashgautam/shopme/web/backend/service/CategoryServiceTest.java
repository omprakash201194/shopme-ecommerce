package com.omprakashgautam.shopme.web.backend.service;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @MockBean
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        Long id = null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.of(category));
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.empty());

        String result = service.checkUnique(id, name, alias);

        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Long id = null;
        String name = "abc";
        String alias = "computers";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.empty());
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.of(category));

        String result = service.checkUnique(id, name, alias);

        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk() {
        Long id = null;
        String name = "abc";
        String alias = "computers";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.empty());
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.empty());

        String result = service.checkUnique(id, name, alias);

        Assertions.assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Long id = 1L;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(2L, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.of(category));
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.empty());

        String result = service.checkUnique(id, name, alias);

        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Long id = 1L;
        String name = "abc";
        String alias = "computers";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.empty());
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.of(category));

        String result = service.checkUnique(2L, name, alias);

        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOk() {
        Long id = 1L;
        String name = "abc";
        String alias = "computers";

        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(Optional.empty());
        Mockito.when(repo.findByAlias(alias)).thenReturn(Optional.empty());

        String result = service.checkUnique(2L, name, alias);

        Assertions.assertThat(result).isEqualTo("OK");
    }
}