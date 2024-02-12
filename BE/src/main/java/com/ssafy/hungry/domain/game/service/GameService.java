package com.ssafy.hungry.domain.game.service;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.UserInfo;
import com.ssafy.hungry.domain.game.entity.game.*;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamMember;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamStatus;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamUnit;
import com.ssafy.hungry.domain.game.repository.*;
import com.ssafy.hungry.domain.room.dto.CurrentSeatDto;
import com.ssafy.hungry.domain.room.entity.RoomEntity;
import com.ssafy.hungry.domain.room.repository.RoomRedisRepository;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.entity.UserGameHistoryEntity;
import com.ssafy.hungry.domain.user.repository.UserGameHistoryRepository;
import com.ssafy.hungry.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final UserGameHistoryRepository userGameHistoryRepository;
    private final GameRepository gameRepository;
    private final RedTeamMemberRepository redTeamMemberRepository;
    private final GameStatusRepository gameStatusRepository;
    private final RedTeamStatusRepository redTeamStatusRepository;
    private final RedTeamUnitRepository redTeamUnitRepository;


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


    public void gameSaveTest(){
        Game game = new Game();
        game.setGameCode("test1");
        game.setGameTheme("theme");
        game.setGameSpeed(1);
        game.setMissionRegion(this.generateMissionRegion());

        gameRepository.save(game);

        int[] userlist = new int[] {1, 2, 18, 21, 22, 23};

        for(int i = 0; i < 6; i++){
            UserEntity user = userRepository.findById(userlist[i]);
            UserGameHistoryEntity entity = new UserGameHistoryEntity();
            entity.setUser(user);
            entity.setGameCode(gameRepository.findByGameCode("test1"));

            userGameHistoryRepository.save(entity);
        }

        for(int i = 0; i < 3; i++){
            RedTeamMember redTeamMember = new RedTeamMember();
            UserEntity user = userRepository.findById(userlist[i]);
            redTeamMember.setUserId(user);
            redTeamMember.setGameCode(game);
            redTeamMember.setUserIndex(i);
            redTeamMemberRepository.save(redTeamMember);
        }

        GameStatus gameStatus = new GameStatus();
        gameStatus.setId(new GameStatusId(game,1,1));
        gameStatusRepository.save(gameStatus);

        RedTeamStatus redTeamStatus = new RedTeamStatus();
        redTeamStatus.setId(new TeamStatusId(gameStatus));
        redTeamStatus.setSuccessReasoning(true);
        redTeamStatusRepository.save(redTeamStatus);

        RedTeamUnit redTeamUnit = new RedTeamUnit();
        redTeamUnit.setId(new RedTeamUnitId(redTeamStatus, 1));
        redTeamUnit.setPosition(1);
        redTeamUnitRepository.save(redTeamUnit);
    }

    @Transactional
    public void gameSelectTest() {
        System.out.println(gameRepository.findById("test1"));
//        System.out.println(redTeamUnitRepository.findAll());
    }
}
