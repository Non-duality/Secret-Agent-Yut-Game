package com.ssafy.hungry.domain.game.entity.game.red;

import com.ssafy.hungry.domain.game.entity.MissionEntity;
import com.ssafy.hungry.domain.game.entity.game.GameStatusId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "red_game_mission")
public class RedGameMission {
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
    private RedTeamInfo redTeamInfo;

    @OneToMany(mappedBy = "redGameMission")
    private List<RedTeamGameMissionParticipants> gameMissionParticipants = new ArrayList<>();
}