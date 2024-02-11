<template>
  <div id="my-container">
    <ov-video
      class="my-video"
      v-if="streamManager"
      :stream-manager="streamManager"
    /><span id="user-name">{{ nickname }}</span>

    <div class="mission-modal">
      <jewelModal v-if="showMissionModal"/>
    </div>

  </div>
  <!-- <div><p>{{ clientData }}</p></div> -->
</template>

<script>
import OvVideo from "./OvVideo.vue";
import { useUserStore } from "@/store/userStore";
import jewelModal from "@/components/game/minigame/Mineral.vue"


export default {
  data() {
    return {
      showMissionModal: false,
    }
  },

  name: "UserVideo",

  components: {
    OvVideo,
    jewelModal,
  },

  props: {
    streamManager: Object,
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    },

    toggleMic() {
      this.showMissionModal = !this.showMissionModal;
    }
    
  },

}
</script>


<style scoped>
@import url("@/assets/css/openvidu/userVideo.css");
</style>