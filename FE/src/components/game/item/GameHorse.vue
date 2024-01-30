<template>
  <div class="horse">
    <button @mouseover="showModal = true" @mouseleave="showModal = false"><img :src="imgSrc" alt="Horse image" /></button>
    <div class="modal" v-if="showModal">{{ horse }}</div>
  </div>
</template>

<script>
export default {
  props: {
    horse: Object,
  },
  data() {
    return {
      imgSrc: "",
      showModal: false, // 모달 상태 관리,
    };
  },
  async mounted() {
    try {
      if (this.horse.img === "horse1") {
        const module = await import(`@/assets/img/horse/horse1.png`);
        this.imgSrc = module.default;
      }else if(this.horse.img === "horse2"){
        const module = await import(`@/assets/img/horse/horse2.png`);
        this.imgSrc = module.default;
      }else if(this.horse.img === "horse3"){
        const module = await import(`@/assets/img/horse/horse3.png`);
        this.imgSrc = module.default;
      }else if(this.horse.img === "horse4"){
        const module = await import(`@/assets/img/horse/horse4.png`);
        this.imgSrc = module.default;
      }else if(this.horse.img === "horse5"){
        const module = await import(`@/assets/img/horse/horse5.png`);
        this.imgSrc = module.default;
      }
    } catch (error) {
      console.error("Error loading image:", error);
      // 오류 발생시 기본 이미지 경로 또는 대체 이미지 설정
      this.imgSrc = "/path/to/default-image.png";
    }
  },
};
</script>

<style>
.horse button {
  background: none; /* 배경 제거 */
  border: none; /* 테두리 제거 */
  padding: 0; /* 패딩 제거 */
  margin: 0; /* 마진 제거 */
  cursor: pointer; /* 커서 스타일 변경 */
}
.horse button:hover img {
  transform: scale(1.1); /* 이미지 확대 */
  transition: transform 0.3s ease, opacity 0.3s ease; /* 부드러운 전환 효과 */
}
.horse img {
  width: 50px;
  margin-left: 10px;
}

.modal {
  /* 모달 스타일 */
  position: fixed;
  background: rgba(255, 255, 255, 0.388);
  border: 1px solid #ddd;
  top: 300px;
  right: 300px;
  padding: 10px;
  width: 200px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}
</style>
