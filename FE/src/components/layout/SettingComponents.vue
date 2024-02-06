<template>
  <div class="setting-modal">
    <h2>게임 설정</h2>
    <div class="background-value">
      <label for="backgroundMusic">배경음 선택</label>
      <select id="backgroundMusic" v-model="selectedOption">
        <option v-for="(option, index) in options" :key="index" :value="option">
          {{ option.name }}
        </option>
      </select>
    </div>
    <div class="setting-value">
      <p>배경 음악</p>
      <input class="volume" type="range" v-model="volumeValue" min="0" max="100">
      <p>{{ volumeValue }}%</p>
    </div>
    <div class="setting-value">
      <p>효과음</p>
      <div class="radio">
        <label class="radio-label" for="on">활성화</label>
        <input checked class="radio-button" type="radio" value="true" name="sound-effect" id="on" v-model="soundEffect">
      </div>
      <div class="radio">
        <label class="radio-label" for="off">비활성화</label>
        <input class="radio-button" type="radio" value="false" name="sound-effect" id="off" v-model="soundEffect">
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
        soundEffect: true,
        selectedOption: "선택하세요",
        options: [
          { path: "@/assets/sound/Axel Lundstrom.mp3", name: "Axel Lundstrom" },
          { path: "@/assets/sound/Becouse Heart.mp3", name: "Becouse Heart" },
          { path: "@/assets/sound/Dee Yan-Key.mp3", name: "Dee Yan-Key" },
          { path: "@/assets/sound/Denys Kyshchuk - Upbeat Game.mp3", name: "Upbeat Game" },
          { path: "@/assets/sound/Eggy Toast.mp3", name: "Eggy Toast" },
          { path: "@/assets/sound/HoliznaCC0.mp3", name: "HoliznaCC0" },
          { path: "@/assets/sound/Mohmohrap.mp3", name: "Mohmohrap" },
          { path: "@/assets/sound/Skipping in the No Standing Zone.mp3", name: "Skipping in the No Standing Zone" },
          { path: "@/assets/sound/Skipping Through the Orchestra Pit.mp3", name: "Skipping Through the Orchestra Pit" },
          { path: "@/assets/sound/Strobotone.mp3", name: "Strobotone" },
          { path: "@/assets/sound/Wizwars.mp3", name: "Wizwars" },
        ],
        audio: null,
      };
    },

    methods: {
      // 설정 모달 닫는 것
      closeSetting() {
        this.$emit('close-setting');
      },
   
      toggleDropdown() {
        this.isOpen = !this.isOpen;
      },

      selectOption(option) {
        this.selectedOption = option;
        this.isOpen = false;
      }
    }
  };
</script>

<style scoped>
@import "../../assets/css/layout/setting.css";
</style>