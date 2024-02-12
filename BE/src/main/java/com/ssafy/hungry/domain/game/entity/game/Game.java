package com.ssafy.hungry.domain.game.entity.game;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamMember;
import com.ssafy.hungry.domain.user.entity.UserGameHistoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "game_Code")
    private String gameCode;

    private int gameSpeed;
    private String gameTheme;
    private int[] missionRegion;

    @OneToMany(mappedBy = "gameCode")
    private List<UserGameHistoryEntity> userList = new ArrayList<>();

    @OneToMany(mappedBy = "gameCode")
    private List<RedTeamMember> redteamMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "gameCode")
    private List<GameStatus> gameStatusList = new ArrayList<>();
}
