package com.ssafy.hungry.domain.pick.controller;

import com.ssafy.hungry.domain.pick.dto.CurrentPickDto;
import com.ssafy.hungry.domain.pick.service.PickRedisService;
import com.ssafy.hungry.global.dto.StompDataDto;
import com.ssafy.hungry.global.service.RedisSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompPickController {

    private final PickRedisService pickRedisService;
    private final RedisSender redisSender;
    private final ChannelTopic roomTopic;

    // 캐릭터 선택이 시작되면 캐릭터 선택 정보 반환.
    @MessageMapping(value = "/pick/{roomCode}/start-pick")
    public void pickCharacter (@DestinationVariable String roomCode){

        Map<String, List<CurrentPickDto>> currentPickInfo = pickRedisService.getCurrentPickInfo(roomCode);

        // 홍팀에게 홍팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_START")
                .code(roomCode + "/red")
                .data(currentPickInfo.get("홍팀")) // Data 보내기
                .build());

        // 청팀에게 청팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_START")
                .code(roomCode + "/blue")
                .data(currentPickInfo.get("청팀")) // Data 보내기
                .build());
    }
}
