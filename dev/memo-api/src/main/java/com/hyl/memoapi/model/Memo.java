package com.hyl.memoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_memo")
    private Long id;

    @Column(name = "last_modif")
    private Date lastModif;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToOne(targetEntity = ReminderByDay.class, mappedBy = "memo", fetch = FetchType.LAZY)
    private ReminderByDay reminderByDay;

    @OneToMany(targetEntity = ReminderByDate.class, mappedBy = "memo", fetch = FetchType.EAGER)
    private List<ReminderByDate> reminderByDate;

    @Column(name = "id_usager")
    private Long idUser;

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", lastModif=" + lastModif +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                (reminderByDay != null ? ", reminderByDay =" + reminderByDay : "") +
                (reminderByDate != null && !reminderByDate.isEmpty() ? ", reminderByDate=" + reminderByDate : "") +
                ", idUser=" + idUser +
                '}';
    }
}
