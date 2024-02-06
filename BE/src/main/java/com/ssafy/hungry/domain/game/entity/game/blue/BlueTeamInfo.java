package com.ssafy.hungry.domain.game.entity.game.blue;


import com.ssafy.hungry.domain.game.entity.game.GameStatus;
import com.ssafy.hungry.domain.game.entity.game.GameStatusId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "blue_team_info")
public class BlueTeamInfo {
    @EmbeddedId
    private GameStatusId id;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "game_code", referencedColumnName = "gameCode"),
            @JoinColumn(name = "game_turn", referencedColumnName = "gameTurn"),
            @JoinColumn(name = "turn_index", referencedColumnName = "turnIndex")
    })
    private GameStatus gameStatus;

    private boolean successReasoning;
    private int bonusTurn;
    private int leftLieChance;
    private int leftReasoningChance;
    private int totalReasoningChance;
    private int[] reasoningList;
    private int useSkill;
    private int[] unitCatch;
    private int yutResult;
    private int selectedUnit;
    private boolean isArrivedMissionRegion;

    @OneToMany(mappedBy = "blueTeamInfo")
    private List<BlueTeamGameUnit> unitList = new ArrayList<>();

    @OneToOne(mappedBy = "blueTeamInfo")
    private BlueGameMission gameMission;
}
