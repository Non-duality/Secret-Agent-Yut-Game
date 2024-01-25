<template>
  <div class="container">
    <!-- (상단) 이벤트 슬라이더 -->
    <div class="event-container">
      <Carousel :autoplay="3000" :wrap-around="true">
        <Slide v-for="slide in 6" :key="slide">
          <!-- <div class="carousel__item">{{ i18n[slide].name }}</div> -->
          <div class="carousel__item">
            <img id="gallery" :src="i18n[slide].name" />
          </div>
          <CustomNavigation @goToPrev="goToPrev" @goToNext="goToNext" />
        </Slide>

        <template #addons>
          <Pagination />
        </template>
      </Carousel>
    </div>

    <!-- (중간) 아티클 제목 -->
    <div class="article-title">
      <!-- 패치노트 -->
      <div id="patch-title">
        <h2>패치노트</h2>
        <button id="detail-btn">더보기></button>
      </div>

      <!-- FAQ -->
      <div id="faq-title">
        <h2>도움말</h2>
        <button id="detail-btn">더보기></button>
      </div>
    </div>

    <!-- (하단) 아티클 내용 -->
    <div class="article-container">
      <!-- 패치노트 -->
      <div id="patch">
        <div class="patch-list"></div>
      </div>

      <!-- FAQ -->
      <div id="faq"></div>
    </div>
  </div>
</template>
      
  <script>
import { defineComponent, ref } from "vue";
import { Carousel, Slide } from "vue3-carousel";
import CustomNavigation from "./CustomNavigation.vue"; // 커스텀 Navigation 컴포넌트

import "vue3-carousel/dist/carousel.css";

import test01 from "../../assets/img/home/event01.png";
import test02 from "../../assets/img/home/test02.png";
import test03 from "../../assets/img/home/test03.png";
import test04 from "../../assets/img/home/test04.png";
import test05 from "../../assets/img/home/test05.png";
import test06 from "../../assets/img/home/test06.png";

export default defineComponent({
  name: "Autoplay",
  components: {
    Carousel,
    Slide,
    CustomNavigation,
  },

  methods: {
    goToPrev() {
      this.$refs.carousel.prev();
    },
    goToNext() {
      this.$refs.carousel.next();
    },
  },

  data: () => ({
    i18n: [
      {},
      { name: test01 },
      { name: test02 },
      { name: test03 },
      { name: test04 },
      { name: test05 },
      { name: test06 },
    ],
  }),
});
</script>
      
  <style scoped>
@import url("../../assets/css/home/home.css");
</style>