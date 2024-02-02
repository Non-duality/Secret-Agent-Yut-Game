<template>
  <div class="game-board">
    <div class="game-board-tile">
      <div class="game-yut-res" v-show="isShowRes">
        <span class="game-yut-res-text">{{ yutText }}</span>
        <GameYut />
      </div>
      <GameBoardTile v-for="(tile, index) in tiles" :key="index" :tile="tile" />
    </div>
    <GameHorse
      v-for="horse in redHorses"
      :key="horse.id"
      :horse="horse"
      @selectHorse="selectHorse"
    />
    <GameHorse
      v-for="horse in blueHorses"
      :key="horse.id"
      :horse="horse"
      @selectHorse="selectHorse"
    />
    <button
      @click="moveHorse"
      style="top: 50px; left: 250px; position: absolute"
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
import GameBoardTile from "./item/GameBoardTile.vue";
import GameHorse from "./item/GameHorse.vue";
import { useGameStore } from "@/store/gameStore";
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
      isShowRes: false, // 윷 던지고 결과 화면 보여줄 때.
      isShowGoDig: false, // 대각선으로 갈지 말지 선택.
      yutText: "", // 윷 결과 문자.
      goModalText1: "",
      goModalText2: "",
    };
  },
  computed: {
    redHorses() {
      const gameStore = useGameStore();
      return gameStore.redHorses;
    },
    blueHorses() {
      const gameStore = useGameStore();
      return gameStore.blueHorses;
    },
    tiles() {
      const gameStore = useGameStore();
      return gameStore.tiles;
    },
  },
  methods: {
    // 윷 던지기
    moveHorse() {
      const gameStore = useGameStore();
      // 윷 던지기 호출
      gameStore.yutThrow();
      // 윷 던지기 결과 텍스트.
      this.yutText = gameStore.yutText;
      this.isShowRes = true;
      setTimeout(() => {
        this.isShowRes = false;
      }, 3000);

      // 만약 아무 말도 안나갔는데 백도가 나오면 그냥 넘어간다.
      if (gameStore.yutRes == -1) {
        if (gameStore.myTeam == 1 && gameStore.redHorses[4].check == 5) return;
        else if (gameStore.myTeam == 2 && gameStore.blueHorses[4].check == 5)
          return;
      }

      gameStore.isSelect = true;
      this.canSelectHorse = true;
      // 윷 먼저 던지고 선택할때까지 기다린다.
      this.$watch("isSelectedHorse", () => {
        // 선택을 하였다면.
        if (this.isSelectedHorse) {
          // 말 이동.
          gameStore.moveHorse(this.selectedHorse);
          // boolean값들 초기화.
          this.isSelectedHorse = false;
          this.canSelectHorse = false;
          gameStore.isSelect = false;
        }
      });
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
            console.log("확인");
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
          // 정 가운데 왼쪽 오른쪽 선택
          else if ([22, 27].includes(horse.index)) {
            // 텍스트 바꿈.
            this.goModalText1 = "왼쪽";
            this.goModalText2 = "오른쪽";
            this.isShowGoDig = true;
            this.$watch("isShowGoDig", () => {
              if (!this.isShowGoDig) {
                this.isSelectedHorse = true;
              }
            });
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
      if([5,10].includes(this.selectedHorse.index)){
        gameStore.isGoDiagonal = true;
      }
      // 정 가운데 왼쪽 오른쪽 선택.
      else{
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
  
  <style>
@import "@/assets/css/game/gameBoard.css";
</style>
  