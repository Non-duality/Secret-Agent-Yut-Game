package com.ssafy.hungry.domain.user.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameHistoryDto {

    private int gameSpeed;
    private String gameTheme;
    private int[] missionRegion;
    private String winner;
    private String blueSpyName;
    private String redSpyName;
    private boolean redTeamReasoningResult;
    private boolean blueTeamReasoningResult;

    private List<BlueTeamMemberDto> blueTeamMemberDtoList = new ArrayList<>();
    private List<RedTeamMemberDto> redTeamMemberDtoList = new ArrayList<>();
    private List<BlueTeamUnitDto> blueTeamUnitDtoList = new ArrayList<>();
    private List<RedTeamUnitDto> redTeamUnitDtoList = new ArrayList<>();

}
