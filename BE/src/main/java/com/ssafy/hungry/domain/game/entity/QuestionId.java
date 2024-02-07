package com.ssafy.hungry.domain.game.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class QuestionId implements Serializable {

    private int unitId;
    private int questionIndex;

}