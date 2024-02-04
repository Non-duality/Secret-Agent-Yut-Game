package com.ssafy.hungry.domain.room.controller;

import com.ssafy.hungry.domain.room.dto.RoomDetailDto;
import com.ssafy.hungry.domain.room.dto.RoomLobbyInfoDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.service.RoomRedisService;
import com.ssafy.hungry.domain.room.service.RoomService;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompRoomController {

    private final JWTUtil jwtUtil;
    private final RoomService roomService;
    private final RoomRedisService roomRedisService;
    private final SimpMessagingTemplate messagingTemplate;

    // 방 입장
    @MessageMapping(value = "/room/{roomCode}/enter")
    public void enterRoom (@DestinationVariable String roomCode ,@Header("Authorization") String token){
        log.info("방 입장 호출 : " + roomCode);

        System.out.println(token);
        // token 을 가져와 userEntity 생성
        UserEntity user = jwtUtil.getUserEntity(token);
        // room code를 가져와 roomEntity 생성
        RoomEntity room = roomService.getRoomByRoomCode(roomCode);

        // redis room 정보 최신화
        RoomLobbyInfoDto roomLobbyInfoDto = roomRedisService.userEnterRoom(room, user);

        // 해당 방 구독자들에게 메시지 전달
        messagingTemplate.convertAndSend("/sub/room/" + roomCode, roomLobbyInfoDto);

    }

    // 방 나가기
    // Todo : 방장이 나갈 경우 방이 삭제되는 기능 구현
    @MessageMapping(value = "/room/{roomCode}/exit")
    public void exitRoom(@DestinationVariable String roomCode ,@Header("Authorization") String token){
        log.info("방 나가기 호출 : " + roomCode);
        // token 을 가져와 userEntity 생성
        UserEntity user = jwtUtil.getUserEntity(token);

        // room code를 가져와 roomEntity 생성
        RoomEntity room = roomService.getRoomByRoomCode(roomCode);

        RoomLobbyInfoDto roomLobbyInfoDto = roomRedisService.userExitRoom(room, user);

        // 해당 방 구독자들에게 메시지 전달
        messagingTemplate.convertAndSend("/sub/room/" + roomCode, roomLobbyInfoDto);


    }
}
