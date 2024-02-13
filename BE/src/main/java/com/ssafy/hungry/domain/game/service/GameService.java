package com.ssafy.hungry.domain.game.service;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.UnitInfo;
import com.ssafy.hungry.domain.game.dto.UserInfo;
import com.ssafy.hungry.domain.game.entity.UnitEntity;
import com.ssafy.hungry.domain.game.entity.game.Game;
import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamMember;
import com.ssafy.hungry.domain.game.entity.game.blue.BlueTeamUnit;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamMember;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamUnit;
import com.ssafy.hungry.domain.game.repository.*;
import com.ssafy.hungry.domain.pick.dto.CurrentUnitPickDto;
import com.ssafy.hungry.domain.pick.dto.CurrentUserPickDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final RoomRedisRepository roomRedisRepository;
    private final PickRedisRepository pickRedisRepository;
    private final UserRepository userRepository;
    private final UserGameHistoryRepository userGameHistoryRepository;
    private final GameRepository gameRepository;
    private final RedTeamMemberRepository redTeamMemberRepository;
    private final RedTeamUnitRepository redTeamUnitRepository;
    private final GameRedisRepository gameRedisRepository;
    private final BlueTeamMemberRepository blueTeamMemberRepository;
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

    // 유저가 들어올 때 마다 GameEnterInfo에 유저 정보 저장하기
    public void updateEnterUserInfo(String roomCode, UserEntity user){
        gameRedisRepository.saveUserEnterInfo("UserEnterInfo: " + roomCode, user);
    }

    // 유저가 전부 들어왔는지 확인하기
    public int getUserEnterCountInfo(String roomCode){
        Map<Object, Object> userEnterInfo =gameRedisRepository.getCurrentUserInfo("UserEnterInfo: " + roomCode);
        return userEnterInfo.size();
    }

    public Map<String, Object> startGame(RoomEntity room) {
        String roomCode = room.getRoomCode();

        //레디스에서 참여자 목록을 받아옴
        List<CurrentUserPickDto> currentUserPickDtoList = pickRedisRepository.getCurrentUserPickInfo("UserPickInfo: " + roomCode,0,-1);
        log.info("gameStart currentUserPickDtoList : " + currentUserPickDtoList.toString());

        //게임시작 dto 에 담아줄 유저 리스트를 생성
        List<UserInfo> redTeamUserList = new ArrayList<>();
        List<UserInfo> blueTeamUserList = new ArrayList<>();

        // 유저 리스트 담기
        for (int i = 0; i < 3; i++){
            CurrentUserPickDto user = currentUserPickDtoList.get(i);
            UserInfo userInfo = UserInfo.builder()
                    .email(userRepository.findById(user.getUserId()).getEmail())
                    .nickname(user.getNickname())
                    .profileImgUrl("")
                    .build();
            redTeamUserList.add(userInfo);
        }

        for (int i = 3; i < 6; i++){
            CurrentUserPickDto user = currentUserPickDtoList.get(i);
            UserInfo userInfo = UserInfo.builder()
                    .email(userRepository.findById(user.getUserId()).getEmail())
                    .nickname(user.getNickname())
                    .profileImgUrl("")
                    .build();
            blueTeamUserList.add(userInfo);
        }

        // 홍팀 유저들의 유닛 선택창 정보
        List<CurrentUnitPickDto> redTeamUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo("RedUnitInfo: " + roomCode);
        List<UnitInfo> redUnitList = new ArrayList<>();
        for(CurrentUnitPickDto unit : redTeamUnitPickInfo){
            UnitInfo unitInfo = UnitInfo.builder()
                    .unitId(unit.getUnitId())
                    .name(unit.getName())
                    .age(unit.getAge())
                    .skill(unit.getSkill())
                    .build();
            redUnitList.add(unitInfo);
        }

        // 청팀 유저들의 유닛 선택창 정보
        List<CurrentUnitPickDto> blueTeamUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo("BlueUnitInfo: " + roomCode);
        List<UnitInfo> blueUnitList = new ArrayList<>();
        for(CurrentUnitPickDto unit : blueTeamUnitPickInfo){
            UnitInfo unitInfo = UnitInfo.builder()
                    .unitId(unit.getUnitId())
                    .name(unit.getName())
                    .age(unit.getAge())
                    .skill(unit.getSkill())
                    .build();
            blueUnitList.add(unitInfo);
        }

        Map<Object,Object> spyPickInfo = pickRedisRepository.getCurrentSpyPickInfo("SpyInfo: " + roomCode);

        //게임시작 dto 에 담아줄 정보 주가
        GameStartDto redGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(this.generateMissionRegion())
                .gameSpeed(room.getGameSpeed())
                .gameTheme(room.getTheme())
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId((int)spyPickInfo.get("청팀"))
                .build();

        GameStartDto blueGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(this.generateMissionRegion())
                .gameSpeed(room.getGameSpeed())
                .gameTheme(room.getTheme())
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId((int)spyPickInfo.get("홍팀"))
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("홍팀", redGameDto);
        result.put("청팀", blueGameDto);

        return result;
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

    public Map<String, Object> startGameDummy() {
//        RoomEntity room = roomRepository.findByRoomCode("720ca5");

        //게임시작 dto 에 담아줄 유저 리스트를 생성
        List<UserInfo> redTeamUserList = new ArrayList<>();
        List<UserInfo> blueTeamUserList = new ArrayList<>();

        UserInfo userInfo1 = UserInfo.builder()
                .email("1")
                .nickname("테스트1")
                .profileImgUrl("")
                .build();
        redTeamUserList.add(userInfo1);

        UserInfo userInfo2 = UserInfo.builder()
                .email("2")
                .nickname("테스트2")
                .profileImgUrl("")
                .build();
        redTeamUserList.add(userInfo2);

        UserInfo userInfo3 = UserInfo.builder()
                .email("3")
                .nickname("테스트3")
                .profileImgUrl("")
                .build();
        redTeamUserList.add(userInfo3);

        UserInfo userInfo4 = UserInfo.builder()
                .email("4")
                .nickname("테스트5")
                .profileImgUrl("")
                .build();
        blueTeamUserList.add(userInfo4);

        UserInfo userInfo5 = UserInfo.builder()
                .email("5")
                .nickname("테스트5")
                .profileImgUrl("")
                .build();
        blueTeamUserList.add(userInfo5);

        UserInfo userInfo6 = UserInfo.builder()
                .email("123")
                .nickname("이희웅")
                .profileImgUrl("")
                .build();
        blueTeamUserList.add(userInfo6);


        // 홍팀 유저들의 유닛 선택창 정보
        List<UnitInfo> redUnitList = new ArrayList<>();
        UnitInfo unitInfo1 = UnitInfo.builder()
                .unitId(1)
                .name("대왕")
                .age(67)
                .skill("밀정 선택 불가")
                .build();
        redUnitList.add(unitInfo1);

        UnitInfo unitInfo2 = UnitInfo.builder()
                .unitId(2)
                .name("창병")
                .age(32)
                .skill("앞 뒤 상대말 1턴간 이동 불가")
                .build();
        redUnitList.add(unitInfo2);

        UnitInfo unitInfo3 = UnitInfo.builder()
                .unitId(3)
                .name("기병")
                .age(46)
                .skill("이동 수 +1")
                .build();
        redUnitList.add(unitInfo3);

        UnitInfo unitInfo4 = UnitInfo.builder()
                .unitId(4)
                .name("농민")
                .age(24)
                .skill("아무런 능력이 없음")
                .build();
        redUnitList.add(unitInfo4);

        UnitInfo unitInfo5 = UnitInfo.builder()
                .unitId(5)
                .name("노비")
                .age(29)
                .skill("이동 수 -1")
                .build();
        redUnitList.add(unitInfo5);

        // 청팀 유저들의 유닛 선택창 정보
        List<UnitInfo> blueUnitList = new ArrayList<>();

        UnitInfo unitInfo6 = UnitInfo.builder()
                .unitId(1)
                .name("대왕")
                .age(67)
                .skill("밀정 선택 불가")
                .build();
        blueUnitList.add(unitInfo6);

        UnitInfo unitInfo7 = UnitInfo.builder()
                .unitId(2)
                .name("창병")
                .age(32)
                .skill("앞 뒤 상대말 1턴간 이동 불가")
                .build();
        blueUnitList.add(unitInfo7);

        UnitInfo unitInfo8 = UnitInfo.builder()
                .unitId(3)
                .name("기병")
                .age(46)
                .skill("이동 수 +1")
                .build();
        blueUnitList.add(unitInfo8);

        UnitInfo unitInfo9 = UnitInfo.builder()
                .unitId(4)
                .name("농민")
                .age(24)
                .skill("아무런 능력이 없음")
                .build();
        blueUnitList.add(unitInfo9);

        UnitInfo unitInfo10 = UnitInfo.builder()
                .unitId(5)
                .name("노비")
                .age(29)
                .skill("이동 수 -1")
                .build();
        blueUnitList.add(unitInfo10);

        //게임시작 dto 에 담아줄 정보 주가
        GameStartDto redGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(this.generateMissionRegion())
                .gameSpeed(0)
                .gameTheme("설날")
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId(2)
                .build();

        GameStartDto blueGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(this.generateMissionRegion())
                .gameSpeed(0)
                .gameTheme("설날")
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId(2)
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("홍팀", redGameDto);
        result.put("청팀", blueGameDto);

        return result;
    }
}
