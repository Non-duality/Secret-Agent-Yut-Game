<template>
  <div class="mini-main">
    <MiniCard v-if="isShowCardSelect && isThrowYut" @selectCard="selectCard" />
    <Cake v-if="isShowCake && isThrowYut" />
    <cham v-if="isShowCham && isThrowYut" />
    <FlyCatch v-if="isShowFlyCatch && isThrowYut" />
    <Mineral v-if="isShowMineral && isThrowYut" />
    <div class="mini-wait" v-if="!isThrowYut">
      {{ nickName }}님이 미션중입니다.<br />
      잠시만 기다려 주세요
    </div>
  </div>
</template>

<script>
import MiniCard from "./item/MiniCard.vue";
import Cake from "./minigame/Cake.vue";
import Cham from "./minigame/Cham.vue";
import FlyCatch from "./minigame/FlyCatch.vue";
import Mineral from "./minigame/Mineral.vue";

import { useMiniGameStore } from "@/store/miniGameStore";
import { useGameStore } from "@/store/gameStore";
import { useUserStore } from "@/store/userStore";

import { socketSend } from "@/util/socket.js";

export default {
  data() {
    return {
      isShowCake: false,
      isShowCham: false,
      isShowFlyCatch: false,
      isShowMineral: false,
    };
  },
  components: {
    MiniCard,
    Cake,
    Cham,
    FlyCatch,
    Mineral,
  },
  computed: {
    isShowCardSelect() {
      const miniStore = useMiniGameStore();
      return miniStore.isShowCardSelect;
    },
    isThrowYut() {
      const gameStore = useGameStore();
      return gameStore.isThrowYut;
    },
    nickName() {
      const gameStore = useGameStore();
      if (!gameStore.teamTurn) return gameStore.redTurnName;
      else return gameStore.blueTurnName;
    },
  },
  methods: {
    selectCard(selectedCard) {
      const miniStore = useMiniGameStore();
      miniStore.isShowCardSelect = false;
      switch (selectedCard.value) {
        case 1:
          this.isShowFlyCatch = true;
          break;
        case 2:
          this.isShowCake = true;
          break;
        case 3:
          this.isShowMineral = true;
          break;
        case 4:
          this.isShowCham = true;
          break;
      }
      // 테스트 용으로 나중에 없앰.
      setTimeout(() => {
        if (this.isThrowYut) {
          const msg = {
            email : useUserStore().userInfo.email,
            result : true,
          }
          console.log(msg);
          socketSend(`/pub/game/${useUserStore().currentRoomInfo.roomCode}/mini-game-finish`, msg);
        }
        // useGameStore().missionEnd();
      }, 5000);
    },
  },
};
</script>

<style>
@import "@/assets/css/game/miniGame.css";
</style>