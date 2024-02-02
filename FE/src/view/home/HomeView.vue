<template>
  <div class="container">
    <!-- (상단) 배너 슬라이드 -->
    <div class="banner-container">
      <div class="event-container">
        <Carousel :autoplay="5000" :wrap-around="true">
          <Slide v-for="slide in 3" :key="slide">
            <div class="carousel__item">
              <img id="gallery" :src="i18n[slide].name" />
            </div>
          </Slide>

          <template #addons>
            <!-- <Pagination /> -->
            <!-- <Navigation /> -->
          </template>
        </Carousel>
      </div>
    </div>

    <!-- (하단) 패치노트 및 도움말 -->
    <div class="article-container">
      <!-- 패치노트 -->
      <div id="patch">
        <!-- 제목 -->
        <div id="patch-title">
          <h3>패치노트</h3>
          <button id="detail-btn" @click="openModal('patch')">더보기></button>
          <transition name="fade">
            <PatchNoteItem
              v-if="showPatchItemModal"
              @close-modal="closeModal"
            />
          </transition>
        </div>

        <!-- 내용 -->
        <div id="patch-content">
          <div id="patch-list">
            <span>Hello</span>
            <span>Hello</span>
            <span>Hello</span>
          </div>
        </div>
      </div>

      <!-- 도움말 -->
      <div id="help">
        <transition name="fade"
          ><div v-if="showMessage" class="help-message">
            <button class="help-close-btn" @click="closeMessage">
              &times;
            </button>
            <!-- <div class="arrow-up"></div> -->
            여기 도움말을 눌러보세요!
          </div></transition
        >

        <div class="help-icon" @click="openModal('help')">
          <i class="fas fa-question-circle"></i>
          <img src="../../assets/img/help/horse.png" @click="" alt="도움말 아이콘" />
        </div>

        <transition name="fade">
          <HelpItem v-if="showHelpItemModal" @close-modal="closeModal" />
        </transition>
      </div>
    </div>
  </div>
</template>
      
  <script>
import { defineComponent } from "vue";
import { Carousel, Slide, Navigation } from "vue3-carousel";

import "vue3-carousel/dist/carousel.css";

// Item vue
import PatchNoteItem from "@/components/home/PatchItem.vue";
import HelpItem from "@/components/home/HelpItem.vue";

// 이미지
import event01 from "@/assets/img/home/carousel1.png";
import event02 from "@/assets/img/home/carousel2.png";
import event03 from "@/assets/img/home/carousel3.png";

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

    toggleMessage() {
      this.showMessage = !this.showMessage;
    },

    closeMessage() {
      this.showMessage = false;
    },
  },

  data() {
    return {
      i18n: [{}, { name: event01 }, { name: event02 }, { name: event03 }],
      showPatchItemModal: false,
      showHelpItemModal: false,
      showMessage: false,
    };
  },

  mounted() {
    // 페이지 로드 후 3초 뒤에 메시지를 표시
    setTimeout(() => {
      this.showMessage = true;
    }, 4000);
  },
});
</script>
      
<style scoped>
@import url("../../assets/css/home/home.css");
</style>