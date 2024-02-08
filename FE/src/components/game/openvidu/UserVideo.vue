<template>
  <div id="my-container">
    <ov-video
      class="my-video"
      v-if="streamManager"
      :stream-manager="streamManager"
    /><span id="user-name">{{ nickname }}</span>
    <!-- <button @click="toggleMic">마이크</button> -->
  </div>
  <!-- <div><p>{{ clientData }}</p></div> -->
</template>

<script>
import OvVideo from "./OvVideo.vue";
import { useUserStore } from "@/store/userStore";


export default {
  name: "UserVideo",

  components: {
    OvVideo,
  },

  props: {
    streamManager: Object,
  },

  computed: {
    nickname() {
      return useUserStore().userInfo.nickname;
    },
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    },
  },
};
</script>


<style scoped>
#user-name {
  color: aliceblue;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.my-video {
  margin: 10px;
  border-radius: 50px;
}
</style>