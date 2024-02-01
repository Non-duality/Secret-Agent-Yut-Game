package com.ssafy.hungry.domain.room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompRoomController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping(value = "/pub/room/{roomCode}/enter")
    public void enterRoom (Authentication authentication){

    }
}
