package com.ssafy.hungry.domain.game.entity.game;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class GameUnitId implements Serializable {
    private GameStatusId gameStatusId;

    private int unitId;
}
