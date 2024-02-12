package com.ssafy.hungry.domain.miniGame.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class flyCatchDto {

    private int timer;
    private int flyCount;

}
