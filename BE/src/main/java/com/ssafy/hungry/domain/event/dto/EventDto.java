package com.ssafy.hungry.domain.event.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventDto {
    private String fromUserEmail;
    private String toUserEmail;
    private String eventCategory;
    private String eventAction;
    private String message;
}