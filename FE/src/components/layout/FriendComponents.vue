<template>
  <div class="profile-scroll-container">
    <div class="profile-container" v-for="friend in myFriends" :key="friend">
      <div class="profile-image">
        <img src="@/assets/img/profile.png" alt="friend-profile" class="friend-profile">
        <img class="status" v-if="friend.online" src="@/assets/img/online.png" alt="status">
        <img class="status" v-else src="@/assets/img/offline.png" alt="status">
      </div>
      <div class="profile-info">
        <h4>{{ friend.nickname }}님</h4>
        <p @click="openChatRoom(friend)">채팅방</p>
      </div>
    </div>
  </div>
</template>

<script>
import { useFriendStore } from "@/store/friendStore";

export default {
  data() {
    return {
    }
  },
  computed: {
    // friends 데이터를 computed 속성으로 정의하여 스토어의 상태를 컴포넌트에 연결합니다.
    myFriends() {
      return useFriendStore().friends;
    }
  },

  mounted() {
    useFriendStore().getFriend()
  },

  methods: {
    openChatRoom(friend) {
      this.$emit('open-chat-room', friend)
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/layout/friend.css";
</style>