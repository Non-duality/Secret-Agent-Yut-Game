package com.ssafy.hungry.domain.game.service;

import com.ssafy.hungry.domain.game.dto.GameStartDto;
import com.ssafy.hungry.domain.game.dto.UnitGoleDto;
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

    public int[] randomPermutation(){
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        boolean[] v = new boolean[5];
        int[] result = new int[5];

        for(int i = 0; i < 5; i++){
            int temp = sr.nextInt(5);
            if(!v[temp]){
                v[temp] = true;
                result[i] = temp;
            }else{
                i--;
                continue;
            }
        }
        return result;
    }

    public Map<String, Object> startGame(RoomEntity room) {
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        String[] placeList = new String[]{"경복궁", "덕수궁"};
        String[] timeList = new String[]{"축시", "인시"};
        String[] contactorList = new String[] {"경비병", "궁녀"};
        String[] stuffList = new String[]{"쪽지", "지도"};
        String[] scalList = new String[]{"오른쪽 가슴", "왼쪽 허벅지", "손목", "등", "종아리"};
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
        int[] randomPer = this.randomPermutation();
        List<CurrentUnitPickDto> redTeamUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo("RedUnitInfo: " + roomCode);
        List<UnitInfo> redUnitList = new ArrayList<>();
        int i = 0;
        for(CurrentUnitPickDto unit : redTeamUnitPickInfo){
            UnitInfo unitInfo = UnitInfo.builder()
                    .unitId(unit.getUnitId())
                    .name(unit.getName())
                    .place(placeList[sr.nextInt(2)])
                    .time(timeList[sr.nextInt(2)])
                    .contactor(contactorList[sr.nextInt(2)])
                    .stuff(stuffList[sr.nextInt(2)])
                    .scal(scalList[randomPer[i]])
                    .skill(unit.getSkill())
                    .build();
            redUnitList.add(unitInfo);
            i++;
        }

        // 청팀 유저들의 유닛 선택창 정보
        randomPer = this.randomPermutation();
        List<CurrentUnitPickDto> blueTeamUnitPickInfo = pickRedisRepository.getCurrentUnitPickInfo("BlueUnitInfo: " + roomCode);
        List<UnitInfo> blueUnitList = new ArrayList<>();
        i = 0;
        for(CurrentUnitPickDto unit : blueTeamUnitPickInfo){
            UnitInfo unitInfo = UnitInfo.builder()
                    .unitId(unit.getUnitId())
                    .name(unit.getName())
                    .place(placeList[sr.nextInt(2)])
                    .time(timeList[sr.nextInt(2)])
                    .contactor(contactorList[sr.nextInt(2)])
                    .stuff(stuffList[sr.nextInt(2)])
                    .scal(scalList[randomPer[i]])
                    .skill(unit.getSkill())
                    .build();
            blueUnitList.add(unitInfo);
            i++;
        }

        Map<Object,Object> spyPickInfo = pickRedisRepository.getCurrentSpyPickInfo("SpyInfo: " + roomCode);

        UnitInfo redSpyInfo = redUnitList.get(Integer.parseInt( String.valueOf(spyPickInfo.get("홍팀"))));
        UnitInfo blueSpyInfo = blueUnitList.get(Integer.parseInt( String.valueOf(spyPickInfo.get("청팀"))));
        String redSpyHint = "밀정은 " + redSpyInfo.getTime() + "에 " + redSpyInfo.getPlace() + "에서 " + redSpyInfo.getContactor() + "을(를) 만나 " + redSpyInfo.getStuff() + "을 전달받았습니다. " +
                "그리고 밀정은 " + redSpyInfo.getScal() + "에 흉터가 있습니다.";
        String blueSpyHint = "밀정은 " + blueSpyInfo.getTime() + "에 " + blueSpyInfo.getPlace() + "에서 " + blueSpyInfo.getContactor() + "을(를) 만나 " + blueSpyInfo.getStuff() + "을 전달받았습니다." +
                "그리고 밀정은 " + blueSpyInfo.getScal() + "에 흉터가 있습니다.";;

        int[] missionRegion = this.generateMissionRegion();

        //게임시작 dto 에 담아줄 정보 주가
        GameStartDto redGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(missionRegion)
                .gameSpeed(room.getGameSpeed())
                .gameTheme(room.getTheme())
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId(Integer.parseInt( String.valueOf(spyPickInfo.get("청팀"))))
                .mySpyHint(blueSpyHint)
                .build();

        GameStartDto blueGameDto = GameStartDto.builder()
                .actionCategory(0)
                .missionRegion(missionRegion)
                .gameSpeed(room.getGameSpeed())
                .gameTheme(room.getTheme())
                .redTeamUserList(redTeamUserList)
                .blueTeamUserList(blueTeamUserList)
                .redTeamUnitList(redUnitList)
                .blueTeamUnitList(blueUnitList)
                .mySpyUnitId(Integer.parseInt( String.valueOf(spyPickInfo.get("홍팀"))))
                .mySpyHint(redSpyHint)
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("홍팀", redGameDto);
        result.put("청팀", blueGameDto);

        this.initGame(room, missionRegion, redUnitList, blueUnitList, blueSpyHint, redSpyHint);

        return result;
    }

    //추리 성공시 저장
    public void reasoning(int team, String gameCode){
        Game game = gameRepository.findByGameCode(gameCode);
        if(team == 2){
            game.setBlueTeamReasoningResult(true);
            gameRepository.save(game);
        }else{
            game.setRedTeamReasoningResult(true);
            gameRepository.save(game);
        }
    }

    //유닛 도착시 저장
    public UnitGoleDto unitGole(String gameCode, UnitGoleDto dto){
        Game game = gameRepository.findByGameCode(gameCode);
        if(dto.getTeam() == 2){
            for(int i = 0; i < dto.getUnitId().length; i++){
                BlueTeamUnit blueTeamUnit = blueTeamUnitRepository.findByGameCodeAndUnitId(game, unitRepository.findById(dto.getUnitId()[i]));
                if(this.isSpy(blueTeamUnit.getId(), 2, gameCode)){
                    dto.setSpy(true);
                    dto.setSpyId(blueTeamUnit.getId());
                }
                blueTeamUnit.setGole(true);
                blueTeamUnitRepository.save(blueTeamUnit);
                return dto;
            }
        }else{
            for(int i = 0; i < dto.getUnitId().length; i++){
                RedTeamUnit redTeamUnit = redTeamUnitRepository.findByGameCodeAndUnitId(game, unitRepository.findById(dto.getUnitId()[i]));
                if(this.isSpy(redTeamUnit.getId(), 2, gameCode)){
                    dto.setSpy(true);
                    dto.setSpyId(redTeamUnit.getId());
                }
                redTeamUnit.setGole(true);
                redTeamUnitRepository.save(redTeamUnit);
                return dto;
            }
        }
        return null;
    }

    //게임 종료 시 저장
    public void saveGameResult(String gameCode, int winner){
        Game game = gameRepository.findByGameCode(gameCode);
        game.setWinner(winner);
        gameRepository.save(game);
    }

    //게임 시작 시 저장
    public void initGame(RoomEntity room, int[] missionRegion , List<UnitInfo> redUnitList, List<UnitInfo> blueUnitList, String blueSpyHint, String redSpyHint){
        //룸코드를 바탕으로 게임 생성
        //밀정 정보 받아오기
        Map<Object, Object> spy = pickRedisRepository.getCurrentSpyPickInfo("SpyInfo: " + room.getRoomCode());

        Game game = new Game();
        game.setGameCode(room.getRoomCode());
        game.setGameSpeed(room.getGameSpeed());
        game.setGameTheme(room.getTheme());
        game.setMissionRegion(missionRegion);
        game.setBlueSpyId(Integer.parseInt( String.valueOf(spy.get("청팀"))));
        game.setRedSpyId(Integer.parseInt( String.valueOf(spy.get("홍팀"))));
        game.setBlueSpyHint(blueSpyHint);
        game.setRedSpyHint(redSpyHint);
        gameRepository.save(game);

        //게임코드와 참여자로 게임 전적 생성
        List<CurrentSeatDto> currentSeatDtoList = roomRedisRepository.getCurrentRoomInfo("RoomInfo: " + room.getRoomCode());
        System.out.println(currentSeatDtoList.size());
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
        int i = 0;
        //청팀 유닛 정보 저장
        for(UnitInfo unitInfo : blueUnitList){
            UnitEntity unit = unitRepository.findById(unitInfo.getUnitId());
            BlueTeamUnit blueTeamUnit = new BlueTeamUnit();
            blueTeamUnit.setGameCode(game);
            blueTeamUnit.setUnitId(unit);
            blueTeamUnit.setUnitIndex(i);
            blueTeamUnit.setPlace(unitInfo.getPlace());
            blueTeamUnit.setTime(unitInfo.getTime());
            blueTeamUnit.setContactor(unitInfo.getContactor());
            blueTeamUnit.setStuff(unitInfo.getStuff());
            blueTeamUnit.setScal(unitInfo.getScal());
            blueTeamUnit.setGole(false);
            blueTeamUnitRepository.save(blueTeamUnit);
            i++;
        }

        i = 0;
        //홍팀 유닛 정보 저장
        for(UnitInfo unitInfo : redUnitList){
            UnitEntity unit = unitRepository.findById(unitInfo.getUnitId());
            RedTeamUnit redTeamUnit = new RedTeamUnit();
            redTeamUnit.setGameCode(game);
            redTeamUnit.setUnitId(unit);
            redTeamUnit.setUnitIndex(i);
            redTeamUnit.setPlace(unitInfo.getPlace());
            redTeamUnit.setTime(unitInfo.getTime());
            redTeamUnit.setContactor(unitInfo.getContactor());
            redTeamUnit.setStuff(unitInfo.getStuff());
            redTeamUnit.setScal(unitInfo.getScal());
            redTeamUnit.setGole(false);
            redTeamUnitRepository.save(redTeamUnit);
            i++;
        }
    }

    @Transactional
    public void gameSelectTest() {
        System.out.println(gameRepository.findAll());
    }

    public Map<String, Object> startGameDummy() {
//        RoomEntity room = roomRepository.findByRoomCode("720ca5");
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        String[] placeList = new String[]{"경복국", "덕수궁"};
        String[] timeList = new String[]{"축시", "인시"};
        String[] contactorList = new String[] {"경비병", "궁녀"};
        String[] stuffList = new String[]{"쪽지", "지도"};
        String[] scalList = new String[]{"오른쪽 가슴", "왼쪽 허벅지", "손목", "등", "종아리"};
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

        int[] randomPer = this.randomPermutation();
        // 홍팀 유저들의 유닛 선택창 정보
        List<UnitInfo> redUnitList = new ArrayList<>();
        UnitInfo unitInfo1 = UnitInfo.builder()
                .unitId(1)
                .name("대왕")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[0]])
                .skill("밀정 선택 불가")
                .build();
        redUnitList.add(unitInfo1);

        UnitInfo unitInfo2 = UnitInfo.builder()
                .unitId(2)
                .name("창병")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[1]])
                .skill("앞 뒤 상대말 1턴간 이동 불가")
                .build();
        redUnitList.add(unitInfo2);

        UnitInfo unitInfo3 = UnitInfo.builder()
                .unitId(3)
                .name("기병")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[2]])
                .skill("이동 수 +1")
                .build();
        redUnitList.add(unitInfo3);

        UnitInfo unitInfo4 = UnitInfo.builder()
                .unitId(4)
                .name("농민")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[3]])
                .skill("아무런 능력이 없음")
                .build();
        redUnitList.add(unitInfo4);

        UnitInfo unitInfo5 = UnitInfo.builder()
                .unitId(5)
                .name("노비")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[4]])
                .skill("이동 수 -1")
                .build();
        redUnitList.add(unitInfo5);

        // 청팀 유저들의 유닛 선택창 정보
        randomPer = this.randomPermutation();
        List<UnitInfo> blueUnitList = new ArrayList<>();

        UnitInfo unitInfo6 = UnitInfo.builder()
                .unitId(1)
                .name("대왕")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[0]])
                .skill("밀정 선택 불가")
                .build();
        blueUnitList.add(unitInfo6);

        UnitInfo unitInfo7 = UnitInfo.builder()
                .unitId(2)
                .name("창병")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[1]])
                .skill("앞 뒤 상대말 1턴간 이동 불가")
                .build();
        blueUnitList.add(unitInfo7);

        UnitInfo unitInfo8 = UnitInfo.builder()
                .unitId(3)
                .name("기병")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[2]])
                .skill("이동 수 +1")
                .build();
        blueUnitList.add(unitInfo8);

        UnitInfo unitInfo9 = UnitInfo.builder()
                .unitId(4)
                .name("농민")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[3]])
                .skill("아무런 능력이 없음")
                .build();
        blueUnitList.add(unitInfo9);

        UnitInfo unitInfo10 = UnitInfo.builder()
                .unitId(5)
                .name("노비")
                .place(placeList[sr.nextInt(2)])
                .time(timeList[sr.nextInt(2)])
                .contactor(contactorList[sr.nextInt(2)])
                .stuff(stuffList[sr.nextInt(2)])
                .scal(scalList[randomPer[4]])
                .skill("이동 수 -1")
                .build();
        blueUnitList.add(unitInfo10);

        UnitInfo redSpyInfo = unitInfo2;
        UnitInfo blueSpyInfo = unitInfo7;
        String redSpyHint = "밀정은 " + redSpyInfo.getTime() + "에 " + redSpyInfo.getPlace() + "에서 " + redSpyInfo.getContactor() + "을(를) 만나 " + redSpyInfo.getStuff() + "을 전달받았습니다. " +
                "그리고 밀정은 " + redSpyInfo.getScal() + "에 흉터가 있습니다.";
        String blueSpyHint = "밀정은 " + blueSpyInfo.getTime() + "에 " + blueSpyInfo.getPlace() + "에서 " + blueSpyInfo.getContactor() + "을(를) 만나 " + blueSpyInfo.getStuff() + "을 전달받았습니다." +
                "그리고 밀정은 " + blueSpyInfo.getScal() + "에 흉터가 있습니다.";;

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
                .mySpyHint(redSpyHint)
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
                .mySpyHint(blueSpyHint)
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("홍팀", redGameDto);
        result.put("청팀", blueGameDto);

        return result;
    }


    public boolean isSpy(int unitId, int team, String roomCode) {
        Map<Object,Object> spyPickInfo = pickRedisRepository.getCurrentSpyPickInfo("SpyInfo: " + roomCode);

        if(team == 1){
            if(unitId == Integer.parseInt(String.valueOf(spyPickInfo.get("홍팀")))){
                this.reasoning(team, roomCode);
                return true;
            }else {
                return false;
            }
        }else{
            if(unitId == Integer.parseInt(String.valueOf(spyPickInfo.get("청팀")))){
                this.reasoning(team, roomCode);
                return true;
            }else {
                return false;
            }
        }
    }
}
