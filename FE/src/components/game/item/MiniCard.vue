<template>
  <div class="card-game">
    <span class="card-title">카드를 선택하시오.</span>
    <div class="card-container">
      <div
        v-for="(card, index) in cards"
        :key="index"
        class="card-item"
        @click="selectCard(card)"
      >
        <img class="card-img" src="@/assets/img/game/taegeuk.png" />
      </div>
    </div>
    <div v-if="selectedCard">
      선택한 카드: {{ selectedCard.suit }} {{ selectedCard.value }}
    </div>
    <button @click="shuffleCards">카드 섞기</button>
  </div>
</template>
  
  <script>
export default {
  data() {
    return {
      cards: [
        { suit: "게임1", value: "A" },
        { suit: "게임2", value: "2" },
        { suit: "게임3", value: "2" },
        { suit: "게임4", value: "2" },
        { suit: "게임5", value: "2" },
        { suit: "게임6", value: "2" },
        { suit: "게임7", value: "2" },
        { suit: "게임8", value: "2" },
        // 여기에 더 많은 카드를 추가하세요...
      ],
      selectedCard: null,
    };
  },
  methods: {
    shuffleCards() {
      this.cards = this.cards
        .map((a) => ({ sort: Math.random(), value: a }))
        .sort((a, b) => a.sort - b.sort)
        .map((a) => a.value);
    },
    selectCard(card) {
      this.selectedCard = card;
    },
  },
};
</script>
  
  <style>
@import "@/assets/css/game/miniCard.css";
</style>
  