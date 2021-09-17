package com.hyl.loanapi.service;

import com.hyl.loanapi.dao.LoanDao;
import com.hyl.loanapi.exception.CustomBadRequestException;
import com.hyl.loanapi.exception.CustomNotFoundException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import com.hyl.loanapi.proxy.ItemProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanService {

    // ********************************************************* logger
    private final Logger logger = LoggerFactory.getLogger(LoanService.class);


    // ********************************************************* Bean
    private final LoanDao loanDao;
    private final ItemProxy itemProxy;


    // ********************************************************* Constructeur
    @Autowired
    public LoanService(LoanDao loanDao, ItemProxy itemProxy) {
        this.loanDao = loanDao;
        this.itemProxy = itemProxy;
    }


    // ********************************************************* Métohdes
    public Loan getLoan(Long idLoan) {
        if (idLoan == null) throw new CustomBadRequestException("L'id du prêt recherché ne peut pas être null");
        Optional<Loan> optLoan = loanDao.findById(idLoan);
        if (optLoan.isPresent()) return optLoan.get();
        else throw new CustomNotFoundException("Le prêt "+idLoan+" n'existe pas");

    }

    public List<Loan> getLoans(long idOwner, State state, String token) {
        switch (state) {
            case IN_PROGRESS:
                return loanDao.findAllByIdOwnerAndEndDateIsNull(idOwner);
            case TERMINATED:
                HashMap<String, String> header = new HashMap<>();
                header.put(HttpHeaders.AUTHORIZATION, token);
                header.put("idUser", String.valueOf(idOwner));
                List<Loan> loans = loanDao.findAllByIdOwnerAndEndDateIsNotNull(idOwner);
                loans.forEach(loan -> loan.setComment(itemProxy.getComment(header, loan.getId())));
                return loans;
            default:
                return new ArrayList<>();
        }
    }

    public Loan addLoan(long idOwner, String token, Loan loan) {
        loan.setIdOwner(idOwner);
        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, token);
        header.put("idUser", String.valueOf(idOwner));
        if (!itemProxy.checkReference(header, loan.getReference()))
            throw new CustomNotFoundException("L'objet subitem avec reference="+loan.getReference()+" n'a pas été trouvé.");
        return this.loanDao.save(loan);
    }

    public void closeLoan(Loan pLoan, long idUser, String token) {
        Loan loan = this.getLoan(pLoan.getId());
        loan.setEndDate(pLoan.getEndDate());
        loanDao.save(loan);

        String comment = pLoan.getComment();
        if (comment != null && !comment.isBlank()) {
            HashMap<String, String> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, token);
            header.put("idUser", String.valueOf(idUser));
            HashMap<String, String> body = new HashMap<>();
            body.put("comment", comment);
            body.put("reference", loan.getReference());
            body.put("idLoan", String.valueOf(loan.getId()));
            itemProxy.postComment(header, body);
        }
    }

    public void deleteLoan(long idUser, String token, List<Loan> loans) {
        List<String> ids = new ArrayList<>();
        loans.forEach(loan -> {
            ids.add(String.valueOf(loan.getId()));
        });
        if (!ids.isEmpty()) {
            HashMap<String, String> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, token);
            header.put("idUser", String.valueOf(idUser));
            itemProxy.deleteTrackingSheets(header, ids);
        }
        loanDao.deleteAll(loans);
    }

    public void deleteLoansByReference(String reference, long idUser) {
        List<Loan> loans = loanDao.findAllByReferenceAndIdOwner(reference, idUser);
        if (!loans.isEmpty()) loanDao.deleteAll(loans);
    }

    public void updateReferenceInLoans(String oldReference, String newReference, long idUser) {
        List<Loan> loans = loanDao.findAllByReferenceAndIdOwner(oldReference, idUser);
        if (!loans.isEmpty()) {
            loans.forEach(loan -> loan.setReference(newReference));
            loanDao.saveAll(loans);
        }
    }

    public boolean isAlreadyInProgressByRefAndIdUser(String reference, long idUser) {
        return loanDao.findByReferenceAndIdOwnerAndEndDateIsNull(reference, idUser).isEmpty();
    }

    public boolean checkSubItemAvailable(String reference, long idUser, String token) {
        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, token);
        header.put("idUser", String.valueOf(idUser));

        if (itemProxy.checkReference(header, reference)) {
            return isAlreadyInProgressByRefAndIdUser(reference, idUser);
        } else {
            return false;
        }
    }
}
