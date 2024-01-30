<template>
  <div class="profile-scroll-container">
    <div class="profile-container" v-for="friend in myFriend" :key="friend">
      <div class="profile-image">
        <img src="@/assets/img/profile.png" alt="friend-profile" class="friend-profile">
        <img class="status" v-if="isActive" src="@/assets/img/online.png" alt="status">
        <img class="status" v-else src="@/assets/img/offline.png" alt="status">
      </div>
      <div class="profile-info">
        <h4>{{ friend }}님</h4>
        <p>채팅</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    const myFriend = ["희웅", "준희", "지훈", "성규", "수빈", "주미"]
    return {
      myFriend,
      isActive: true,
    }
  },

  methods: {

  }
}
</script>

<style scoped>
@import "@/assets/css/layout/friend.css";
</style>