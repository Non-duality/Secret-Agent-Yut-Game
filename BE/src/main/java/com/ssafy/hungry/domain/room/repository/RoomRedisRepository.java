package com.ssafy.hungry.domain.room.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomRedisRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String LOBBY_KEY_PREFIX = "LobbyInfo:";

    // 방을 생성 한 후 현재 좌석 정보를 redis에 추가
    public void createCurrentSeat(int roomId){

        // 팀을 나눠서 저장 0 ~ 2번은 1팀, 3 ~ 5번은 2팀
        for(int i = 0; i < 3; i++){
            saveToRedis(roomId, CurrentSeatDto.builder()
                    .state(0)
                    .team(1)
                    .seatNumber(i)
                    .build());
        }

        for(int i = 3; i < 6; i++){
            saveToRedis(roomId, CurrentSeatDto.builder()
                    .state(0)
                    .team(2)
                    .seatNumber(i)
                    .build());
        }

    }

    // redis에 저장하기
    public void saveToRedis(int roomId, CurrentSeatDto currentSeatDto){
        log.info("RoomRedisRepository saveToRedis 호출");

        String key = LOBBY_KEY_PREFIX + roomId;

        try {
            String jsonData = objectMapper.writeValueAsString(currentSeatDto);
            redisTemplate.opsForHash().put(key, Integer.toString(currentSeatDto.getSeatNumber()),jsonData);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
