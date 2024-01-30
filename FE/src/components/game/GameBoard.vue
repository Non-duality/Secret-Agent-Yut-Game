<template>
  <div class="game-board">
    <div class="game-board-tile">
      <GameBoardTile v-for="(tile, index) in tiles" :key="index" :tile="tile" />
    </div>
    <div class="game-horse-red">
      <h3>홍팀</h3>
      <GameHorse v-for="horse in team1horses" :key="horse.id" :horse="horse" />
    </div>
    <div class="game-horse-blue">
      <h3>청팀</h3>
      <GameHorse v-for="horse in team1horses" :key="horse.id" :horse="horse" />
    </div>
  </div>
</template>
                                                                        
<script>
import GameBoardTile from "./item/GameBoardTile.vue";
import GameHorse from "./item/GameHorse.vue";

export default {
  components: {
    GameBoardTile,
    GameHorse,
  },
  data() {
    return {
      // 타일
      tiles: [
        { id: 0, position: "right" },
        { id: 1, position: "right" },
        { id: 2, position: "right" },
        { id: 3, position: "right" },
        { id: 4, position: "right" },
        { id: 5, position: "right" },
        { id: 6, position: "top" },
        { id: 7, position: "top" },
        { id: 8, position: "top" },
        { id: 9, position: "top" },
        { id: 10, position: "top" },
        { id: 11, position: "left" },
        { id: 12, position: "left" },
        { id: 13, position: "left" },
        { id: 14, position: "left" },
        { id: 15, position: "left" },
        { id: 16, position: "bottom" },
        { id: 17, position: "bottom" },
        { id: 18, position: "bottom" },
        { id: 19, position: "bottom" },
        { id: 20, position: "top-right" },
        { id: 21, position: "top-right" },
        { id: 22, position: "center" },
        { id: 23, position: "bottom-left" },
        { id: 24, position: "bottom-left" },
        { id: 25, position: "top-left" },
        { id: 26, position: "top-left" },
        { id: 27, position: "center" },
        { id: 28, position: "bottom-right" },
        { id: 29, position: "bottom-right" },
      ],
      // 말
      team1horses: [
        { id: 1, index: 0, team: 1, status: "wait", img: "horse1" },
        { id: 2, index: 0, team: 1, status: "wait", img: "horse2" },
        { id: 3, index: 0, team: 1, status: "wait", img: "horse3" },
        { id: 4, index: 0, team: 1, status: "wait", img: "horse4" },
        { id: 5, index: 0, team: 1, status: "wait", img: "horse5" },
      ],
      team2horses: [
        {
          id: 1,
          index: 0,
          team: 2,
          status: "wait",
          img: "@/assets/img/horse/horse1.png",
        },
        {
          id: 2,
          index: 0,
          team: 2,
          status: "wait",
          img: "@/assets/img/horse/horse2.png",
        },
        {
          id: 3,
          index: 0,
          team: 2,
          status: "wait",
          img: "@/assets/img/horse/horse3.png",
        },
        {
          id: 4,
          index: 0,
          team: 2,
          status: "wait",
          img: "@/assets/img/horse/horse4.png",
        },
        {
          id: 5,
          index: 0,
          team: 2,
          status: "wait",
          img: "@/assets/img/horse/horse5.png",
        },
      ],
    };
  },
};
</script>
  
  <style>
@import "@/assets/css/game/gameBoard.css";
</style>
  