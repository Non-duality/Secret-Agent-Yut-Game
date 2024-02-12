package com.ssafy.hungry.domain.game.entity.game;

import com.ssafy.hungry.domain.game.entity.game.red.RedTeamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class RedTeamUnitId implements Serializable {

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "game_code"),
            @JoinColumn(name = "game_turn", referencedColumnName = "game_turn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turn_index")
    })
    private RedTeamStatus redTeamStatus;

    private int unitIndex;

}
