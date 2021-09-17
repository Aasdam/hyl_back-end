package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface PictureDao extends JpaRepository<Picture, Long> {
    Optional<Picture> findByName(String name);
}
