package com.ssafy.hungry.domain.room.controller;

import com.ssafy.hungry.domain.room.dto.CreateRoomDto;
import com.ssafy.hungry.domain.room.dto.RoomDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.service.RoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    // 방 전체 조회하기
    @GetMapping
    public ResponseEntity<List<RoomDto>> getRoomList(){
        log.info("방 목록 조회 호출");
        List<RoomDto> roomList = roomService.getRoomList();
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    // 방 생성. 방 생성 후 room id를 반환한다.
    @PostMapping("/create")
    public ResponseEntity<Integer> createRoom(@RequestBody CreateRoomDto createRoomDto){
        log.info("방 생성 : " + createRoomDto.getTitle());

        // 방을 개설한 유저 email 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int roomId = roomService.createRoom(createRoomDto, email);

        return new ResponseEntity<>(roomId,HttpStatus.CREATED);
    }
}
