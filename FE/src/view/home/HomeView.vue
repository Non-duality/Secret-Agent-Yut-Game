<template>
  <div class="container">
    <!-- (상단) 이벤트 슬라이더 -->
    <div class="event-container">
      <Carousel :autoplay="4000" :wrap-around="true">
        <Slide v-for="slide in 6" :key="slide">
          <div class="carousel__item">
            <img id="gallery" :src="i18n[slide].name" />
          </div>
        </Slide>

        <template #addons>
          <!-- <Pagination /> -->
          <Navigation />
        </template>
      </Carousel>
    </div>

    <!-- (중간) 아티클 제목 -->
    <div class="article-title">
      <!-- 패치노트 -->
      <div id="patch-title">
        <h3>패치노트</h3>
        <button id="detail-btn" @click="openModal('patch')">더보기></button>
        <transition name="fade">
          <PatchNoteItem v-if="showPatchItemModal" @close-modal="closeModal" />
        </transition>
      </div>

      <!-- FAQ -->
      <div id="faq-title">
        <h3>도움말</h3>
        <button id="detail-btn" @click="openModal('help')">더보기></button>

        <transition name="fade">
          <HelpItem v-if="showHelpItemModal" @close-modal="closeModal" />
        </transition>
      </div>
    </div>

    <!-- (하단) 아티클 내용 -->
    <div class="article-container">
      <!-- 패치노트 -->
      <div id="patch">
        <div class="patch-list">
          <span>Hello</span>
          <span>Hello</span>
          <span>Hello</span>
        </div>
      </div>

      <!-- FAQ -->
      <div id="faq">
        <div class="patch-list">
          <span>Hello</span>
          <span>Hello</span>
          <span>Hello</span>
        </div>
      </div>
    </div>
  </div>
</template>
      
  <script>
import { defineComponent } from "vue";
import { Carousel, Slide, Navigation } from "vue3-carousel";

import "vue3-carousel/dist/carousel.css";

// Item vue
import PatchNoteItem from "../../components/home/PatchItem.vue";
import HelpItem from "../../components/home/HelpItem.vue";

// 이미지
import event01 from "../../assets/img/home/event01.png";
import event02 from "../../assets/img/home/event02.png";
import test03 from "../../assets/img/home/test03.png";
import test04 from "../../assets/img/home/test04.png";
import test05 from "../../assets/img/home/test05.png";
import test06 from "../../assets/img/home/test06.png";

export default defineComponent({
  name: "Autoplay",
  components: {
    Carousel,
    Slide,
    Navigation,
    PatchNoteItem,
    HelpItem,
  },

  methods: {
    openModal(value) {
      if (value === "patch") {
        this.showPatchItemModal = true;
      } else if (value === "help") {
        this.showHelpItemModal = true;
      }
    },

    closeModal(value) {
      if (value === "patch") {
        this.showPatchItemModal = false;
      } else if (value === "help") {
        this.showHelpItemModal = false;
      }
    },
  },

  data() {
    return {
      i18n: [
        {},
        { name: event01 },
        { name: event02 },
        { name: test03 },
        { name: test04 },
        { name: test05 },
        { name: test06 },
      ],
      showPatchItemModal: false,
      showHelpItemModal: false,
    };
  },
});
</script>
      
<style scoped>
@import url("../../assets/css/home/home.css");
</style>