<script>
import Main from "@/view/Main.vue";
import { initializeSnowAnimation } from "@/animations/snowAnimation";
import { initializeClickSoundEffect  } from "@/animations/clickSound";

export default {
  components:{
    Main,
  },

  mounted() {
    // 눈 배경
    initializeSnowAnimation("canv", "./src/assets/img/MainBackground.png");

    // 클릭 사운드
    initializeClickSoundEffect();
  },
  
}
</script>

<template>
  <div class="app-container">
    <Main></Main>
    <canvas id="canv"></canvas>
  </div>
</template>
