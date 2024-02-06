package com.ssafy.hungry.domain.game.entity;

import com.ssafy.hungry.domain.game.entity.game.GameStatusId;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
public class GameMissionParticipantId implements Serializable {

    private GameStatusId gameStatusId;
    private int userId;

}
