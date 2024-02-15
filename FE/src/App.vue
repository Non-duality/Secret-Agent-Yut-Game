<script>
import Main from "@/view/Main.vue";
import { initializeSnowAnimation } from "@/animations/snowAnimation";
import { initializeClickSoundEffect } from "@/animations/clickSound";

export default {
  components: {
    Main,
  },

  mounted() {
    // 눈 배경
     ("canv", "./src/assets/img/MainBackground.png");

    // 클릭 사운드
    initializeClickSoundEffect();
  },

  computed: {
    // 사이트 변경될 때마다 배경화면 변경
    backgroundClass() {
      return this.$route.meta.background || "default";
    },
  },
};
</script>

<template>
  <div class="app-container" :class="backgroundClass">
    <Main></Main>
    <!-- 효과음 오디오 요소 추가 -->
    <audio id="clickSound" src="./src/assets/sound/1.mp3"></audio>
    <canvas id="canv"></canvas>
  </div>
</template>
