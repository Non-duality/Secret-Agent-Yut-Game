package com.ssafy.hungry.domain.room.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrentSeatDto {

    private int team;
    private int seatNumber;
    private int userId;
    private String nickname;
    private String profileImgUrl;
    private int state;
    private boolean isReady;

}
