package com.ssafy.hungry.domain.game.entity.game;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamStatus;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@Entity
@Table(name = "game_status")
public class GameStatus {

    @EmbeddedId
    private GameStatusId id;

    @MapsId("gameCode")
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "game_code", referencedColumnName = "game_code")
    private Game gameCode;

    @OneToOne(mappedBy = "gameStatus")
    private RedTeamStatus redTeamStatus;

    @OneToOne(mappedBy = "gameStatus")
    private BlueTeamStatus blueTeamStatus;
}
