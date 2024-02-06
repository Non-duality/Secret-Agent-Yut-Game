<template>
  <div class="wait-chat">
    <!-- 채팅 로그 -->
    <div class="chat-container">
      <div v-for="item in roomChat" :key="item">
        {{ item }}
      </div>
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
      roomChat: [],

      msg: "",
    };
  },

  // 여기서 chat 내용 계속 업데이트
  created() {
    if (useRoomStore().roomChatMessages.length > 0) {
      this.roomChat = useRoomStore().roomChatMessages;
    }
  },

  methods: {
    sendLocalMessage() {
      useRoomStore().sendMessage(this.msg);
      this.msg = "";
    },
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/GameWaitChat.css";
</style>