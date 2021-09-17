package com.hyl.memoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reminder_by_day")
public class ReminderByDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reminder_by_day")
    private Long id;

    @Column(name = "monday")
    private Boolean monday;

    @Column(name = "tuesday")
    private Boolean tuesday;

    @Column(name = "wednesday")
    private Boolean wednesday;

    @Column(name = "thursday")
    private Boolean thursday;

    @Column(name = "friday")
    private Boolean friday;

    @Column(name = "saturday")
    private Boolean saturday;

    @Column(name = "sunday")
    private Boolean sunday;

    @OneToOne(targetEntity = Memo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_memo")
    @JsonIgnore
    private Memo memo;

    @Override
    public String toString() {
        return "ReminderByDay{" +
                "id=" + id +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", idMemo=" + memo.getId() +
                '}';
    }
}
