package com.ssafy.hungry.domain.game.dto;

import lombok.Data;

@Data
public class UnitGoleDto {
    private int actionCategory;
    private int team;
    private int unitIndex;
    private boolean isSpy;
}
