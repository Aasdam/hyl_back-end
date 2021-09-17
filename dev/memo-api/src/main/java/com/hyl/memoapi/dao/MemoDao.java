package com.hyl.memoapi.dao;

import com.hyl.memoapi.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MemoDao extends JpaRepository<Memo, Long> {
    List<Memo> findAllByIdUser(Long idUser);
}
