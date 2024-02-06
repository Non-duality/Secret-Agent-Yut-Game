package com.ssafy.hungry.domain.game.entity.game.blue;

import com.ssafy.hungry.domain.game.entity.MissionEntity;
import com.ssafy.hungry.domain.game.entity.game.GameStatusId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "blue_game_mission")
public class BlueGameMission {
    @EmbeddedId
    private GameStatusId id;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private MissionEntity mission;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(referencedColumnName = "gameCode"),
            @JoinColumn(referencedColumnName = "gameTurn"),
            @JoinColumn(referencedColumnName = "turnIndex")
    })
    private BlueTeamInfo blueTeamInfo;

    @OneToMany(mappedBy = "blueGameMission")
    private List<BlueTeamGameMissionParticipants> gameMissionParticipants = new ArrayList<>();
}
