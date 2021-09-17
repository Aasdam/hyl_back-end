package com.hyl.userapi.dao;

import com.hyl.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCellphone(String cellphone);
}
