package com.ssafy.hungry.domain.game.entity.game.blue;

import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.entity.game.BlueTeamUnitId;
import com.ssafy.hungry.domain.game.entity.game.RedTeamUnitId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "blue_team_unit")
public class BlueTeamUnit {
    @EmbeddedId
    private BlueTeamUnitId id;

    @MapsId("blueTeamStatus")
    @ManyToOne
    @ToString.Exclude
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "game_code"),
            @JoinColumn(name = "game_turn", referencedColumnName = "game_turn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turn_index")
    })
    private BlueTeamStatus blueTeamStatus;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id")
    private UnitEntity unitId;

    private int position;
}
