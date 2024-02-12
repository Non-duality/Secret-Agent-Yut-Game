package com.ssafy.hungry.domain.game.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo {
    private String email;
    private String nickname;
    private String profileImgUrl;
}
