<template>
  <div class="end-main">
    <div class="end-container">
      <span class="end-myTeam-res">WIN</span>
      <button class="end-btn" @click="sendToParent">확인</button>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";

export default {
  props: {
    winMessage: Number,
  },
  computed: {
    text() {
      switch (this.winMessage) {
        case 1:
          return "홍팀이 승리했습니다!";
        case 2:
          return "청팀이 승리했습니다!";
        case 3:
          return "밀정이 도착하였습니다.";
        default:
          return "게임 결과를 기다리는 중...";
      }
    },
  },
  methods: {
    sendToParent() {
      this.$emit("closeModal");
      useUserStore().showModalSide = true;
    },
  },
};
</script>

<style>
@import "@/assets/css/game/gameEnd.css";
</style>