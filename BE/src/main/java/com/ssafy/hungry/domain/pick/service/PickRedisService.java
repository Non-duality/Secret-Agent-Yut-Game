package com.ssafy.hungry.domain.pick.service;

import com.ssafy.hungry.domain.pick.dto.CurrentPickDto;
import com.ssafy.hungry.domain.pick.exception.TeamNotFoundException;
import com.ssafy.hungry.domain.pick.repository.PickRedisRepository;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickRedisService {

    private final PickRedisRepository pickRedisRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final static String PICK_KEY_PREFIX = "PickInfo:";
    private final static String ROOM_KEY_PREFIX = "RoomInfo:";

    // redis키 생성하기
    public String generateKey(String roomCode){
        return PICK_KEY_PREFIX + " " + roomCode;
    }

    // 방에서 게임 준비가 완료되면 캐릭터 선택 정보를 currentSeatInfo를 활용하여 redis에 추가
    public void createCurrentPick(String roomCode){
        String key = generateKey(roomCode);
        String RoomKey = ROOM_KEY_PREFIX + " " + roomCode;
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(RoomKey);
        System.out.println(currentSeatDtoList.toString());
        // 팀을 나눠서 저장 0 ~ 2번은 1팀(홍팀), 3 ~ 5번은 2팀(청팀)
        for(int i = 0; i < 3; i++){
            pickRedisRepository.saveToRedis(key, CurrentPickDto.builder()
                            .state(0)
                            .team(1)
                            .seatNumber(i)
                            .userId(currentSeatDtoList.get(i).getUserId())
                            .nickname(currentSeatDtoList.get(i).getNickname())
                            .build());
        }

        for(int i = 3; i < 6; i++){
            pickRedisRepository.saveToRedis(key, CurrentPickDto.builder()
                            .state(0)
                            .team(2)
                            .seatNumber(i)
                            .userId(currentSeatDtoList.get(i).getUserId())
                            .nickname(currentSeatDtoList.get(i).getNickname())
                            .build());
        }

    }

    // 현재 캐릭터 선택 정보 반환하기
    public Map<String, List<CurrentPickDto>> getCurrentPickInfo(String roomCode){
        String key = generateKey(roomCode);

        // 홍팀 캐릭터 선택창 정보
        List<CurrentPickDto> redTeamCurrentPickInfo = pickRedisRepository.getCurrentPickInfo(key, 0, 2);

        // 청팀 캐릭터 선택창 정보
        List<CurrentPickDto> blueTeamCurrentPickInfo = pickRedisRepository.getCurrentPickInfo(key, 3, 5);

        // 나눈 캐릭터 선택 정보를 Map에 담기
        Map<String, List<CurrentPickDto>> currentPickInfo = new HashMap<>();

        currentPickInfo.put("홍팀", redTeamCurrentPickInfo);
        currentPickInfo.put("청팀", blueTeamCurrentPickInfo);

        return currentPickInfo;
    }


}
