package com.ssafy.hungry.domain.room.service;

import com.ssafy.hungry.domain.pick.service.PickRedisService;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import com.ssafy.hungry.domain.room.dto.RoomDetailDto;
import com.ssafy.hungry.domain.room.dto.RoomLobbyInfoDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.exception.AllUsersNotReadyException;
import com.ssafy.hungry.domain.room.exception.OwnerValidationException;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.room.repository.RoomRepository;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomRedisService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final PickRedisService pickRedisService;
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
    // 현재 방 정보 얻기
    public List<CurrentSeatDto> getCurrentRoomInfo(String roomCode){
        String key = generateKey(roomCode);
        return roomRedisRepository.getCurrentRoomInfo(key);
    }

    // 몇 명의 유저가 방에 참여했는지 카운트
    public int getCurrentUserCount(String roomCode){
        String key = generateKey(roomCode);
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getState() != 0){
                count ++;
            }
        }

        return count;
    }

    // 유저가 방에 입장했을 때 redis 방 정보 최신화 후 최신화 된 dto 전달
    public RoomLobbyInfoDto userEnterRoom(RoomEntity room, UserEntity user){
        String key = generateKey(room.getRoomCode());
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        // 일반 유저라면 state가 1
        int userState = 1;

        // 방장이라면 state가 2
        if(user.getId() == room.getOwner().getId()){
            userState = 2;
        }

        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getState() == 0){
                seat.setState(userState);
                seat.setUserId(user.getId());
                seat.setNickname(user.getNickname());
                seat.setProfileImgUrl(user.getProfileImgUrl());

                roomRedisRepository.reSaveToRedis(key, seat, count);
                break;
            }
            count ++;
        }

        // 방에 전달할 최신화된 정보 Dto
        RoomLobbyInfoDto roomLobbyInfoDto =RoomLobbyInfoDto.builder()
                .ownerId(room.getOwner().getId())
                .currentSeatDtoList(getCurrentRoomInfo(room.getRoomCode()))
                .roomDetailDto(RoomDetailDto.builder()
                        .title(room.getTitle())
                        .isPublic(room.isPublic())
                        .gameSpeed(room.getGameSpeed())
                        .currentUserCount(getCurrentUserCount(room.getRoomCode()))
                        .theme(room.getTheme())
                        .build())
                .message(user.getNickname() + "님이 입장하였습니다.")
                .roomState(0)
                .build();

        return  roomLobbyInfoDto;
    }

    // 유저가 방에서 나갔을 때 redis 방 정보 최신화 후 최신화 된 dto 전달
    // 나가기를 누른 유저가 방장일 경우 방 삭제
    @Transactional
    public RoomLobbyInfoDto userExitRoom(RoomEntity room, UserEntity user){
        String key = generateKey(room.getRoomCode());
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        String exitMessage = "";
        int roomState = 0;
        // 방 나가기를 누른 사람이 방장일 경우 방을 삭제
        if(user.getId() == room.getOwner().getId()){

            // redis 방을 키값을 이용해 삭제
            roomRedisRepository.deleteToRedis(key);
            // rooms 테이블에 해당 방의 end_At 설정하여 방 삭제
            room.setEndAt(LocalDateTime.now());
            roomRepository.save(room);
            // 해당 방 구독자들에게 방이 삭제되었습니다. 메세지 보내기
            exitMessage = "방이 삭제되었습니다.";
            roomState = 1;
        }

        // 방장이 아닌 유저가 나갈 경우
        else{
            int count = 0;

            for(CurrentSeatDto seat : currentSeatDtoList){
                if(seat.getUserId() == user.getId()){
                    seat.setState(0);
                    seat.setUserId(0);
                    seat.setNickname(null);
                    seat.setProfileImgUrl(null);

                    roomRedisRepository.reSaveToRedis(key, seat, count);
                    break;
                }
                count ++;
            }

            exitMessage = user.getNickname() + "님이 퇴장하였습니다.";
        }

        // 방에 전달할 최신화된 정보 Dto
        RoomLobbyInfoDto roomLobbyInfoDto =RoomLobbyInfoDto.builder()
                .ownerId(room.getOwner().getId())
                .currentSeatDtoList(getCurrentRoomInfo(room.getRoomCode()))
                .roomDetailDto(RoomDetailDto.builder()
                        .title(room.getTitle())
                        .isPublic(room.isPublic())
                        .gameSpeed(room.getGameSpeed())
                        .currentUserCount(getCurrentUserCount(room.getRoomCode()))
                        .theme(room.getTheme())
                        .build())
                .message(exitMessage)
                .roomState(roomState)
                .build();

        return  roomLobbyInfoDto;
    }

    // 유저의 준비 완료, 준비 취소 최신화
    public List<CurrentSeatDto> userReadyRoom(String roomCode, int userId){
        String key = generateKey(roomCode);

        // 현재의 방 정보 들고오기
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);

        // 현재 ready한 인원과 일치하는 좌석 정보 찾고 최신화 시키기
        int count = 0;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(seat.getUserId() == userId){
                seat.setReady(!seat.isReady());
                roomRedisRepository.reSaveToRedis(key, seat, count);
                break;
            }
            count++;
        }

        return currentSeatDtoList;
    }

    // 준비 완료가 되면 게임 시작
    public void enterPickRoom(RoomEntity room, UserEntity user){
        String key = generateKey(room.getRoomCode());
        // START를 보낸 사용자가 owner인지 확인
        if(user.getId() != room.getOwner().getId()){
            throw new OwnerValidationException("방장이 아닙니다.");
        }

        // 모든 사용자가 ready 상태인지 확인
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo(key);
        boolean isAllReady = true;
        for(CurrentSeatDto seat : currentSeatDtoList){
            if(!(seat.getState() == 2) && !seat.isReady()){
                isAllReady = false;
            }
        }

//        if(!isAllReady){
//            throw new AllUsersNotReadyException("모든 유저가 준비완료 되지 않았습니다.");
//        }

        // 조건을 다 충족했다면 레디스에 픽 정보를 담을 수 있는 sorted set 데이터 생성
        pickRedisService.createCurrentUserPick(room.getRoomCode());
        pickRedisService.createCurrentUnitPick(room.getRoomCode());
    }


}
