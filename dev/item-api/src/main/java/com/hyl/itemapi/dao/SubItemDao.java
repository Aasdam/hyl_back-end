package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.SubItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface SubItemDao extends JpaRepository<SubItem, Long> {
    List<SubItem> findByReference(String reference);
}
