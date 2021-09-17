package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface CategoryDao extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndIdUser(String name, long idUser);
    List<Category> findAllByIdUserAndCategoryParentIsNull(long idUser);
}
