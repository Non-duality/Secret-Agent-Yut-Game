package com.ssafy.hungry.domain.pick.controller;

import com.ssafy.hungry.domain.pick.dto.CurrentUnitPickDto;
import com.ssafy.hungry.domain.pick.dto.CurrentUserPickDto;
import com.ssafy.hungry.domain.pick.service.PickRedisService;
import com.ssafy.hungry.global.dto.StompDataDto;
import com.ssafy.hungry.global.service.RedisSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompPickController {

    private final PickRedisService pickRedisService;
    private final RedisSender redisSender;
    private final ChannelTopic roomTopic;

    // 유닛 선택이 시작되면 유닛 선택 정보 반환, 선택할 수 있는 유닛 리스트도 반환.
    @MessageMapping(value = "/pick/{roomCode}/get-pre-info")
    public void pickCharacter (@DestinationVariable String roomCode){

        // 현재 유저 선택 정보 불러오기
        Map<String, List<CurrentUserPickDto>> currentUserPickInfo = pickRedisService.getCurrentUserPickInfo(roomCode);

        // 현재 유닛 리스트 불러오기
        Map<String, List<CurrentUnitPickDto>> currentUnitPickInfo = pickRedisService.getCurrentUnitPickInfo(roomCode);

        // 홍팀에 맞는 정보 넣기
        Map<String, Object> redTeamPickInfo = new HashMap<>();
        redTeamPickInfo.put("userInfo", currentUserPickInfo.get("홍팀"));
        redTeamPickInfo.put("unitInfo", currentUnitPickInfo.get("홍팀"));

        // 청팀에 맞는 정보 넣기
        Map<String, Object> blueTeamPickInfo = new HashMap<>();
        blueTeamPickInfo.put("userInfo", currentUserPickInfo.get("청팀"));
        blueTeamPickInfo.put("unitInfo", currentUnitPickInfo.get("청팀"));

        // 홍팀에게 홍팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_GET_PRE_INFO")
                .code(roomCode + "/red")
                .data(redTeamPickInfo) // Data 보내기
                .build());

        // 청팀에게 청팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_GET_PRE_INFO")
                .code(roomCode + "/blue")
                .data("") // Data 보내기
                .build());

    }


}
