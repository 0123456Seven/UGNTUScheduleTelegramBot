package ru.xaero.schedulebot.scheduletelegrambot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subgroups")
public class Subgroup {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Subgroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
