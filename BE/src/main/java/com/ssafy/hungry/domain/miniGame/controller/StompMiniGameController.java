package com.ssafy.hungry.domain.miniGame.controller;

import com.ssafy.hungry.global.dto.StompDataDto;
import com.ssafy.hungry.global.service.RedisSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompMiniGameController {

    private final ChannelTopic gameTopic;
    private final RedisSender redisSender;

    @MessageMapping(value="/pick/{roomCode}/start-flyCatch")
    public void startFlyCatch(@DestinationVariable String roomCode){

        Map<String, Object> flyCatchStartInfo = new HashMap<>();
        flyCatchStartInfo.put("timer", 15);
        flyCatchStartInfo.put("flyCount", 5);
        flyCatchStartInfo.put("", 15);


        redisSender.sendToRedis(gameTopic, StompDataDto.builder()
                .type("MINIGAME_FLY_CATCH_START")
                .code(roomCode)
                .data("") // Data 보내기
                .build());
    }

    @MessageMapping(value="/pick/{roomCode}/catch-fly")
    public void catchFly(@DestinationVariable String roomCode){

    }

    @MessageMapping(value="/pick/{roomCode}/finish-flyCatch")
    public void finishFlyCatch(@DestinationVariable String roomCode){

    }

}
