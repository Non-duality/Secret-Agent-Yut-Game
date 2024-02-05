<template>
  <div class="game-main">
    <MessageModal/>
    <span class="game-red-team-name">홍팀</span>
    <GameVideo class="game-video-team1" />
    <GameBoard class="game-board-main" />
    <span class="game-blue-team-name">청팀</span>
    <GameVideo class="game-video-team2" />
    <MiniGame v-show="isMission"/>
    <!-- <GameChat class="game-chat-main"/> -->
  </div>
</template>

<script>
import { useGameStore } from "@/store/gameStore";
import GameBoard from "@/components/game/GameBoard.vue";
import GameVideo from "@/components/game/GameVideo.vue";
import GameChat from "@/components/game/GameChat.vue";
import MiniGame from '@/components/game/MiniGame.vue';
import MessageModal from "@/components/layout/MessageModal.vue";

export default {
  components: {
    GameBoard,
    GameVideo,
    GameChat,
    MiniGame,
    MessageModal,
  },
  computed: {
    // 턴 체크.
    isMission() {
      const gameStore = useGameStore();
      return gameStore.isMission;
    },
  },
};
</script>

<style>
@import "@/assets/css/game/gameView.css";
</style>