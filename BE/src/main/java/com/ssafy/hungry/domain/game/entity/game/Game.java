package com.ssafy.hungry.domain.game.entity.game;

import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamMember;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamMember;
import com.ssafy.hungry.domain.user.entity.UserGameHistoryEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "game")
public class Game {
    @Id
    private String gameCode;

    private int[] missionRegion;
    private int gameSpeed;
    private String gameTheme;

    @OneToMany(mappedBy = "gameCode")
    private List<BlueTeamMember> blueTeamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "gameCode")
    private List<RedTeamMember> redTeamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "gameCode")
    private List<UserGameHistoryEntity> userGameHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "gameCode")
    private List<GameStatus> gameStatusList = new ArrayList<>();
}
