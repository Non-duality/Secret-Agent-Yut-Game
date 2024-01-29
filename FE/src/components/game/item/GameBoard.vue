<template>
  <div class="game-board">
    <GameBoardTile
      v-for="(tileValue, index) in tiles"
      :key="index"
      :value="tileValue"
      :class="{
        'large-tile':
          index === 0 || index === 5 || index === 10 || index === 15,
      }"
    />
  </div>
</template>
                                                                        
<script>
import GameBoardTile from "./GameBoardTile.vue";

export default {
  components: {
    GameBoardTile,
  },
  data() {
    return {
      // 여기에 보드의 상태를 나타내는 배열 또는 객체를 정의합니다.
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
    };
  },
};
</script>
  
  <style>
@import "@/assets/css/game/gameBoard.css";
</style>
  