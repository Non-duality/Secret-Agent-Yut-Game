<template>
  <div class="setting-modal">
    <h2>게임 설정</h2>
    <div class="setting-value">
      <p>배경 음악</p>
      <input type="range" v-model="progressValue" min="0" max="100">
      <p>{{ progressValue }}%</p>
    </div>
    <div class="setting-value">
      <p>효과음</p>
      <div class="mode">
        <input type="checkbox" id="full" name="mode" value="full" checked/>
      </div>
    </div>
    <div class="btn">
      <button class="btn-confirm">확인</button>
      <button @click="closeSettingModal" class="btn-cancel">취소</button>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        progressValue: 30,
      }
    },

    methods: {
      closeSettingModal() {
        this.$emit('closeSetting')
      }
    }
  }
</script>

<style scoped>
@import '@/assets/css/layout/settingComponents.css';
</style>