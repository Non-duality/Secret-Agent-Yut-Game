package com.ssafy.hungry.domain.game.controller;

import com.ssafy.hungry.domain.game.dto.*;
import com.ssafy.hungry.domain.game.repository.GameRepository;
import com.ssafy.hungry.domain.game.service.GameService;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.global.repository.PrincipalRepository;
import com.ssafy.hungry.global.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameStompController {
    private final SessionRepository sessionRepository;
    private final PrincipalRepository principalRepository;
    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final UserRepository userRepository;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRepository gameRepository;

    //게임 시작
    @MessageMapping("/game/{roomCode}/start")
    public void gameStart(@DestinationVariable String roomCode, Principal principal) {

        // pub가 들어올 때마다 유저 추가하기
        String email = principalRepository.findById(principal.getName()).get().getEmail();
        log.info("gameStart 호출 : " + roomCode + " " + email);
        UserEntity user = userRepository.findByEmail(email);
        log.info("gameStart user검색 : " + roomCode + " " + user.toString());
        gameService.updateEnterUserInfo(roomCode, user);

        // 들어온 유저가 6명일 때만 game start
        if (gameService.getUserEnterCountInfo(roomCode) == 6 || true) {
            RoomEntity room = roomRepository.findByRoomCode(roomCode);
//            Map<String, Object> gamePreInfo = gameService.startGame(room);
            Map<String, Object> gamePreInfo = gameService.startGameDummy();
            simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode + "/red", gamePreInfo.get("홍팀"));
            simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode + "/blue", gamePreInfo.get("청팀"));
        }

    }

    //윳을 던진 결과를 전달
    @MessageMapping("/game/{roomCode}/throw-yut")
    public void throwYut(@DestinationVariable String roomCode, ThrowYutDto dto) {
        dto.setActionCategory(1);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    //이동에 선택된 말을 전달
    @MessageMapping("/game/{roomCode}/select-unit")
    public void selectUnit(@DestinationVariable String roomCode, SelectUnitDto dto) {
        dto.setActionCategory(2);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    //밀정 추리 전달
    @MessageMapping("/game/{roomCode}/reasoning")
    public void reasoning(@DestinationVariable String roomCode, ReasoningDto dto) {
        dto.setActionCategory(3);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }

    @MessageMapping("/game/{roomCode}/mission")
    public void mission(@DestinationVariable String roomCode){

    }
    //추리권 사용여부
    @MessageMapping("/game/{roomCode}/reason-ticket-use")
    public void reasonTicketUse(@DestinationVariable String roomCode, ReasonTicketDto dto){
        dto.setActionCategory(4);
        simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode, dto);
    }
    
    //게임 결과 (승리팀)
    @MessageMapping("/game/{roomCode}/chat")
    public void gameChat(@DestinationVariable String roomCode, GameChatDto gameChatDto){
        gameChatDto.setActionCategory(6);
        log.info("게임 채팅 호출 : " + roomCode + " " + gameChatDto.getMessage());

        if(gameChatDto.getTeam().equals("홍팀")){
            simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode + "/red", gameChatDto);

        }else if(gameChatDto.getTeam().equals("청팀")){
            simpMessagingTemplate.convertAndSend("/sub/game/" + roomCode + "/blue", gameChatDto);
        }

    }

    @MessageMapping("/game/selecttest")
    public void test2() {
        gameService.gameSelectTest();
    }


}

