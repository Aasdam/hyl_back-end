package com.hyl.loanapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.loanapi.model.constraint.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "loan")
@AlreadyClosedConstraint(groups = {Loan.CloseValidation.class})
@IdOwnerConstraint(groups = {Loan.CloseValidation.class, Loan.DeleteValidation.class})
@EndDateConstraint(groups = Loan.CloseValidation.class)
public class Loan {

    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}
    public interface CloseValidation {}
    public interface DeleteValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_loan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "{hyl.loan.id.error.notnull}", groups = {CloseValidation.class, DeleteValidation.class})
    private Long id;

    @Column(name = "id_usager")
    private Long idOwner;

    @NotNull(message = "{hyl.loan.startdate.error.notnull}", groups = {AddValidation.class})
    @PastOrPresent(message = "{hyl.loan.startdate.error.pastorpresent}", groups = {AddValidation.class})
    @Column(name = "start_date")
    private Date startDate;

    @NotNull(message = "{hyl.loan.enddate.error.notnull}", groups = {CloseValidation.class})
    @PastOrPresent(message = "{hyl.loan.enddate.error.pastorpresent}", groups = {CloseValidation.class})
    @Null(message = "{hyl.loan.enddate.error.null}", groups = {AddValidation.class})
    @Column(name = "end_date")
    private Date endDate;

    @NotBlank(message = "{hyl.loan.reference.error.notblank}", groups = {AddValidation.class})
    @Length(min = 5, max = 15, message = "{hyl.loan.reference.error.length}", groups = {AddValidation.class})
    @ReferenceConstraint(groups = {AddValidation.class})
    @Column(name = "reference")
    private String reference;

    @NoWhiteSpaceConstraint(message = "{hyl.loan.beneficiary.error.nowhitespace}", groups = {AddValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.loan.beneficiary.error.characterrepetition}", groups = {AddValidation.class})
    @NotBlank(message = "{hyl.loan.beneficiary.error.notblank}", groups = {AddValidation.class})
    @Length(min = 5, max = 15, message = "{hyl.loan.beneficiary.error.length}", groups = {AddValidation.class})
    @Column(name = "beneficiary")
    private String beneficiary;

    @InformationConstraint(groups = {AddValidation.class})
    @Column(name = "information")
    private String information;

    @Future(message = "{hyl.loan.reminder.error.future}", groups = {AddValidation.class})
    @Column(name = "reminder")
    private Date reminder;

    @Transient
    private String comment;

    @Transient @JsonIgnore
    private SimpleDateFormat simpleDateFormat;


    //************************************************** CONSTRUCTEURS
    public Loan() {
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }

    public Loan(Date startDate, Date endDate, String reference,
                String beneficiary, String information, String comment, Date reminder) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reference = reference;
        this.beneficiary = beneficiary;
        this.information = information;
        this.comment = comment;
        this.reminder = reminder;
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", idOwner=" + idOwner +
                ", startDate=" + (startDate != null ? simpleDateFormat.format(startDate) : null) +
                ", endDate=" + (endDate != null ? simpleDateFormat.format(endDate) : null) +
                ", reference='" + reference + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", information='" + information + '\'' +
                ", comment='" + comment + '\'' +
                ", reminder=" + (reminder != null ? simpleDateFormat.format(reminder) : null) +
                '}';
    }
}
