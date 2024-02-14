<template>
  <div class="personal-chat">
    <!-- 채팅 로그 -->
    <div class="chat-container">
      <div
        v-for="(message, index) in reversedpersonalChat"
        :key="index"
        class="chat-log"
      >
        <span
        ></span>
      </div>
      <div>
        {{ personalChat }}
      </div>
      <!-- 입력 부분 -->
      <div class="chat-input-div">
        <input
          class="chat-input"
          type="text"
          placeholder="메시지를 입력하시오."
          v-model="msg"
          @keyup.enter="sendLocalMessage"
        />
        <button class="send-btn" @click="sendLocalMessage">
          <font-awesome-icon :icon="['fas', 'paper-plane']" size="lg" style="color: #e9eaed;" />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
// 아이콘
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { library } from "@fortawesome/fontawesome-svg-core";
library.add(faPaperPlane);

import { useFriendStore } from "@/store/friendStore";
import { useUserStore } from "@/store/userStore";
import { sendEvent } from "@/util/socket";

export default {
  props: {
    friendInfo: Object
  },

  data() {
    return {
      msg: "",
    }
  },
  computed: {
    personalChat() {
      return useFriendStore().chatMessages['toUserEmail']
    },

    // reversedpersonalChat() {
    //   return this.personalChat.slice().reverse()
    // }
  },

  methods: {
    sendLocalMessage() {
      if (this.msg === "") return;

      const event = {
        fromUserEmail: useUserStore().userInfo.email,
        toUserEmail: this.friendInfo.email,
        eventCategory: "1",
        eventAction: "메시지 보내기",
        message : this.msg,
      }

      sendEvent(event)

      this.msg =""
    }
  },

  mounted() {
  }
}
</script>

<style scoped>
@import "@/assets/css/layout/chat.css";
</style>