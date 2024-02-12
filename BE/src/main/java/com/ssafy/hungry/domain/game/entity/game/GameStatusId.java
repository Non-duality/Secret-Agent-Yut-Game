package com.ssafy.hungry.domain.game.entity.game;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class GameStatusId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "game_code" , referencedColumnName = "game_code")
    private Game gameCode;

    @Column(name = "game_turn")
    private int gameTurn;

    @Column(name = "turn_index")
    private int turnIndex;


}
