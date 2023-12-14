package ru.xaero.schedulebot.scheduletelegrambot.models;


import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.time.LocalDate;

@Table(name = "schedule")
@Entity
public class Schedule {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "subgroup_id", referencedColumnName = "id")
    private Subgroup subgroup;
    @ManyToOne
    @JoinColumn(name = "pair_id", referencedColumnName = "id")
    private PairsTime pairsTime;
    @Column(name = "date")
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private StudentGroup studentGroup;
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Types type;

    public Schedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subgroup getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(Subgroup subgroup) {
        this.subgroup = subgroup;
    }

    public PairsTime getPairsTime() {
        return pairsTime;
    }

    public void setPairsTime(PairsTime pairsTime) {
        this.pairsTime = pairsTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StudentGroup getGroup() {
        return studentGroup;
    }

    public void setGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }
}
