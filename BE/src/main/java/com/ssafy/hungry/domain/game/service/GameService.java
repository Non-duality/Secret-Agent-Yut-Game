package com.ssafy.hungry.domain.game.service;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.UserInfo;
import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.entity.game.Game;
import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamMember;
import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamUnit;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamMember;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamUnit;
import com.ssafy.hungry.domain.game.repository.*;
import com.ssafy.hungry.domain.pick.dto.CurrentUnitPickDto;
import com.ssafy.hungry.domain.pick.repository.PickRedisRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {
    private final RoomRedisRepository roomRedisRepository;
    private final UserRepository userRepository;
    private final UserGameHistoryRepository userGameHistoryRepository;
    private final GameRepository gameRepository;
    private final RedTeamMemberRepository redTeamMemberRepository;
    private final RedTeamUnitRepository redTeamUnitRepository;
    private final BlueTeamMemberRepository blueTeamMemberRepository;
    private final PickRedisRepository pickRedisRepository;
    private final UnitRepository unitRepository;
    private final BlueTeamUnitRepository blueTeamUnitRepository;

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

    //추리 성공시 저장
    public void reasoning(String team, String gameCode){
        Game game = gameRepository.findByGameCode(gameCode);
        if(team.equals("청팀")){
            game.setBlueTeamReasoningResult(true);
            gameRepository.save(game);
        }else{
            game.setRedTeamReasoningResult(true);
            gameRepository.save(game);
        }
    }

    //유닛 도착시 저장
    public void unitGole(String team, int unitIndex, String gameCode){
        Game game = gameRepository.findByGameCode(gameCode);
        if(team.equals("청팀")){
            BlueTeamUnit blueTeamUnit = blueTeamUnitRepository.findByGameCodeAndUnitIndex(game, unitIndex);
            blueTeamUnit.setGole(true);
            blueTeamUnitRepository.save(blueTeamUnit);
        }else{
            RedTeamUnit redTeamUnit = redTeamUnitRepository.findByGameCodeAndUnitIndex(game, unitIndex);
            redTeamUnit.setGole(true);
            redTeamUnitRepository.save(redTeamUnit);
        }
    }

    //게임 종료 시 저장
    public void saveGameResult(String gameCode, int winner){
        Game game = gameRepository.findByGameCode(gameCode);
        game.setWinner(winner);
        gameRepository.save(game);
    }

    //게임 시작 시 저장
    public void initGame(RoomEntity room, int[] missionRegion){
        //룸코드를 바탕으로 게임 생성
        //밀정 정보 받아오기
        Map<Object, Object> spy = pickRedisRepository.getCurrentSpyPickInfo("SpyInfo: " + room.getRoomCode());

        Game game = new Game();
        game.setGameCode(room.getRoomCode());
        game.setGameSpeed(room.getGameSpeed());
        game.setGameTheme(room.getTheme());
        game.setMissionRegion(missionRegion);
        game.setBlueSpyId((Integer) spy.get("청팀"));
        game.setRedSpyId((Integer) spy.get("홍팀"));
        gameRepository.save(game);

        //게임코드와 참여자로 게임 전적 생성
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo("RoomInfo : " + room.getRoomCode());
        for(int i = 0; i < 6; i++){
            UserEntity user = userRepository.findById(currentSeatDtoList.get(i).getUserId());
            UserGameHistoryEntity entity = new UserGameHistoryEntity();
            entity.setUser(user);
            entity.setGameCode(game);
            userGameHistoryRepository.save(entity);
        }

        //홍팀 멤버 저장
        for (int i = 0; i < 3; i++){
            RedTeamMember redTeamMember = new RedTeamMember();
            UserEntity user = userRepository.findById(currentSeatDtoList.get(i).getUserId());
            redTeamMember.setGameCode(game);
            redTeamMember.setUserId(user);
            redTeamMember.setUserIndex(i);
            redTeamMemberRepository.save(redTeamMember);
        }

        //청팀 멤버 저장
        for (int i = 3; i < 6; i++){
            BlueTeamMember blueTeamMember = new BlueTeamMember();
            UserEntity user = userRepository.findById(currentSeatDtoList.get(i).getUserId());
            blueTeamMember.setGameCode(game);
            blueTeamMember.setUserId(user);
            blueTeamMember.setUserIndex(i);
            blueTeamMemberRepository.save(blueTeamMember);
        }

        //유닛 정보 받아오기
        List<CurrentUnitPickDto> blueUnitInfo = pickRedisRepository.getCurrentUnitPickInfo("BlueUnitInfo: " + room.getRoomCode());
        List<CurrentUnitPickDto> redUnitInfo = pickRedisRepository.getCurrentUnitPickInfo("RedUnitInfo: " + room.getRoomCode());

        //청팀 유닛 정보 저장
        for(int i = 0; i < 5; i++){
            UnitEntity unit = unitRepository.findById(blueUnitInfo.get(i).getUnitId());
            BlueTeamUnit blueTeamUnit = new BlueTeamUnit();
            blueTeamUnit.setGameCode(game);
            blueTeamUnit.setUnitId(unit);
            blueTeamUnit.setGole(false);
            blueTeamUnitRepository.save(blueTeamUnit);
        }

        //홍팀 유닛 정보 저장
        for(int i = 0; i < 5; i++){
            UnitEntity unit = unitRepository.findById(redUnitInfo.get(i).getUnitId());
            RedTeamUnit redTeamUnit = new RedTeamUnit();
            redTeamUnit.setGameCode(game);
            redTeamUnit.setUnitId(unit);
            redTeamUnit.setGole(false);
            redTeamUnitRepository.save(redTeamUnit);
        }
    }

    @Transactional
    public void gameSelectTest() {
        System.out.println(gameRepository.findAll());
    }
}
