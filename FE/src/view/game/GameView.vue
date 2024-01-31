<template>
    <div class="game-main">
        <span class="game-red-team-name">홍팀</span>
        <GameVideo class="game-video-team1"/>
        <GameBoard class="game-board-main"/>
        <span class="game-blue-team-name">청팀</span>
        <GameVideo class="game-video-team2"/>
        <!-- <GameChat class="game-chat-main"/> -->
    </div>
</template>

<script>
import GameBoard from '@/components/game/GameBoard.vue';
import GameVideo from '@/components/game/GameVideo.vue';
import GameChat from '@/components/game/GameChat.vue';

export default {
    components:{
        GameBoard,
        GameVideo,
        GameChat,
    }
}
</script>

<style>
@import "@/assets/css/game/gameView.css";
</style>