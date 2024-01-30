<template>
  <div class="setting-modal">
    <h2>게임 설정</h2>
    <div class="setting-value">
      <p>배경 음악</p>
      <input class="volume" type="range" v-model="volumeValue" min="0" max="100">
      <p>{{ volumeValue }}%</p>
    </div>
    <div class="setting-value">
      <p>효과음</p>
      <div class="radio">
        <label class="radio-label" for="on">활성화</label>
        <input checked class="radio-button" type="radio" name="sound-effect" id="on" v-model="soundEffect">
      </div>
      <div class="radio">
        <label class="radio-label" for="off">비활성화</label>
        <input class="radio-button" type="radio" name="sound-effect" id="off" v-model="soundEffect">
      </div>
    </div>
    <div class="btn-box">
      <button class="btn-confirm">확인</button>
      <button @click="closeSetting" class="btn-cancel">취소</button>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        volumeValue: 30,
        soundEffect: ''
      }
    },

    methods: {
      // 설정 모달 닫는 것
      closeSetting() {
        this.$emit('close-setting');
      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/layout/setting.css";
</style>