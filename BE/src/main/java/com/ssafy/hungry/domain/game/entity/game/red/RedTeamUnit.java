package com.ssafy.hungry.domain.game.entity.game.red;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.entity.game.RedTeamUnitId;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "red_team_unit")
public class RedTeamUnit {
    @EmbeddedId
    private RedTeamUnitId id;

    @MapsId("redTeamStatus")
    @ManyToOne
    @ToString.Exclude
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "game_code"),
            @JoinColumn(name = "game_turn", referencedColumnName = "game_turn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turn_index")
    })
    private RedTeamStatus redTeamStatus;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id")
    private UnitEntity unitId;

    private int position;
}
