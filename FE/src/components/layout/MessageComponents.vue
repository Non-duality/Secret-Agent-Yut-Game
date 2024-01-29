<template>
  <div class="notification-container">
    <div v-for="friend in invitingFriends" :key="friend" class="friend-invite">
      <div class="frined-invite">
        <h4 class="title">친구 초대가 왔습니다.</h4>
        <p class="content">{{ friend }}님으로부터 친구 신청이 왔습니다.</p>
        <button class="accept" @click="acceptFriend(friend)">수락</button>
        <button class="refuse" @click="refureFriend(friend)">거절</button>
      </div>
    </div>
  
    <div v-for="chat in chatings" :key="chat" class="chat-notification">
      <div class="chating" @click="goChat(chat)">
        <h4 class="title">채팅이 왔습니다</h4>
        <p class="content">{{ chat }}님으로부터 채팅이 왔습니다. 확인해보세요!</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    // 더미데이터
    const invitingFriends = ["chan", "jun"]
    const chatings = ["jumi", "subin", "jihun", "junhee", "heeyoong", "seonggyu"]
    return {
      invitingFriends,
      chatings,
    };
  },

  methods: {
    closeSidebar() {
      this.$emit('closeSidebar');
    },

    async acceptFriend(user) {
      // 수락하는 로직
      // try {
      //   const { res } = await axios.post('url', user)
      //   console.log(res)
      // } catch (error) {
      //   console.log(error)
      // }
    },

    async refureFriend(user) {
      // 거절하는 로직
      // try {
      //   const { res } = await axios.delete('url', user)
      //   console.log(res)
      // } catch (error) {
      //   console.log(error)
      // }
    },
    // 채팅
    async goChat(user) { 
      // 채팅 온 사람과 채팅하러 가는 로직
      // try {
      //   const { res } = await axios.get('url', user)
      //   console.log(res)
      // } catch (error) {
      //   console.log(error)
      // }
    },
  }
}
</script>

<style scoped>
  @import "@/assets/css/layout/message.css";
</style>