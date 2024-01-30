package com.ssafy.hungry.domain.room.service;

import com.ssafy.hungry.domain.room.dto.CreateRoomDto;
import com.ssafy.hungry.domain.room.dto.RoomDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import com.ssafy.hungry.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {


    private final UserService userService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    // 방 비밀번호를 암호화 시키기 위한 객체
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<RoomDto> getRoomList() {
        log.info("RoomService getRoomList 호출");

        List<RoomEntity> roomEntityList = roomRepository.findAllByEndAtIsNullOrderByStartAt();
        List<RoomDto> roomDtoList = new ArrayList<>();

        for(RoomEntity roomEntity : roomEntityList){
            roomDtoList.add(RoomDto.builder()
                            .roomId(roomEntity.getRoomId())
                            // Todo : redis로 부터 현재 유저 정보 받아오기
                            .currentUserCount(0)
                            .title(roomEntity.getTitle())
                            .isPublic(roomEntity.isPublic())
                            .build());
        }

        return roomDtoList;
    }

    public int createRoom(CreateRoomDto createRoomDto, String email){
        log.info("RoomService createRoom 호출 : " + createRoomDto);


        UserEntity user = userRepository.findByEmail(email);

        RoomEntity roomEntity = RoomEntity.builder()
                .owner(user)
                .title(createRoomDto.getTitle())
                .theme(createRoomDto.getTheme())
                .gameSpeed(createRoomDto.getSpeed())
                .isPublic(createRoomDto.isPublic())
                .build();

        // room이 비공개라면 password를 암호화 시키기
        if(!createRoomDto.isPublic() && createRoomDto.getPassword() != null){
            roomEntity.setPassword(bCryptPasswordEncoder.encode(createRoomDto.getPassword()));
        }

        // rooms 테이블 저장
        roomRepository.save(roomEntity);

        // 만들어진 room id 반환
        int roomId = (roomRepository.findRoomByEndAtIsNullAndOwnerId(user.getId())).getRoomId();

        // redis에 만들어진 방에 현재 좌석 정보를 만들기
        roomRedisRepository.createCurrentSeat(roomId);

        // 만들어진 room id 반환
        return roomId;
    }
}
