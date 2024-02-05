package com.ssafy.hungry.domain.room.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageDto {

    private String nickname;
    private String message;

}
