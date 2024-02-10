package com.ssafy.hungry.domain.pick.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PickInfoDto {

    private String team;
    private String email;
    private int unitId;

}
