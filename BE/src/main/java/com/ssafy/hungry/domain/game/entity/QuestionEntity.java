package com.ssafy.hungry.domain.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
public class QuestionEntity {
    @EmbeddedId
    private QuestionId id;

    @MapsId("unitId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    private String content;
}