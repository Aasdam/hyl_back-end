package com.hyl.batch.controller;

import com.hyl.batch.model.Loan;
import com.hyl.batch.service.LoanService;
import com.hyl.batch.service.MailService;
import com.hyl.batch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

@Controller
public class BatchController implements CommandLineRunner {

    //****************************************** LOGGER
    private final Logger logger = LoggerFactory.getLogger(BatchController.class);

    //****************************************** BEANS
    private final UserService userService;
    private final LoanService loanService;
    private final MailService mailService;

    //****************************************** CONSTRUCTOR
    @Autowired
    public BatchController(UserService userService, LoanService loanService, MailService mailService) {
        this.userService = userService;
        this.loanService = loanService;
        this.mailService = mailService;
    }

    //****************************************** METHODES
    @Override
    public void run(String... args) throws Exception {

        // Récupération des prêts en cours où un rappel est prévu 'aujourd'hui'
        List<Loan> loans = loanService.getLoansToCallBack();

        // Récupération du token admin (pour passer l'authentification ayant lieu lors du contact avec le serveur-mail)
        HashMap<String,String> signInAdminMap = userService.signInAdmin();

        // Envoi d'un email à l'utilisateur
        loans.forEach(loan -> mailService.sendCallBack(loan, signInAdminMap));

    }
}
