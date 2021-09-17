package com.hyl.batch.dao;

import com.hyl.batch.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanDao extends JpaRepository<Loan, Long> {
    List<Loan> findAllByReminderBetweenAndEndDateIsNull(Date intervalMin, Date intervalMax);
}
