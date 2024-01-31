package com.ssafy.hungry.domain.room.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentSeatDto {

    private int team;
    private int seatNumber;
    private int userId;
    private String nickname;
    private String profileImgUrl;
    private int state;

    @JsonProperty("ready")
    private boolean isReady;

}
