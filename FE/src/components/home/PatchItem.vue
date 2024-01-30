<template>
  <!-- 패치노트 모달창 -->
  <div class="modal">
    <div class="modal-content">
      <font-awesome-icon :icon="['fas', 'x']" style="color: #ffffff;" class="close" @click="closeModal('patch')" />
      <h2 class="title">패치노트</h2>
      <br />
      
      <br />
    </div>
  </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faX } from '@fortawesome/free-solid-svg-icons';
library.add(faX)

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