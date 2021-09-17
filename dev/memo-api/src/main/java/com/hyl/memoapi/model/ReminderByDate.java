package com.hyl.memoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reminder_by_date")
public class ReminderByDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reminder_by_date")
    private Long id;

    @Column(name="reminder_date")
    private Date reminderDate;

    @ManyToOne(targetEntity = Memo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_memo")
    @JsonIgnore
    private Memo memo;

    @Override
    public String toString() {
        return "ReminderByDate{" +
                "id=" + id +
                ", reminderDate=" + reminderDate +
                ", idMemo=" + memo.getId() +
                '}';
    }
}
