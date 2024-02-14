<template>
  <div class="game-board">
    <!-- 여기에 추리 말 선택 모달이 나와야한다. -->
    <GameSpySelect v-if="reasoningChoose" />
    <!-- 방향 전환 시 모달로 선택 -->
    <GameModal
      v-if="isShowReasoning || isShowGoDig"
      :modalType="modalType"
      @trueMethod="goDigYes"
      @falseMethod="goDigNo"
      @useTicket="useTicket"
      @notUseTicket="notUseTicket"
    />
    <!-- 차례 메시지  -->
    <div v-if="isShowTurnMessage" class="game-board-turn-message-container">
      <span class="game-board-turn-message">{{ turnMessage }}</span>
    </div>

    <!-- 타이머 -->
    <div class="game-board-timer-container">
      <span
        class="game-board-timer"
        :class="{ 'game-board-timer-five': timerCheck <= 5 }"
        >{{ timerCheck }}</span
      >
    </div>
    <!-- 팀당 다음차례 -->
    <div class="game-board-team-turn">
      <div class="game-board-team-turn-container">
        <span class="game-board-team-turn-title">현재 차례</span>
        <span
          :class="{
            'game-board-team-turn-red': !teamTurn,
            'game-board-team-turn-blue': teamTurn,
          }"
          >{{ nowTunrName }}</span
        >
      </div>
      <div class="game-board-team-turn-container">
        <span class="game-board-team-turn-title">다음 차례</span>
        <span
          :class="{
            'game-board-team-turn-red': teamTurn,
            'game-board-team-turn-blue': !teamTurn,
          }"
          >{{ nextTurnName }}</span
        >
      </div>
    </div>
    <div class="game-board-tile">
      <!-- 윷 던진 결과 -->
      <div class="game-yut-text-div" v-show="isShowResText">
        <span class="game-yut-res-text">{{ yutText }}</span>
      </div>
      <div class="game-yut-res" v-show="isShowRes">
        <GameYut ref="yutThrow" />
      </div>
      <!-- 윷판 -->
      <GameBoardTile v-for="(tile, index) in tiles" :key="index" :tile="tile" />
    </div>
    <!-- 홍팀 말 -->
    <GameHorse
      v-for="horse in redHorses"
      :key="horse.id"
      :horse="horse"
      @selectHorse="selectHorse"
    />
    <!-- 청팀 말 -->
    <GameHorse
      v-for="horse in blueHorses"
      :key="horse.id"
      :horse="horse"
      @selectHorse="selectHorse"
    />

    <!-- 버튼 임시 -->
    <button
      class="game-board-throw-btn"
      @click="moveHorse"
      :disabled="!isThrowYut"
    >
      던지기
    </button>
  </div>
</template>
                                                                        
<script>
import { useGameStore } from "@/store/gameStore";
import { useUserStore } from "@/store/userStore";
import { connect, socketSend } from "@/util/socket.js";
import GameBoardTile from "./item/GameBoardTile.vue";
import GameHorse from "./item/GameHorse.vue";
import GameYut from "./item/GameYut.vue";
import GameModal from "./item/GameModal.vue";
import GameSpySelect from "./item/GameSpySelect.vue";

