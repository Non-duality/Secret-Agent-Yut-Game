package com.ssafy.hungry.domain.game.controller;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.ReasoningDto;
import com.ssafy.hungry.domain.game.dto.SelectUnitDto;
import com.ssafy.hungry.domain.game.dto.ThrowYutDto;
import com.ssafy.hungry.domain.game.repository.GameRepository;
import com.ssafy.hungry.domain.game.service.GameService;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.global.repository.PrincipalRepository;
import com.ssafy.hungry.global.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameStompController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepository sessionRepository;
    private final PrincipalRepository principalRepository;
    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final UserRepository userRepository;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRepository gameRepository;


    //윳을 던진 결과를 전달
    @MessageMapping("/game/{roomCode}/throw-yut")
    public void throwYut(@DestinationVariable String roomCode, ThrowYutDto dto){
        dto.setActionCategory(1);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    //이동에 선택된 말을 전달
    @MessageMapping("/game/{roomCode}/select-unit")
    public void selectUnit(@DestinationVariable String roomCode, SelectUnitDto dto){
        dto.setActionCategory(2);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    //밀정 추리 전달
    @MessageMapping("/game/{roomCode}/reasoning")
    public void reasoning(@DestinationVariable String roomCode, ReasoningDto dto){
        dto.setActionCategory(3);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    @MessageMapping("/game/selecttest")
    public void test2(){
        gameService.gameSelectTest();
    }
}

