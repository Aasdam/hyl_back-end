package com.hyl.itemapi.dao;

import com.hyl.itemapi.model.TrackingSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TrackingSheetDao extends JpaRepository<TrackingSheet, Long> {
    Optional<TrackingSheet> findByIdLoan(Long idLoan);
    List<TrackingSheet> findAllByIdLoanIn(List<Long> idsLoan);
}
