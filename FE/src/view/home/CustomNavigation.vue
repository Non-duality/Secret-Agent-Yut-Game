<template>
  <div class="custom-navigation">
    <!-- 왼쪽 화살표 아이콘 (이전 슬라이드로 이동) -->
    <div class="custom-arrow custom-arrow-left" @click="goToPrev">
      <i class="fas fa-chevron-left"></i> <!-- 이 아이콘은 Font Awesome 라이브러리에서 가져옴 -->
    </div>

    <!-- 오른쪽 화살표 아이콘 (다음 슬라이드로 이동) -->
    <div class="custom-arrow custom-arrow-right" @click="goToNext">
      <i class="fas fa-chevron-right"></i> <!-- 이 아이콘은 Font Awesome 라이브러리에서 가져옴 -->
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goToPrev() {
      this.$emit("goToPrev");
    },
    goToNext() {
      this.$emit("goToNext");
    },
  },
};
</script>

<style scoped>
.custom-navigation {
  display: flex;
  align-items: center;
}

.custom-arrow {
  cursor: pointer;
  font-size: 24px;
  color: white; /* 아이콘을 하얀색으로 설정 */
  margin: 0 10px;
}

.custom-arrow:hover {
  /* 호버 상태일 때 스타일 변경 가능 */
  color: #007bff; /* 원하는 호버 색상으로 변경 */
}
</style>
