package com.ssafy.hungry.domain.game.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GameStartDto {
    private String gameCode;
    private int[] missionRegion;
    private int gameSpeed;
    private String gameTheme;
    private List<UserInfo> redTeamList;
    private List<UserInfo> blueTeamrList;
    private int gameTurn;
}
