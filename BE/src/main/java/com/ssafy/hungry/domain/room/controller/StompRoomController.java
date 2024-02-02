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

        // token 을 가져와 userEntity 생성
        UserEntity user = jwtUtil.getUserEntity(token);
        // room code를 가져와 roomEntity 생성
        RoomEntity room = roomService.getRoomByRoomCode(roomCode);

        // redis room 정보 최신화
        roomRedisService.userEnterRoom(roomCode, user);

        // 방에 전달할 최신화된 정보 Dto
        RoomLobbyInfoDto roomLobbyInfoDto =RoomLobbyInfoDto.builder()
                .ownerId(room.getOwner().getId())
                .currentSeatDtoList(roomRedisService.getCurrentRoomInfo(room.getRoomCode()))
                .roomDetailDto(RoomDetailDto.builder()
                        .title(room.getTitle())
                        .isPublic(room.isPublic())
                        .gameSpeed(room.getGameSpeed())
                        .currentUserCount(roomRedisService.getCurrentUserCount(room.getRoomCode()))
                        .theme(room.getTheme())
                        .build())
                .message(user.getNickname() + "님이 입장하였습니다.")
                .build();

        messagingTemplate.convertAndSend("/sub/room/" + roomCode, roomLobbyInfoDto);

    }

    // 방 나가기
    @MessageMapping(value = "/room/{roomCode}/exit")
    public void exitRoom(@DestinationVariable String roomCode ,@Header("Authorization") String token){

        // token 을 가져와 userEntity 생성
        UserEntity user = jwtUtil.getUserEntity(token);

        // room code를 가져와 roomEntity 생성
        RoomEntity room = roomService.getRoomByRoomCode(roomCode);

        // redis room 정보 최신화
        roomRedisService.userEnterRoom(roomCode, user);

        // 방에 전달할 최신화된 정보 Dto
        RoomLobbyInfoDto roomLobbyInfoDto =RoomLobbyInfoDto.builder()
                .ownerId(room.getOwner().getId())
                .currentSeatDtoList(roomRedisService.getCurrentRoomInfo(room.getRoomCode()))
                .roomDetailDto(RoomDetailDto.builder()
                        .title(room.getTitle())
                        .isPublic(room.isPublic())
                        .gameSpeed(room.getGameSpeed())
                        .currentUserCount(roomRedisService.getCurrentUserCount(room.getRoomCode()))
                        .theme(room.getTheme())
                        .build())
                .message(user.getNickname() + "님이 퇴장하였습니다.")
                .build();

        messagingTemplate.convertAndSend("/sub/room/" + roomCode, roomLobbyInfoDto);

    }
}
