package com.ssafy.hungry.domain.game.entity.game;

import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamInfo;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamInfo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "game_status")
public class GameStatus {
    @EmbeddedId
    private GameStatusId id;

    @MapsId("gameCode")
    @ManyToOne
    @JoinColumn(name = "game_code")
    private Game gameCode;

    @OneToOne(mappedBy = "gameStatus")
    private RedTeamInfo redTeamInfo;

    @OneToOne(mappedBy = "gameStatus")
    private BlueTeamInfo blueTeamInfo;
}
