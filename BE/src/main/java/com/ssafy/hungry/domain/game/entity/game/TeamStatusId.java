package com.ssafy.hungry.domain.game.entity.game;


import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class TeamStatusId implements Serializable {

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "game_code"),
            @JoinColumn(name = "game_turn", referencedColumnName = "game_turn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turn_index")
    })
    private GameStatus gameStatus;

}
