<template>
  <!-- 패치노트 모달창 -->
  <div class="modal">
    <div class="modal-content">
      <button class="close" @click="closeModal('patch')">&times;</button>
      <h2 class="title">패치노트</h2>
      <br />
      
      <br />
      <button @click="closeModal('patch')" class="modal-cancel-btn">
        나가기
      </button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    closeModal(modalId) {
      this.$emit("close-modal", modalId);
    },
  },
};
</script>

<style scoped>
@import url("../../assets/css/home/patch.css")
</style>