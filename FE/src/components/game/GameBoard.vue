<template>
  <div class="game-board">
    <div class="game-board-tile">
      <!-- 윷 던진 결과 -->
      <div class="game-yut-res" v-show="isShowRes">
        <div class="game-yut-text-div">
          <span class="game-yut-res-text" v-show="isShowResText">{{
            yutText
          }}</span>
        </div>
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
      @click="connectSocket"
      style="top: 0px; left: 250px; position: absolute"
      :disabled="!isThrowYut"
    >
      연결
    </button>
    <!-- 버튼 임시 -->
    <button
      @click="sendStart"
      style="top: 0px; left: 300px; position: absolute"
      :disabled="!isThrowYut"
    >
      시작
    </button>
    <!-- 버튼 임시 -->
    <button
      @click="moveHorse"
      style="top: 50px; left: 250px; position: absolute"
      :disabled="!isThrowYut"
    >
      test
    </button>
    <button
      v-if="isShowGoDig"
      @click="goDigYes"
      style="top: 150px; left: 250px; position: absolute"
    >
      {{ goModalText1 }}
    </button>
    <button
      v-if="isShowGoDig"
      @click="goDigNo"
      style="top: 250px; left: 250px; position: absolute"
    >
      {{ goModalText2 }}
    </button>
  </div>
</template>
                                                                        
<script>
import { connect, socketSend } from "@/util/socket.js";
import GameBoardTile from "./item/GameBoardTile.vue";
import GameHorse from "./item/GameHorse.vue";
import { useGameStore } from "@/store/gameStore";
import { useUserStore } from "@/store/userStore";
import GameYut from "./item/GameYut.vue";

export default {
  components: {
    GameBoardTile,
    GameHorse,
    GameYut,
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
      return gameStore.isThrowYut;
    },
  },
  methods: {
    connectSocket() {
      const userStore = useUserStore();
      connect(userStore.accessToken, this.handleRecvMessage);
    },
    handleRecvMessage(receivedMsg) {
      console.log(receivedMsg);
      if(!this.isThrowYut){
        console.log(receivedMsg.actionCategory);
        if(receivedMsg.actionCategory === 1){
          receiveYutRes();
        }else if(receivedMsg.actionCategory === 2){
          receiveSelectHorse();
        }
      }
    },
    // 윷 결과를 받아 왔을 때.
    receiveYutRes(){

    },
    // 말 선택 결과를 받아 왔을 때.
    receiveSelectHorse(){

    },
    sendStart() {
      socketSend("/pub/game/80ba0a/start", "");
    },
    //pub/game/{code}/start
    // 윷 던지기
    moveHorse() {
      // 내가 던질 차례인가 체크.
      if (!this.isThrowYut) return;
      // 윷 던지기 호출
      const gameStore = useGameStore();
      gameStore.yutThrow();
      // 윷 던지기 결과 텍스트.
      this.yutText = gameStore.yutText;
      // 소켓 전송
      let msg = {
        yutRes: gameStore.yutRes,
        throwRes: gameStore.throwRes,
      };
      socketSend("/pub/game/80ba0a/throw-yut", msg);
      // 텍스트와 윷결과 판을 다른 타이밍에 나타나게 한다.
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

      // 만약 아무 말도 안나갔는데 백도가 나오면 그냥 넘어간다.
      if (gameStore.yutRes == -1) {
        if (gameStore.myTeam == 1 && gameStore.redHorses[4].check == 5) return;
        else if (gameStore.myTeam == 2 && gameStore.blueHorses[4].check == 5)
          return;
      }
      // 윷 결과가 나오고 나서 부터 선택가능하다.
      setTimeout(() => {
        gameStore.isSelect = true;
        this.canSelectHorse = true;
        // 윷 먼저 던지고 선택할때까지 기다린다.
        this.$watch("isSelectedHorse", () => {
          // 선택을 하였다면.
          if (this.isSelectedHorse) {
            // 말 이동.
            gameStore.moveHorse(this.selectedHorse);
            // 소켓 전송
            msg = { unitIndex: this.selectedHorse.id };
            socketSend("/pub/game/80ba0a/select-unit", msg);
            // boolean값들 초기화.
            this.isSelectedHorse = false;
            this.canSelectHorse = false;
            gameStore.isSelect = false;
          }
        });
      }, 2000);
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
              // 텍스트 바꿈.
              this.goModalText1 = "네";
              this.goModalText2 = "아니오";
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
              // 텍스트 바꿈.
              this.goModalText1 = "왼쪽";
              this.goModalText2 = "오른쪽";
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
  },
};
</script>
  
<style scoped>
@import "@/assets/css/game/gameBoard.css";
</style>
  