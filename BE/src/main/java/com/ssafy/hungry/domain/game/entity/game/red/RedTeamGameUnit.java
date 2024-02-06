package com.ssafy.hungry.domain.game.entity.game.red;

import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.entity.game.GameUnitId;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "red_team_game_unit")
public class RedTeamGameUnit {
    @EmbeddedId
    private GameUnitId id;

    @MapsId("unitId")
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    @MapsId("gameStatusId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "game_code"),
            @JoinColumn(name = "game_turn"),
            @JoinColumn(name = "turn_index")
    })
    private RedTeamInfo redTeamInfo;

    @ManyToOne
    @JoinColumn(name = "picked_user", referencedColumnName = "id")
    private UserEntity pickedUser;

    private int location;
    private boolean isGole;
}
