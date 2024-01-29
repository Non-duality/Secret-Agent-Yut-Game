package com.ssafy.hungry.domain.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyInfoDto {
    private String email;
    private String nickname;
    private Date birthDate;
    private String gender;
    private String profileImgUrl;
}
