package com.ssafy.hungry.domain.game.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "units")
public class UnitEntity {
    @Id
    @Column(name = "unit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int age;
    private String skill;

//    @OneToMany(mappedBy = "unit")
//    private List<QuestionEntity> questionList = new ArrayList<>();
}