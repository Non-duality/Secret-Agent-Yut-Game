package com.ssafy.hungry.domain.pick.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hungry.domain.pick.dto.CurrentPickDto;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PickRedisRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    // redis에 저장하기
    public void saveToRedis(String key, CurrentPickDto currentPickDto){
        log.info("PickRedisRepository saveToRedis 호출 : " + key);

        try {
            String jsonData = objectMapper.writeValueAsString(currentPickDto);
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
            zSetOps.add(key, jsonData, currentPickDto.getSeatNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // redis에 지우고 다시 저장하기
    public void reSaveToRedis(String key, CurrentPickDto currentPickDto, int seatNumber){
        log.info("PickRedisRepository reSaveToRedis 호출 : " + key);

        try {
            String jsonData = objectMapper.writeValueAsString(currentPickDto);
            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
            zSetOps.removeRange(key, seatNumber, seatNumber);
            zSetOps.add(key, jsonData, currentPickDto.getSeatNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // redis에서  현재 캐릭터 선택 정보 가져오기
    public List<CurrentPickDto> getCurrentPickInfo(String key, int start, int end){

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<String> list = zSetOps.range(key, start, end);
        List<CurrentPickDto> currentPickDtoList = null;

        try{
            currentPickDtoList = objectMapper.readValue(list.toString(), new TypeReference<List<CurrentPickDto>>(){});
        }catch (Exception e){
            e.printStackTrace();
        }

        return currentPickDtoList;
    }
}
