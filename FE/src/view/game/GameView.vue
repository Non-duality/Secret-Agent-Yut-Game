<template>
  <div class="game-main">
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


export default {
  components: {
    GameBoard,
    GameVideo,
    GameChat,
    MiniGame,
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

<style scoped>
@import "../../assets/css/game/gameView.css";
</style>