package com.hyl.batch.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "loan", schema = "loan")
public class Loan {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_loan")
    private Long id;

    @Column(name = "id_usager")
    private Long idOwner;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "reminder")
    private Date reminder;

    @Transient
    private User user;

    @Transient
    private SubItem subItem;

    @Transient
    private SimpleDateFormat simpleDateFormat;


    //************************************************** CONSTRUCTEUR
    public Loan() {
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

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }

    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", startDate=" + (startDate != null ? simpleDateFormat.format(startDate) : null) +
                ", endDate=" + (endDate != null ? simpleDateFormat.format(endDate) : null) +
                ", beneficiary='" + beneficiary + '\'' +
                ", user=" + user +
                ", subItem=" + subItem +
                ", reminder=" + (reminder != null ? simpleDateFormat.format(reminder) : null) +
                '}';
    }
}
