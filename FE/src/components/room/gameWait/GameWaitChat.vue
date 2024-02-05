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
          <span class="chat-name">{{ message.name }}</span>
          <span>:</span>
          <span class="chat-chat">{{ message.chat }}</span>
        </div>
      </div>
    </div>
    <div class="chat-input-div">
      <span style="margin-left: 30px">user1</span>
      <span style="margin-left: 20px">:</span>
      <input class="chat-input" type="text" placeholder="입력하시오">
    </div>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  setup() {
    const chat = ref([
      {
        name: "user1",
        chat: "어서 시작하자구!!",
      },
      {
        name: "user2",
        chat: "빨리 하자!!!",
      },
      {
        name: "user1",
        chat: "ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ",
      },
      {
        name: "user5",
        chat: "ㄱㄷ",
      },
      {
        name: "user4",
        chat: "가자갖갖",
      },
      {
        name: "user2",
        chat: "빨리 하자!!!",
      },
    ]);

    return { chat };
  },
};
</script>

<style scoped>
@import "@/assets/css/room/GameWaitChat.css";
</style>