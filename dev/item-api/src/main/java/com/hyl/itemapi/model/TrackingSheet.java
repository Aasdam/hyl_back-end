package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.itemapi.model.constraint.AtomicIdLoanConstraint;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tracking_sheet")
public class TrackingSheet {


    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_tracking_sheet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.trackingsheet.id.error.null}", groups = {AddValidation.class})
    private Long id;

    @PastOrPresent(message = "{hyl.trackingsheet.date.error.pastorpresent}", groups = {AddValidation.class})
    @Column(name = "date")
    private Date date;

    @NotBlank(message = "{hyl.trackingsheet.comment.error.notblank}", groups = {AddValidation.class})
    @Length(min = 15, max = 150, message = "{hyl.trackingsheet.comment.error.length}", groups = {AddValidation.class})
    @Column(name = "comment")
    private String comment;

    @IdOwnerConstraint(groups = {AddValidation.class})
    @NotNull(message = "{hyl.trackingsheet.subitem.error.notnull}", groups = {AddValidation.class})
    @ManyToOne(targetEntity = SubItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subitem")
    @JsonIgnore
    private SubItem subItem;

    @AtomicIdLoanConstraint(groups = {AddValidation.class})
    @Column(name = "id_loan")
    private Long idLoan;

    @Transient @JsonIgnore
    private SimpleDateFormat simpleDateFormat;


    //************************************************** CONSTRUCTEURS
    public TrackingSheet() {
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    }


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }

    public Long getIdLoan() {
        return idLoan;
    }

    public void setIdLoan(Long idLoan) {
        this.idLoan = idLoan;
    }

    //************************************************** TO STRING
    @Override
    public String toString() {
        return "TrackingSheet{" +
                "id=" + id +
                ", date=" + (date != null ? simpleDateFormat.format(date) : null) +
                ", comment='" + comment + '\'' +
                ", subItem.id=" + (subItem != null ? subItem.getId() : null) +
                ", idLoan='" + idLoan + '\'' +
                '}';
    }
}
