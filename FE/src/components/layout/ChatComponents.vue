<template>
  <div class="personal-chat">
    <!-- 채팅 로그 -->
    <div class="chat-container">
      <div
        v-for="(message, index) in reversedpersonalChat"
        :key="index"
        class="chat-log"
      >
        <span :class="getNicknameColor(message[0])">[{{ message[0] }}] </span>
        <span class="chat-message">{{ message[1] }}</span>
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
      return useFriendStore().chatMessages[this.friendInfo.email] || []
    },

    reversedpersonalChat() {
      return this.personalChat.slice().reverse()
    }
  },

  methods: {
    // 메시지 보내기
    sendLocalMessage() {
      const friendStore = useFriendStore()
      if (this.msg === "") return;

      const event = {
        fromUserEmail: useUserStore().userInfo.email,
        toUserEmail: this.friendInfo.email,
        eventCategory: "1", 
        eventAction: "메시지 보내기",
        message : this.msg,
      }
      console.log(this.friendInfo)
      sendEvent(event)

      friendStore.addChatMyMessage(this.friendInfo.email, useUserStore().userInfo.nickname, this.msg)

      this.msg =""
    },

    // 닉네임에 따라 색 변경
    getNicknameColor(nickname) {
      if (nickname === useUserStore().userInfo.nickname) {
        return 'color-red'
      } else {
        return 'color-green'
      }
    }
  },
}
</script>

<style scoped>
@import "@/assets/css/layout/chat.css";
</style>