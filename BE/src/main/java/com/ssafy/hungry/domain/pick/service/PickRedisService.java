package com.ssafy.hungry.domain.pick.service;

import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.repository.GameRepository;
import com.ssafy.hungry.domain.pick.dto.CurrentUnitPickDto;
import com.ssafy.hungry.domain.pick.dto.CurrentUserPickDto;
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
    private final GameRepository gameRepository;
    private final static String PICK_KEY_PREFIX = "UserPickInfo:";
    private final static String ROOM_KEY_PREFIX = "RoomInfo:";
    private final static String RED_KEY_PREFIX = "RedUnitInfo:";
    private final static String BLUE_KEY_PREFIX = "BlueUnitInfo:";

    // redis키 생성하기
    public String generateKey(String prefix , String roomCode){
        return prefix + " " + roomCode;
    }

    // 방에서 게임 준비가 완료되면 유저들의 유닛 선택 정보를 currentSeatInfo를 활용하여 redis에 추가
    public void createCurrentUserPick(String roomCode){
        String PickKey = generateKey(PICK_KEY_PREFIX, roomCode);
        String RoomKey = generateKey(ROOM_KEY_PREFIX, roomCode);

        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(RoomKey);
        System.out.println(currentSeatDtoList.toString());
        // 팀을 나눠서 저장 0 ~ 2번은 1팀(홍팀), 3 ~ 5번은 2팀(청팀)
        for(int i = 0; i < 3; i++){
            pickRedisRepository.saveUserPickToRedis(PickKey, CurrentUserPickDto.builder()
                            .state(0)
                            .team(1)
                            .seatNumber(i)
                            .userId(currentSeatDtoList.get(i).getUserId())
                            .nickname(currentSeatDtoList.get(i).getNickname())
                            .build());
        }

        for(int i = 3; i < 6; i++){
            pickRedisRepository.saveUserPickToRedis(PickKey, CurrentUserPickDto.builder()
                            .state(0)
                            .team(2)
                            .seatNumber(i)
                            .userId(currentSeatDtoList.get(i).getUserId())
                            .nickname(currentSeatDtoList.get(i).getNickname())
                            .build());
        }

    }

    // 홍팀, 청팀 유닛 정보를 redis에 저장하기
    public void createCurrentUnitPick(String roomCode){
        String redUnitKey = generateKey(RED_KEY_PREFIX, roomCode);
        String blueUnitKey = generateKey(BLUE_KEY_PREFIX, roomCode);

        List<UnitEntity> unitList = gameRepository.findAll();
        log.info("유닛 정보 리스트 : " + unitList.toString());
        // 홍팀 유닛 정보 redis에 저장
        for(UnitEntity unit : unitList){

            CurrentUnitPickDto unitPickDto = CurrentUnitPickDto.builder()
                            .unitId(unit.getId())
                            .name(unit.getName())
                            .age(unit.getAge())
                            .skill(unit.getSkill())
                            .isPick(false)
                            .build();

            if (unit.getId() == 4 || unit.getId() == 5){
                unitPickDto.setPick(true);
            }

            pickRedisRepository.saveUnitPickToRedis(redUnitKey, unitPickDto);
        }

        // 청팀 유닛 정보 redis에 저장
        for(UnitEntity unit : unitList){

            CurrentUnitPickDto unitPickDto = CurrentUnitPickDto.builder()
                    .unitId(unit.getId())
                    .name(unit.getName())
                    .age(unit.getAge())
                    .skill(unit.getSkill())
                    .isPick(false)
                    .build();

            if (unit.getId() == 4 || unit.getId() == 5){
                unitPickDto.setPick(true);
            }

            pickRedisRepository.saveUnitPickToRedis(blueUnitKey, unitPickDto);
        }

    }

    // 현재 유저들의 유닛 선택 정보 반환하기
    public Map<String, List<CurrentUserPickDto>> getCurrentUserPickInfo(String roomCode){
        String key = generateKey(PICK_KEY_PREFIX,roomCode);

        // 홍팀 유저들의 유닛 선택창 정보
        List<CurrentUserPickDto> redTeamCurrentPickInfo = pickRedisRepository.getCurrentUserPickInfo(key, 0, 2);

        // 청팀 유저들의 유닛 선택창 정보
        List<CurrentUserPickDto> blueTeamCurrentPickInfo = pickRedisRepository.getCurrentUserPickInfo(key, 3, 5);

        // 나눈 유저들의 유닛 선택창 정보를 Map에 담기
        Map<String, List<CurrentUserPickDto>> currentPickInfo = new HashMap<>();

        currentPickInfo.put("홍팀", redTeamCurrentPickInfo);
        currentPickInfo.put("청팀", blueTeamCurrentPickInfo);

        return currentPickInfo;
    }

    // 현재 유닛들의 정보와 선택 상황 정보 반환하기
    public Map<String, List<CurrentUnitPickDto>> getCurrentUnitPickInfo(String roomCode){
        String redUnitKey = generateKey(RED_KEY_PREFIX, roomCode);
        String blueUnitKey = generateKey(BLUE_KEY_PREFIX, roomCode);

        // 홍팀 유저들의 유닛 선택창 정보
        List<CurrentUnitPickDto> redTeamCurrentUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo(redUnitKey);

        // 청팀 유저들의 유닛 선택창 정보
        List<CurrentUnitPickDto> blueTeamCurrentUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo(blueUnitKey);

        // 나눈 유저들의 유닛 선택창 정보를 Map에 담기
        Map<String, List<CurrentUnitPickDto>> currentUnitPickInfo = new HashMap<>();

        currentUnitPickInfo.put("홍팀", redTeamCurrentUnitPickInfo);
        currentUnitPickInfo.put("청팀", blueTeamCurrentUnitPickInfo);

        return currentUnitPickInfo;
    }


}
