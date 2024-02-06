package com.ssafy.hungry.domain.game.entity.game.red;

import com.ssafy.hungry.domain.game.entity.GameMissionParticipantId;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "red_team_game_mission_participants")
public class RedTeamGameMissionParticipants {
    @EmbeddedId
    private GameMissionParticipantId id;

    @MapsId("gameStatusId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(referencedColumnName = "gameCode"),
            @JoinColumn(referencedColumnName = "gameTurn"),
            @JoinColumn(referencedColumnName = "turnIndex")
    })
    private RedGameMission redGameMission;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user_id;

    private int labTime;
    private int count;
    private boolean success;
}