export default {
  components: {
    GameBoardTile,
    GameHorse,
    GameYut,
    GameModal,
    GameSpySelect,
  },
  mounted() {
    // 새로고침 방지 이벤트를 추가한다.
    window.addEventListener("beforeunload", this.leave);
    this.connectSocket();
    useUserStore().showModalSide = false;
  },
  data() {
    return {
      selectedHorse: null, // 선택한 말
      isSelectedHorse: false, // 말을 선택했는지.
      canSelectHorse: false, // 말을 선택할 수 있을 때
      isShowRes: true, // 윷 던지고 결과 화면 보여줄 때.
      isShowGoDig: false, // 대각선으로 갈지 말지 선택.
      isShowResText: false, // 윷결과 텍스트 출력
      yutText: "", // 윷 결과 문자.
      goModalText1: "",
      goModalText2: "",
      recvList: [],
      // 모달 타입 1=> 대각선 2=> 가운데 방향 3=> 추리.
      modalType: 2,
    };
  },
  computed: {
    // 홍팀 말
    redHorses() {
      const gameStore = useGameStore();
      return gameStore.redHorses;
    },
    // 청팀 말
    blueHorses() {
      const gameStore = useGameStore();
      return gameStore.blueHorses;
    },
    // 타일
    tiles() {
      const gameStore = useGameStore();
      return gameStore.tiles;
    },
    // 턴 체크.
    isThrowYut() {
      const gameStore = useGameStore();
      return gameStore.isThrowYut && gameStore.throwChance > 0 ? true : false;
    },
    // 타이머 체크
    timerCheck() {
      const gameStore = useGameStore();
      if (gameStore.timer === 0 && gameStore.isThrowYut) {
        // 말 선택
        if (this.canSelectHorse) {
          this.randomHorse();
        }
        // 윷 던지기
        else {
          this.moveHorse();
        }
      }
      return gameStore.timer;
    },
    // 방 코드
    roomCode() {
      const userStore = useUserStore();
    //   return userStore.currentRoomInfo.roomCode;
      return "720ca5";
    },
    // 차례 메시지
    turnMessage() {
      const gameStore = useGameStore();
      return gameStore.turnMessage;
    },
    // 차례 메시지 Flag
    isShowTurnMessage() {
      const gameStore = useGameStore();
      return gameStore.isShowTurnMessage;
    },
    // 현재 차례
    nowTunrName() {
      const gameStore = useGameStore();
      // 홍팀
      if (!gameStore.teamTurn) return "홍팀 : " + gameStore.redTurnName + "님";
      else return "청팀 : " + gameStore.blueTurnName + "님";
    },
    // 다음 차례
    nextTurnName() {
      const gameStore = useGameStore();
      if (!gameStore.teamTurn) return "청팀 : " + gameStore.blueTurnName + "님";
      else return "홍팀 : " + gameStore.redTurnName + "님";
    },
    teamTurn() {
      const gameStore = useGameStore();
      return gameStore.teamTurn;
    },
    // 추리 선택 모달 Flag
    isShowReasoning() {
      const gameStore = useGameStore();
      if (gameStore.isShowReasoning) {
        // 내 차례
        if (gameStore.isThrowYut) {
          this.modalType = 3;
        }
        // 홍팀
        else if (!gameStore.teamTurn) {
          // 같은 팀 차례.
          if (gameStore.myTeam === 1) {
            this.modalType = 4;
          }
          // 다른 팀 차례.
          else {
            this.modalType = 5;
          }
        }
        // 청팀
        else {
          // 같은 팀 차례.
          if (gameStore.myTeam === 2) {
            this.modalType = 4;
          }
          // 다른 팀 차례.
          else {
            this.modalType = 5;
          }
        }
      }
      return gameStore.isShowReasoning;
    },
    reasoningChoose() {
      const gameStore = useGameStore();
      return gameStore.reasoningChoose;
    },
  },
  methods: {
    // 로딩 완료 후 게임 시작.
    gameStart() {
      const gameStore = useGameStore();
      gameStore.turnMessage = "게임 시작";
      setTimeout(() => {
        gameStore.isShowTurnMessage = true;
      }, 1000);
      setTimeout(() => {
        gameStore.isShowTurnMessage = false;
        // gameStore.isShowReasoning = true;
        gameStore.startTimer();
      }, 3000);
    },
    leave(event) {
      event.preventDefault();
      event.returnValue = "홈으로...";

      // 홈으로 이동
      useUserStore().initData();
      alert("홈으로!!");
      window.location.href = "/";
      return event.returnValue;
    },
    sendStart() {
      socketSend(`/pub/game/${this.roomCode}/start`, "");
    },
    // 연결
    connectSocket() {
      const userStore = useUserStore();
      const gameStore = useGameStore();
      // 테스트
      if (["4", "5", "123"].includes(userStore.userInfo.email)) {
        gameStore.myTeam = 2;
        connect("blue", userStore.accessToken, this.handleRecvMessage);
      } else {
        gameStore.myTeam = 1;
        connect("red", userStore.accessToken, this.handleRecvMessage);
      }
      setTimeout(() => {
        if (gameStore.redUser.length === 0) this.sendStart();
      }, 3000);
    },
    // 받아오기.
    handleRecvMessage(receivedMsg) {
      const gameStore = useGameStore();
      if (receivedMsg.actionCategory === 0 && gameStore.redUser.length === 0) {
        this.setInfo(receivedMsg);
      } else if (receivedMsg.actionCategory === 1 && !gameStore.isThrowYut) {
        this.receiveYutRes(receivedMsg);
      } else if (receivedMsg.actionCategory === 2) {
        // 말 이동.
        this.receiveSelectHorse(receivedMsg);

        // boolean값들 초기화.
        this.isSelectedHorse = false;
        this.canSelectHorse = false;
        gameStore.isSelect = false;
      } else if (receivedMsg.actionCategory === 3) {
        console.log(receivedMsg);
        gameStore.reasoningChoose = false;
        gameStore.turnChange();
      } else if (receivedMsg.actionCategory === 4) {
        gameStore.isShowReasoning = false;
        if (receivedMsg.reasoningChoose) {
          // 추리권 사용.
          gameStore.reasoningChoose = true;
        } else {
          gameStore.startTimer();
        }
      }
    },
    // 초기 정보 저장
    setInfo(receivedMsg) {
      console.log("setInfo");
      console.log(receivedMsg);
      const gameStore = useGameStore();
      const userStore = useUserStore();
      // 우리팀의 스파이
      gameStore.mySpyId = receivedMsg.mySpyUnitId;

      for (let i = 0; i <= 30; i++) {
        gameStore.tiles[i].horse = [];
      }

      for (let i = 0; i < 5; i++) {
        gameStore.redHorses[i].name = receivedMsg.redTeamUnitList[i].name;
        gameStore.redHorses[i].age = receivedMsg.redTeamUnitList[i].age;
        gameStore.redHorses[i].skill = receivedMsg.redTeamUnitList[i].skill;
        gameStore.redHorses[i].contactor =
          gameStore.myTeam === 1
            ? "???"
            : receivedMsg.redTeamUnitList[i].contactor;
        gameStore.redHorses[i].place =
          gameStore.myTeam === 1 ? "???" : receivedMsg.redTeamUnitList[i].place;
        gameStore.redHorses[i].scal =
          gameStore.myTeam === 1 ? "???" : receivedMsg.redTeamUnitList[i].scal;
        gameStore.redHorses[i].stuff =
          gameStore.myTeam === 1 ? "???" : receivedMsg.redTeamUnitList[i].stuff;
        gameStore.redHorses[i].time =
          gameStore.myTeam === 1 ? "???" : receivedMsg.redTeamUnitList[i].time;
        gameStore.redHorses[i].id = i + 1;
        gameStore.redHorses[i].index = 0;
        gameStore.redHorses[i].team = 1;
        gameStore.redHorses[i].status = "wait";
        gameStore.redHorses[i].check = i;
        gameStore.redHorses[i].endOrder = 0;
        gameStore.redHorses[i].stun = 0;
        gameStore.blueHorses[i].name = receivedMsg.blueTeamUnitList[i].name;
        gameStore.blueHorses[i].age = receivedMsg.blueTeamUnitList[i].age;
        gameStore.blueHorses[i].skill = receivedMsg.blueTeamUnitList[i].skill;
        gameStore.blueHorses[i].contactor =
          gameStore.myTeam === 2
            ? "???"
            : receivedMsg.redTeamUnitList[i].contactor;
        gameStore.blueHorses[i].place =
          gameStore.myTeam === 2 ? "???" : receivedMsg.redTeamUnitList[i].place;
        gameStore.blueHorses[i].scal =
          gameStore.myTeam === 2 ? "???" : receivedMsg.redTeamUnitList[i].scal;
        gameStore.blueHorses[i].stuff =
          gameStore.myTeam === 2 ? "???" : receivedMsg.redTeamUnitList[i].stuff;
        gameStore.blueHorses[i].time =
          gameStore.myTeam === 2 ? "???" : receivedMsg.redTeamUnitList[i].time;
        gameStore.blueHorses[i].id = i + 1;
        gameStore.blueHorses[i].index = 0;
        gameStore.blueHorses[i].team = 2;
        gameStore.blueHorses[i].status = "wait";
        gameStore.blueHorses[i].check = i;
        gameStore.blueHorses[i].endOrder = 0;
        gameStore.blueHorses[i].stun = 0;
      }
      gameStore.redUser = receivedMsg.redTeamUserList;
      gameStore.blueUser = receivedMsg.blueTeamUserList;
      for (let i = 0; i < 3; i++) {
        if (
          gameStore.redUser[i].email === userStore.userInfo.email ||
          gameStore.blueUser[i].email === userStore.userInfo.email
        ) {
          gameStore.myTurn = i;
          console.log("내턴 : " + gameStore.myTurn);
          if (i === 0 && gameStore.myTeam === 1) {
            setTimeout(() => {
              gameStore.isThrowYut = true;
            }, 10000);
          }
          break;
        }
      }
      // 차례 닉네임
      gameStore.redTurnName = receivedMsg.redTeamUserList[0].nickname;
      gameStore.blueTurnName = receivedMsg.blueTeamUserList[0].nickname;
      gameStore.ticket = 0;
      gameStore.enemyTicket = 0;
      gameStore.ticketTemp = 0;
      gameStore.enemyTicketTemp = 0;
      // 라운드
      gameStore.gameSpeed = 3;
      // 미션 타일
      gameStore.missionTiles = receivedMsg.missionRegion;
      // gameStore.startTimer();
      setTimeout(() => {
        this.gameStart();
      }, 10000);
    },

    // 윷 결과를 받아 왔을 때.
    receiveYutRes(receivedMsg) {
      const gameStore = useGameStore();
      if (gameStore.timerId !== null) {
        clearInterval(gameStore.timerId);
        gameStore.timerId = null;
      }
      gameStore.yutRes = receivedMsg.yutRes;
      gameStore.throwRes = receivedMsg.throwRes;
      gameStore.setYutText(receivedMsg.yutRes);

      if (gameStore.yutRes >= 4) {
        gameStore.throwChance += 1;
      }

      this.yutText = gameStore.yutText;

      // 텍스트와 윷결과 판을 다른 타이밍에 나타나게 한다.
      this.isShowRes = true;
      this.$refs.yutThrow.throwYut();
      setTimeout(() => {
        this.isShowResText = true;
      }, 2000);

      // 윷 결과를 없앤다.
      setTimeout(() => {
        this.isShowRes = false;
      }, 3500);
      // 만약 아무 말도 안나갔는데 백도가 나오면 그냥 넘어간다.
      if (gameStore.yutRes == -1) {
        if (
          receivedMsg.team == 1 &&
          gameStore.redHorses[4].check == 4 - gameStore.redEnd &&
          gameStore.redHorses[4].index === 0
        ) {
          setTimeout(() => {
            gameStore.turnChange();
            return;
          }, 3500);
        } else if (
          receivedMsg.team == 2 &&
          gameStore.blueHorses[4].check == 4 - gameStore.blueEnd &&
          gameStore.redHorses[4].index === 0
        ) {
          setTimeout(() => {
            gameStore.turnChange();
            return;
          }, 3500);
        }
      }
      setTimeout(() => {
        gameStore.startTimer();
      }, 3500);
    },
    // 말 선택 결과를 받아 왔을 때.
    receiveSelectHorse(receivedMsg) {
      const gameStore = useGameStore();
      this.isShowResText = false;
      if (gameStore.timerId !== null) {
        clearInterval(gameStore.timerId);
        gameStore.timerId = null;
      }
      gameStore.isGoDiagonal = receivedMsg.goDiagonal;
      gameStore.isCenterDir = receivedMsg.centerDir;
      gameStore.moveHorse(receivedMsg);
    },
    //pub/game/{code}/start
    // 윷 던지기
    moveHorse() {
      const gameStore = useGameStore();
      // 내가 던질 차례인가 체크.
      if (!this.isThrowYut) {
        return;
      }
      // 윷 던지기 호출
      gameStore.yutThrow();
      // 윷 던지기 결과 텍스트.
      this.yutText = gameStore.yutText;
      // 소켓 전송
      let msg = {
        yutRes: gameStore.yutRes,
        throwRes: gameStore.throwRes,
        team: gameStore.myTeam,
      };
      socketSend(`/pub/game/${this.roomCode}/throw-yut`, msg);

      // 텍스트와 윷결과 판을 다른 타이밍에 나타나게 한다.

      this.isShowRes = true;
      this.$refs.yutThrow.throwYut();
      setTimeout(() => {
        this.isShowResText = true;
      }, 2000);

      // 윷 결과를 없앤다.
      setTimeout(() => {
        this.isShowRes = false;
      }, 3500);

      // 만약 아무 말도 안나갔는데 백도가 나오면 그냥 넘어간다.
      if (gameStore.yutRes == -1) {
        if (
          gameStore.myTeam == 1 &&
          gameStore.redHorses[4].check == 4 - gameStore.redEnd &&
          gameStore.redHorses[4].index === 0
        ) {
          setTimeout(() => {
            gameStore.turnChange();
            return;
          }, 3500);
        } else if (
          gameStore.myTeam == 2 &&
          gameStore.blueHorses[4].check == 4 - gameStore.blueEnd &&
          gameStore.redHorses[4].index === 0
        ) {
          setTimeout(() => {
            gameStore.turnChange();
            return;
          }, 3500);
        }
      }
      // 윷 결과가 나오고 나서 부터 선택가능하다.
      setTimeout(() => {
        gameStore.isSelect = true;
        this.canSelectHorse = true;
        // 타이머 다시 시작.
        gameStore.startTimer();
        // 윷 먼저 던지고 선택할때까지 기다린다.
        this.$watch("isSelectedHorse", () => {
          // 선택을 하였다면.
          if (this.isSelectedHorse) {
            // clearInterval(gameStore.timerId);
            // gameStore.timerId = null;
            console.log("소켓 보내기 전");
            console.log(this.selectedHorse);
            // 소켓 전송
            msg = {
              unitIndex: this.selectedHorse.id,
              team: this.selectedHorse.team,
              goDiagonal: gameStore.isGoDiagonal,
              centerDir: gameStore.isCenterDir,
            };
            console.log(msg);
            socketSend(`/pub/game/${this.roomCode}/select-unit`, msg);
            this.isSelectedHorse = false;
            this.canSelectHorse = false;
            gameStore.isSelect = false;
          }
        });
      }, 3500);
      // 말이 이동하고 미션장소인지 아닌지 체크 후 타이머 시작.
    },
    // 제한시간동안 말 선택이 없으면 랜덤으로 선택
    randomHorse() {
      const gameStore = useGameStore();
      while (true) {
        let random = Math.floor(Math.random() * 5);
        // 홍팀 차례면.
        if (!gameStore.teamTurn) {
          if (gameStore.redHorses[random] !== "end") {
            if (
              gameStore.redHorses[random] === "wait" &&
              gameStore.yutRes === -1
            ) {
              continue;
            }
            if ([5, 10].includes(gameStore.redHorses[random].index)) {
              gameStore.isGoDiagonal = true;
            } else if ([22, 27].includes(gameStore.redHorses[random].index)) {
              gameStore.isCenterDir = true;
            }
          }
          this.selectedHorse = gameStore.redHorses[random];
          this.isSelectedHorse = true;
          break;
        }
        // 청팀
        else {
          if (gameStore.blueHorses[random] !== "end") {
            if (
              gameStore.blueHorses[random] === "wait" &&
              gameStore.yutRes === -1
            ) {
              continue;
            }
            if ([5, 10].includes(gameStore.blueHorses[random].index)) {
              gameStore.isGoDiagonal = true;
            } else if ([22, 27].includes(gameStore.blueHorses[random].index)) {
              gameStore.isCenterDir = true;
            }
          }
          this.selectedHorse = gameStore.blueHorses[random];
          this.isSelectedHorse = true;
          break;
        }
      }
    },
    // 말 선택시 이벤트 받기
    selectHorse(horse) {
      // 선택할 수 있을 때만 클릭 이벤트 발생.
      if (this.canSelectHorse) {
        const gameStore = useGameStore();
        const myTeam = gameStore.myTeam;

        // 백도인데 대기중인 말을 선택하면 다시.
        if (horse.index == 0 && gameStore.yutRes == -1) {
          console.log("다시");
        } else if (horse.team == myTeam) {
          // 말 정보를 먼저 담는다.
          this.selectedHorse = horse;
          // 만약 대각선이라면.
          if ([5, 10].includes(horse.index)) {
            // 백도는 모달없이 간다.
            if (gameStore.yutRes === -1) {
              this.isSelectedHorse = true;
            } else {
              // 선택 모달
              this.modalType = 1;
              // 대각선으로 갈지 선택.
              this.isShowGoDig = true;
              this.$watch("isShowGoDig", () => {
                // 선택을 했다면.
                if (!this.isShowGoDig) {
                  this.isSelectedHorse = true;
                }
              });
            }
          }
          // 정 가운데 왼쪽 오른쪽 선택
          else if ([22, 27].includes(horse.index)) {
            // 백도는 모달없이 간다.
            if (gameStore.yutRes === -1) {
              this.isSelectedHorse = true;
            } else {
              // 선택 모달
              this.modalType = 2;
              // 대각선으로 갈지 선택.
              this.isShowGoDig = true;
              this.$watch("isShowGoDig", () => {
                // 선택을 했다면.
                if (!this.isShowGoDig) {
                  this.isSelectedHorse = true;
                }
              });
            }
          } else {
            this.isSelectedHorse = true;
          }
        } else {
          console.log("같은 팀을 선택해 주세요.");
        }
      }
    },

    // 대각선으로 갈때
    goDigYes() {
      const gameStore = useGameStore();
      // 5,10번 타일에서 대각선 이동
      if ([5, 10].includes(this.selectedHorse.index)) {
        gameStore.isGoDiagonal = true;
      }
      // 정 가운데 왼쪽 오른쪽 선택.
      else {
        gameStore.isCenterDir = true;
      }
      this.isShowGoDig = false;
    },
    // 대각선으로 안갈때
    goDigNo() {
      this.isShowGoDig = false;
    },
    // 추리권을 사용할때.
    useTicket() {
      const msg = { reasoningChoose: true };
      socketSend(`/pub/game/${this.roomCode}/reason-ticket-use`, msg);
    },
    // 추리권을 사용하지 않을때.
    notUseTicket() {
      const msg = { reasoningChoose: false };
      socketSend(`/pub/game/${this.roomCode}/reason-ticket-use`, msg);
    },
  },

  // 윷 결과 보여주기.
  showRes() {
    this.isShowRes = true;
    this.$refs.yutThrow.throwYut();
    setTimeout(() => {
      this.isShowResText = true;
    }, 2000);

    // 윷 결과를 없앤다.
    setTimeout(() => {
      this.isShowRes = false;
      this.isShowResText = false;
    }, 3500);
  },
};
</script>
  
<style scoped>
@import "@/assets/css/game/gameBoard.css";
</style>
  