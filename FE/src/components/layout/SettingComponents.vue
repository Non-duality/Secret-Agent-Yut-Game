<template>
  <div class="setting-modal">
    <h2>게임 설정</h2>
    <div class="background-value">
      <label for="backgroundMusic">배경음 선택</label>
      <select id="backgroundMusic" v-model="selectedOption" @change="selectOption(selectedOption)">
        <option v-for="(option, index) in options" :key="index" :value="option">
          {{ option.name }}
        </option>
      </select>
      <audio id="clickSound" ref="audioElement" preload="auto">
        <source :src="selectedOption.path" type="audio/mpeg">
      </audio>
    </div>
    <div class="setting-value">
      <p class="volume-sound">소리 크기</p>
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
      <button class="btn-confirm" @click="playBgm">변경</button>
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
        selectedOption: "",   // 현재 선택된 BGM
        options: [
          { path: "@/assets/sound/Axel.mp3", name: "Axel Lundstrom" },
          { path: "@/assets/sound/Becouse.mp3", name: "Becouse Heart" },
          { path: "@/assets/sound/Dee.mp3", name: "Dee Yan-Key" },
          { path: "@/assets/sound/Denys.mp3", name: "Upbeat Game" },
          { path: "@/assets/sound/Eggy.mp3", name: "Eggy Toast" },
          { path: "@/assets/sound/Holizna.mp3", name: "HoliznaCC0" },
          { path: "@/assets/sound/Mohmohrap.mp3", name: "Mohmohrap" },
          { path: "@/assets/sound/Standing.mp3", name: "Skipping in the No Standing Zone" },
          { path: "@/assets/sound/Strobotone.mp3", name: "Strobotone" },
          { path: "@/assets/sound/Wizwars.mp3", name: "Wizwars" },
        ],

        isOpen: false,  // 설정 모달이 열려있는지 닫혀있는지 판단
        audio: null,    // 현재 재생중인 음악
      };
    },

    methods: {
      // 설정 모달 닫는 것
      closeSetting() {
        try {
          this.$emit('close-setting');
        } catch (error) {
          console.error('Error in closeSetting:', error);
        }
      },
      
      // 설정 모달의 닫힘 여부 판단하는 메서드
      toggleDropdown() {
        this.isOpen = !this.isOpen;
      },

      // 배경음 선택하는 메서드
      selectOption(option) {
        this.selectedOption = option;
        this.isOpen = false;
        console.log(this.selectedOption)
      },

      // 배경음 재생
      playBgm() {
        // 재생 중인 배경음이 있다면 일시 중지
        if ( this.audio != null ) {
          this.audio.pause();
          this.audio.currentTime = 0;     // 재생 중인 배경음 초기화
        }
        this.audio = this.$refs.audioElement;              // 오디오 세팅
        this.audio.volume = this.volumeValue / 100;        // 볼륨 설정
        // 오디오가 완전히 로드될 때까지 기다린 후 재생
        this.audio.addEventListener('loadeddata', () => {
          this.audio.play();
        });
      },
    }
  };
</script>

<style scoped>
@import "@/assets/css/layout/setting.css";
</style>