<template>
  <div class="game-board">
    <div class="game-board-tile">
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
  </div>
</template>
                                                                        
<script>
import GameBoardTile from "./item/GameBoardTile.vue";
import GameHorse from "./item/GameHorse.vue";

import { useGameStore } from "@/store/gameStore";

export default {
  components: {
    GameBoardTile,
    GameHorse,
  },
  data() {
    return {
      redWaitIdx: 0,
      blueWaitIdx: 0,
      isSelectHorse: null,
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
    moveHorse() {
      const gameStore = useGameStore();
      gameStore.yutThrow();
      gameStore.moveHorse();
    },
    selectHorse(horse) {
      this.$watch('')
    }
  },
};
</script>
  
  <style>
@import "@/assets/css/game/gameBoard.css";
</style>
  