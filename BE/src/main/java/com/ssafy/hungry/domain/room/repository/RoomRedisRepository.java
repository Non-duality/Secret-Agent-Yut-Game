package com.ssafy.hungry.domain.room.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomRedisRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String LOBBY_KEY_PREFIX = "LobbyInfo:";

    // redis키 생성하기
    public String generateKey(String roomCode){
        return LOBBY_KEY_PREFIX + " " + roomCode;
    }

    // redis에 저장하기
    public void saveToRedis(String roomCode, CurrentSeatDto currentSeatDto){
        log.info("RoomRedisRepository saveToRedis 호출");

        String key = generateKey(roomCode);

        try {
            String jsonData = objectMapper.writeValueAsString(currentSeatDto);
            redisTemplate.opsForHash().put(key, Integer.toString(currentSeatDto.getSeatNumber()),jsonData);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // 방을 생성 한 후 현재 좌석 정보를 redis에 추가
    public void createCurrentSeat(String roomCode){

        // 팀을 나눠서 저장 0 ~ 2번은 1팀, 3 ~ 5번은 2팀
        for(int i = 0; i < 3; i++){
            saveToRedis(roomCode, CurrentSeatDto.builder()
                    .state(0)
                    .team(1)
                    .seatNumber(i)
                    .build());
        }

        for(int i = 3; i < 6; i++){
            saveToRedis(roomCode, CurrentSeatDto.builder()
                    .state(0)
                    .team(2)
                    .seatNumber(i)
                    .build());
        }

    }

    // Redis에서 Lobby 정보 가져오기
    public List<CurrentSeatDto> getCurrentLobbyInfo(String roomCode){
        String key = generateKey(roomCode);
        List<Object> list = redisTemplate.opsForHash().values(key);
        List<CurrentSeatDto> currentSeatDtoList = null;
        try{
            currentSeatDtoList = objectMapper.readValue(list.toString(), new TypeReference<List<CurrentSeatDto>>(){});
        }catch (Exception e){
            e.printStackTrace();
        }

        return currentSeatDtoList;
    }

    // 몇 명의 유저가 방에 참여했는지 카운트
    public int getCurrentUserCount(String roomCode){

        List<CurrentSeatDto> currentSeatDtoList = getCurrentLobbyInfo(roomCode);

        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getState() == 1){
                count ++;
            }
        }

        return count;
    }

}
