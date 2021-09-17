package com.hyl.loanapi.dao;

import com.hyl.loanapi.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface LoanDao extends JpaRepository<Loan, Long> {
    List<Loan> findAllByIdOwnerAndEndDateIsNull(long idOwner);
    List<Loan> findAllByIdOwnerAndEndDateIsNotNull(long idOwner);
    Optional<Loan> findByReferenceAndIdOwnerAndEndDateIsNull(String string, Long idUser);
    List<Loan> findAllByReferenceAndIdOwner(String reference, long idUser);
}
