package com.ssafy.hungry.domain.game.service;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.UserInfo;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final RoomRedisRepository roomRedisRepository;
    private final UserRepository userRepository;

    public int[] generateMissionRegion() {
        // 0 5 10 15 22 27 을 제외하고 0~29까지중 랜덤으로 4개를 선택
        int[] missionRigion = new int[4];
        int[][] map = new int[][] {{1,2,3,4}, {6,7,8,9}, {11,12,13,14}, {16,17,18,19}};
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        for(int i = 0; i < 4; i++){
            missionRigion[i] = map[i][sr.nextInt(4)];
        }
        return missionRigion;
    }

    public GameStartDto startGame(RoomEntity room, String gameCode) {
        GameStartDto dto = new GameStartDto();
        //레디스에서 참여자 목록을 받아옴
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo("RoomInfo : " + gameCode);
        System.out.println(currentSeatDtoList);
        //게임시작dto 에 담아줄 유저 리스트를 생성
        List<UserInfo> redTeamList = new ArrayList<>();
        List<UserInfo> blueTeamList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(userRepository.findById(currentSeatDtoList.get(i).getUserId()).getEmail());
            userInfo.setNickname(currentSeatDtoList.get(i).getNickname());
            userInfo.setProfileImgUrl(currentSeatDtoList.get(i).getProfileImgUrl());
            redTeamList.add(userInfo);
        }
        for (int i = 3; i < 6; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setEmail(userRepository.findById(currentSeatDtoList.get(i).getUserId()).getEmail());
            userInfo.setNickname(currentSeatDtoList.get(i).getNickname());
            userInfo.setProfileImgUrl(currentSeatDtoList.get(i).getProfileImgUrl());
            blueTeamList.add(userInfo);
        }
        //게임시작 dto 에 담아줄 정보 주가
        dto.setGameCode(gameCode);
        dto.setGameSpeed(room.getGameSpeed());
        dto.setGameTheme(room.getTheme());
        dto.setMissionRegion(this.generateMissionRegion());
        dto.setRedTeamList(redTeamList);
        dto.setRedTeamList(blueTeamList);
        dto.setGameTurn(0);
        return dto;
    }
}
