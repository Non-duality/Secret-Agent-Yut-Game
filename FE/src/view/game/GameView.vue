<template>
  <div class="game-main">
    <MessageModal />
    <!-- <span class="game-red-team-name">홍팀</span> -->
    <GameVideo class="game-video-team1"/>
    <GameBoard class="game-board-main" />
    <!-- <span class="game-blue-team-name">청팀</span> -->
    <GameVideo class="game-video-team2"/>
    <MiniGame v-show="isMission" />
    <GameChat class="game-chat-main" />
    <GameEnd
      v-if="isGameEnd"
      class="game-end"
      :winMessage="winMessage"
      @closeModal="closeModal"
    />
  </div>
</template>

<script>
import { useGameStore } from "@/store/gameStore";
import { useUserStore } from "@/store/userStore";

import GameBoard from "@/components/game/GameBoard.vue";
import GameVideo from "@/components/game/GameVideo.vue";
import GameChat from "@/components/game/GameChat.vue";
import MiniGame from "@/components/game/MiniGame.vue";
import MessageModal from "@/components/layout/MessageModal.vue";
import GameEnd from "@/components/game/GameEnd.vue";

export default {
  components: {
    GameBoard,
    GameVideo,
    GameChat,
    MiniGame,
    MessageModal,
    GameEnd,
  },
  data() {
    return {
      winMessage: null,
      myTeamName: "",
    };
  },
  computed: {
    // 미션 장소 체크.
    isMission() {
      const gameStore = useGameStore();
      return gameStore.isMission;
    },
    // 게임 상태 체크
    isGameEnd() {
      if (useGameStore().redEnd === 5) {
        this.redWin();
        return true;
      }
      if (useGameStore().blueEnd === 5) {
        this.blueWin();
        return true;
      }
      return false;
    },
  },

  methods: {
    redWin() {
      this.winMessage = 1;
      this.isShowEnd = true;
    },
    blueWin() {
      this.winMessage = 2;
      this.isShowEnd = true;
    },
    closeModal() {
      this.$router.push({ name: "home" });
    },
  },

  // 여기서 로딩창
  mounted() {
    // 로딩 7~8초 정도 보여주기.
  },
};
</script>

<style scoped>
@import "@/assets/css/game/gameView.css";
</style>
