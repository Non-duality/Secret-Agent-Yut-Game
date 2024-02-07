<template>
  <div class="wait-chat">
    <!-- 채팅 로그 -->
    <div class="chat-container">
      <div v-for="(message, index) in reversedRoomChat" :key="index">
        <span :style="{ color: getColorForMessage(message).color, textAlign: 'left' }" v-html="getColorForMessage(message).text"></span>
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
import { useRoomStore } from "@/store/roomStore";
import { useUserStore } from "@/store/userStore";
import { socketSend } from "@/util/socket";

export default {
  data() {
    return {
      msg: "",
    };
  },

  // 여기서 chat 내용 계속 업데이트
  computed: {
    roomChat() {
      return useRoomStore().roomChatMessages;
    },
    reversedRoomChat() {
      // roomChat 배열을 뒤집어 반환
      return this.roomChat.slice().reverse();
    },
  },

  methods: {
    sendLocalMessage() {
      if (this.msg === "") return;

      const tempMSG = {
        nickname: useUserStore().userInfo.nickname,
        message: this.msg,
      };

      socketSend(
        "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/chat",
        tempMSG
      );

      this.msg = "";
    },

    // 메시지의 종류에 따라 색상을 반환하는 메서드
    getColorForMessage(message) {
      if (message.includes("님이 입장하였습니다.")) {
        return { color: "red", text: message };
      } else {
        const parts = message.split(":"); // ":"를 기준으로 메시지를 분할

        // 이름과 내용이 존재하는 경우
        return {
          color: "purple",
          text: `<span style="color: purple; text-align: left;">${parts[0]}</span>: <span style="color: white;">${parts[1]}</span>`,
        };
      }
    },
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/GameWaitChat.css";
</style>
