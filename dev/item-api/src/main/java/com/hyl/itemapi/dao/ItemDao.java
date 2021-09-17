package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ItemDao extends JpaRepository<Item, Long> {
    Optional<Item> findByNameAndIdUser(String name, long idUser);
    List<Item> findAllByIdUser(long idUser);
    List<Item> findAllByIdUserAndCategoryIsNull(long idUser);
}
