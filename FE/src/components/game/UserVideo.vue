<template>
  <div id="my-container">
    <ov-video
      class="my-video"
      v-if="streamManager"
      :stream-manager="streamManager"
    />
  </div>
</template>

<script>
import OvVideo from "./OvVideo.vue";

export default {
  name: "UserVideo",

  components: {
    OvVideo,
  },

  props: {
    streamManager: Object,
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    },

    // 임시 미션 모달 토글
    toggleMission() {
      if (this.missionModalNum === -1) {
        this.missionModalNum = Math.floor(Math.random() * 4);
      } else {
        // 현재 모달의 상태 토글
        this.showMissionModal[this.missionModalNum] = !this.showMissionModal[this.missionModalNum];
        
        // 다른 모달을 표시할 준비가 되면 missionModalNum을 다시 -1로 설정
        if (this.showMissionModal[this.missionModalNum]) {
          this.missionModalNum = -1;
        }
      }
    }
    
  },

}
</script>


<style scoped>
@import url("@/assets/css/openvidu/userVideo.css");
</style>