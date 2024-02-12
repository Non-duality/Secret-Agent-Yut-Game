package com.ssafy.hungry.domain.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class QuestionEntity {
    @EmbeddedId
    private QuestionId id;

    @MapsId("unitId")
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private UnitEntity unitId;

    private String content;
}
