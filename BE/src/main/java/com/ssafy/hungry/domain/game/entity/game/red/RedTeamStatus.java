package com.ssafy.hungry.domain.game.entity.game.red;

import com.ssafy.hungry.domain.game.entity.game.GameStatus;
import com.ssafy.hungry.domain.game.entity.game.TeamStatusId;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "red_team_status")
public class RedTeamStatus {
    @EmbeddedId
    private TeamStatusId id;

    @MapsId("gameStatus")
    @OneToOne
    @ToString.Exclude
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "game_code"),
            @JoinColumn(name = "game_turn", referencedColumnName = "game_turn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turn_index")
    })
    private GameStatus gameStatus;

    @OneToMany(mappedBy = "redTeamStatus")
    private List<RedTeamUnit> redTeamUnitList = new ArrayList<>();

    private boolean successReasoning;
}
