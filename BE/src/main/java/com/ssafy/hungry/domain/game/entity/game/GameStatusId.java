package com.ssafy.hungry.domain.game.entity.game;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class GameStatusId implements Serializable {

    private String gameCode;
    private String gameTurn;
    private String turnIndex;

}
