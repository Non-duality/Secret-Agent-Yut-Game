package com.ssafy.hungry.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UnitInfo {

    private int unitId;
    private String name;
    private int age;
    private String skill;

}
