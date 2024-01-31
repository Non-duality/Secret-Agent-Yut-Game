package com.ssafy.hungry.domain.room.service;

import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomRedisService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;

    // 현재 방에 참여한 인원 구하기
    public int getCurrentUserCount(int roomId){
        String roomCode = roomRepository.findByRoomId(roomId);
        return roomRedisRepository.getCurrentUserCount(roomCode);
    };
}
