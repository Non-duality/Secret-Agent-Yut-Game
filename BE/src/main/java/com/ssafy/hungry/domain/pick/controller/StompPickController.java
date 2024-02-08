package com.ssafy.hungry.domain.pick.controller;

import com.ssafy.hungry.domain.pick.dto.*;
import com.ssafy.hungry.domain.pick.service.PickRedisService;
import com.ssafy.hungry.global.dto.StompDataDto;
import com.ssafy.hungry.global.service.RedisSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompPickController {

    private final PickRedisService pickRedisService;
    private final RedisSender redisSender;
    private final ChannelTopic roomTopic;

    private final static int PICK_TIME = 15;

    // 유닛 선택이 시작되면 유닛 선택 정보 반환, 선택할 수 있는 유닛 리스트도 반환.
    @MessageMapping(value = "/pick/{roomCode}/get-pre-info")
    public void getPrePickInfo(@DestinationVariable String roomCode) {
        log.info("유닛 선택 호출 : " + roomCode);
        // 현재 유저 선택 정보 불러오기

        Map<String, List<CurrentUserPickDto>> currentUserPickInfo = pickRedisService.getCurrentUserPickInfo(roomCode);
        Map<String, List<CurrentUnitPickDto>> currentUnitPickInfo = pickRedisService.getCurrentUnitPickInfo(roomCode);

        Map<String, Object> redTeamPickInfo = new HashMap<>();
        redTeamPickInfo.put("userInfo", currentUserPickInfo.get("홍팀"));
        redTeamPickInfo.put("unitInfo", currentUnitPickInfo.get("홍팀"));

        Map<String, Object> blueTeamPickInfo = new HashMap<>();
        blueTeamPickInfo.put("userInfo", currentUserPickInfo.get("청팀"));
        blueTeamPickInfo.put("unitInfo", currentUnitPickInfo.get("청팀"));

        // 홍팀에게 홍팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_START")
                .code(roomCode + "/red")
                .data(redTeamPickInfo) // Data 보내기
                .build());

        // 청팀에게 청팀 캐릭터 선택 정보 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_START")
                .code(roomCode + "/blue")
                .data("") // Data 보내기
                .build());

    }


    // 유닛 픽 시작을 알림. timer 정보와, 픽을 시작해야하는 인원을 지정
    @MessageMapping(value = "/pick/{roomCode}/start")
    public void startPick(@DestinationVariable String roomCode) {
        log.info("유닛 픽 시작 호출 : " + roomCode);
        // 각 팀 첫번째 픽 순서 찾기
        String RedTeamPickEmail = pickRedisService.findPickOrder(roomCode, "홍팀");
        String BlueTeamPickEmail = pickRedisService.findPickOrder(roomCode, "청팀");

        // 홍팀에게 첫번째로 픽할 유저 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_ORDER")
                .code(roomCode + "/red")
                .data(OrderPickDto.builder()
                        .Email(RedTeamPickEmail)
                        .time(PICK_TIME)
                        .build()) // Data 보내기
                .build());

        // 청팀에게 첫번째로 픽할 유저 보내기
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_ORDER")
                .code(roomCode + "/blue")
                .data(OrderPickDto.builder()
                        .Email(BlueTeamPickEmail)
                        .time(PICK_TIME)
                        .build()) // Data 보내기
                .build());

    }

    // 한 유저가 pick이 완료 되었다면 redis에 수정사항 최신화
    @MessageMapping(value = "/pick/{roomCode}/done")
    public void pickDone(@DestinationVariable String roomCode, DonePickDto donePickDto) {
        log.info("유닛 픽 완료 호출 : " + roomCode);

        // 팀 정보를 확인하여 해당 팀의 픽 정보를 수정
        pickRedisService.updateCurrentPickInfo(roomCode, donePickDto);

        // 들어온 팀의 다음번 픽 순서를 확인
        String pickEmail = pickRedisService.findPickOrder(roomCode, donePickDto.getTeam());

        String targetRoomCode = "";
        if (donePickDto.getTeam().equals("홍팀")) {
            targetRoomCode = roomCode + "/red";
        } else if (donePickDto.getTeam().equals("청팀")) {
            targetRoomCode = roomCode + "/blue";
        }

        // 픽을 다 했다면 밀정 픽으로 넘어가기
        if (pickEmail.equals("")) {
            log.info("밀정 픽 시작 : " + roomCode);

            List<CurrentUnitPickDto> pickList = null;

            // Todo : 밀정 픽 선택을 위해 상대방의 pick 정보 받아오기
            if(donePickDto.getTeam().equals("홍팀")){
                pickList = pickRedisService.getPickUnitList(roomCode, "청팀");
            }else if(donePickDto.getTeam().equals("청팀")){
                pickList = pickRedisService.getPickUnitList(roomCode, "홍팀");
            }

            // 상대방의 픽 정보 넘기기
            redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                    .type("PICK_SELECT_SPY")
                    .code(targetRoomCode)
                    .data(pickList) // Data 보내기
                    .build());
        }

        // 픽을 다 하지 않았다면 다음 픽으로 넘어가기
        else {
            log.info("다음 픽 정보 전달 : " +donePickDto.getTeam() + roomCode);

            // 다음 픽을 할 유저 정보 전달
            redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                    .type("PICK_NEXT")
                    .code(targetRoomCode)
                    .data(OrderPickDto.builder()
                            .Email(pickEmail)
                            .time(PICK_TIME)
                            .build()) // Data 보내기
                    .build());
        }
    }

    // 밀정 선택 완료
    @MessageMapping(value="/pick/{roomCode}/spy")
    public void spyPick(@DestinationVariable String roomCode){

    }

    // 유닛 선택창의 팀 채팅
    @MessageMapping(value = "/pick/{roomCode}/chat")
    public void pickChat(@DestinationVariable String roomCode, PickChatMessageDto pickChatMessageDto) {
        log.info("유닛 선택 창 채팅 : " + pickChatMessageDto.getTeam() + roomCode);

        String targetRoomCode = "";
        if (pickChatMessageDto.getTeam().equals("홍팀")) {
            targetRoomCode = roomCode + "/red";
        } else if (pickChatMessageDto.getTeam().equals("청팀")) {
            targetRoomCode = roomCode + "/blue";
        }

        // redis Room Channel topic에 전달
        redisSender.sendToRedis(roomTopic, StompDataDto.builder()
                .type("PICK_CHAT")
                .code(targetRoomCode)
                .data(pickChatMessageDto)
                .build());
    }


}
