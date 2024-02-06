<template>
  <div class="wait-chat">
    <div class="chat-container">
      <!-- 채팅을 받아오면 for문으로 출력한다. -->
      <!-- 나중에 실시간으로 받아오는 기능을 추가해야한다. -->
      <div
        v-for="(message, index) in chat"
        :key="index"
        :class="{ 'my-chat': message.name === 'user1' }"
      >
        <div class="chat-div">
          <!-- <span class="chat-name">{{ message.name }}</span>
          <span>:</span>
          <span class="chat-chat">{{ message.chat }}</span> -->
        </div>
      </div>
    </div>
    <div class="chat-input-div">
      <input
        class="chat-input"
        type="text"
        placeholder="메시지를 입력하시오."
        @keyup.enter="sendLocalMessage"
      />
      <button class="send-btn" @click="sendLocalMessage">
        <span>보내기</span>
      </button>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";

export default {
  data() {
    return {
      chat: [],

      msg: {},
    };
  },

  // 여기서 chat 내용 계속 업데이트
  created() {
    if (useRoomStore().roomChatMessages.length > 0) {
      this.chat = {
        ...useRoomStore().roomChatMessages,
      };
    }
  },

  methods: {
    sendLocalMessage() {
      this.msg = {};
      // useRoomStore().sendMessage();
    },
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/GameWaitChat.css";
</style>