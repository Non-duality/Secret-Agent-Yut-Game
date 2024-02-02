package com.ssafy.hungry.domain.room.service;

import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomRedisService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;

    private final static String ROOM_KEY_PREFIX = "RoomInfo:";

    // redis키 생성하기
    public String generateKey(String roomCode){
        return ROOM_KEY_PREFIX + " " + roomCode;
    }

    // 현재 방에 참여한 인원 구하기
    public int getCurrentUserCount(int roomId){
        String roomCode = roomRepository.findByRoomId(roomId);
        return getCurrentUserCount(roomCode);
    };

    // 방을 생성 한 후 현재 좌석 정보를 redis에 추가
    public void createCurrentSeat(String roomCode){
        String key = generateKey(roomCode);

        // 팀을 나눠서 저장 0 ~ 2번은 1팀, 3 ~ 5번은 2팀
        for(int i = 0; i < 3; i++){
            roomRedisRepository.saveToRedis(key, CurrentSeatDto.builder()
                    .state(0)
                    .team(1)
                    .seatNumber(i)
                    .build());
        }

        for(int i = 3; i < 6; i++){
            roomRedisRepository.saveToRedis(key, CurrentSeatDto.builder()
                    .state(0)
                    .team(2)
                    .seatNumber(i)
                    .build());
        }

    }

    // 몇 명의 유저가 방에 참여했는지 카운트
    public int getCurrentUserCount(String roomCode){
        String key = generateKey(roomCode);
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getState() == 1){
                count ++;
            }
        }

        return count;
    }

    // 유저가 방에 입장했을 때 redis 방 정보 최신화
    public void userEnterRoom(String roomCode, UserEntity user){
        String key = generateKey(roomCode);
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getState() == 0){
                seat.setState(1);
                seat.setUserId(user.getId());
                seat.setNickname(user.getNickname());
                seat.setProfileImgUrl(user.getProfileImgUrl());

                roomRedisRepository.reSaveToRedis(key, seat, count);
                return;
            }
            count ++;
        }

    }
    // 현재 방 정보 얻기
    public List<CurrentSeatDto> getCurrentRoomInfo(String roomCode){
        String key = generateKey(roomCode);
        return roomRedisRepository.getCurrentRoomInfo(key);
    }

    // 유저가 방에서 나갔을 때 redis 방 정보 최신화
    public void userExitRoom(String roomCode, UserEntity user){
        String key = generateKey(roomCode);
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        int count = 0;

        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getUserId() == user.getId()){
                seat.setState(0);
                seat.setUserId(0);
                seat.setNickname(null);
                seat.setProfileImgUrl(null);

                roomRedisRepository.reSaveToRedis(key, seat, count);
                return;
            }
            count ++;
        }

    }
}
