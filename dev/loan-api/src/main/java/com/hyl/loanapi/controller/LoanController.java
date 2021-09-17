package com.hyl.loanapi.controller;

import com.hyl.loanapi.exception.CustomBadRequestException;
import com.hyl.loanapi.exception.CustomNotFoundException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import com.hyl.loanapi.model.validation.ValidCloseLoansListValidation;
import com.hyl.loanapi.service.LoanService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "loan")
public class LoanController {

    // ********************************************************* logger
    private final Logger logger = LoggerFactory.getLogger(LoanController.class);


    // ********************************************************* Bean
    private final LoanService loanService;


    // ********************************************************* Constructeur
    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    // ********************************************************* GET
    @GetMapping("/in-progress")
    public List<Loan> getLoansInProgress(@Autowired HttpServletRequest request) {
        return loanService.getLoans(extractIdUserFromHeader(request), State.IN_PROGRESS, null);
    }

    @GetMapping("/terminated")
    public List<Loan> getLoansTerminated(@Autowired HttpServletRequest request) {
        return loanService.getLoans(extractIdUserFromHeader(request), State.TERMINATED, extractJWTFromHeader(request));
    }

    @GetMapping("/check-sub-available")
    public boolean checkSubitemAvailable(@RequestParam String reference, @Autowired HttpServletRequest request) {
        return !loanService.checkSubItemAvailable(reference, extractIdUserFromHeader(request), extractJWTFromHeader(request));
    }


    // ********************************************************* POST
    @PostMapping("/add-loan")
    public Loan addLoan(@Autowired HttpServletRequest request,
                        @Validated(Loan.AddValidation.class) @RequestBody Loan loan) {
        return loanService.addLoan(extractIdUserFromHeader(request), extractJWTFromHeader(request), loan);
    }

    @PostMapping("/close-loans")
    public void closeLoan(@Validated(Loan.CloseValidation.class) @RequestBody ValidCloseLoansListValidation loans,
                          @Autowired HttpServletRequest request) {
        loans.forEach(loan -> loanService.closeLoan(loan, extractIdUserFromHeader(request), extractJWTFromHeader(request)));
    }


    // ********************************************************* PATCH
    @PatchMapping(value = "/update-reference")
    public void updateReference(@Autowired HttpServletRequest request, @RequestBody HashMap<String, String> hashMap) {
        String oldReference = hashMap.get("oldReference");
        String newReference = hashMap.get("newReference");

        if (oldReference == null || oldReference.isBlank())
            throw new CustomBadRequestException("Le paramètre oldReference n'est pas présent.");
        if (newReference == null || newReference.isBlank())
            throw new CustomBadRequestException("Le paramètre newReference n'est pas présent.");

        loanService.updateReferenceInLoans(oldReference, newReference, extractIdUserFromHeader(request));
    }


    // ********************************************************* DELETE
    @DeleteMapping("/delete-loans")
    public void deleteLoan(@Validated(Loan.DeleteValidation.class)
                               @RequestParam(name = "ids") ValidCloseLoansListValidation loans,
                           @Autowired HttpServletRequest request) {

        loanService.deleteLoan(extractIdUserFromHeader(request), extractJWTFromHeader(request), loans.getList());
    }

    @DeleteMapping("/delete-loans-by-reference")
    public void deleteLoanByReference(@Autowired HttpServletRequest request, @RequestParam String reference) {
        if (reference == null || reference.isBlank()) {
            throw new CustomBadRequestException("La référence ne doit pas être null ou vide");
        }
        loanService.deleteLoansByReference(reference, extractIdUserFromHeader(request));
    }


    // ********************************************************* SHARE
    @ExceptionHandler(value = {CustomBadRequestException.class, CustomNotFoundException.class, FeignException.class})
    public ResponseEntity<?> handle(RuntimeException exception) {

        String className = exception.getClass().getName();
        if (className.contains("$")) {
            className = className.substring(0, className.indexOf("$"));
        }

        if (className.equals(CustomBadRequestException.class.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        } else if (className.equals(CustomNotFoundException.class.getName())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        } else if (className.equals(FeignException.class.getName())) {
            return ResponseEntity.status(((FeignException) exception).status()).body(exception);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
        }
    }

    public static long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        try {
            return Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur doit être un nombre.");
        }
    }

    public static String extractJWTFromHeader (HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isBlank()) {
            throw new CustomBadRequestException("Aucun token n'est spécifié dans le header 'AUTHORIZATION' de la requête.");
        }
        return token;
    }
}
