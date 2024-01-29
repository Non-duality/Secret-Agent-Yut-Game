<template>
  <div class="profile-scroll-container">
    <div class="profile-container" v-for="friend in myFriend" :key="friend">
      <div class="profile-image">
        <img src="@/assets/img/profile.png" alt="friend-profile" class="friend-profile">
      </div>
      <div class="profile-info">
        <h4>{{ friend }}님</h4>
        <img :src="isActive ? '../../assets/img/online.png' : '../../assets/img/offline.png'" alt="status-profile">
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
    closeSidebar() {
      this.$emit('closeSidebar');
    }
  }
}
</script>

<style scoped>
  .profile-scroll-container {
    max-height: 350px;
    overflow-y: auto;
  }

  .profile-container {
  display: flex;
  align-items: center;
  }

  .profile-image {
    margin-right: 10px;
  }

  .profile-info h4 {
    margin: 0;
    margin-bottom: 5px;
    color: white;
    font-size: 13px;
  }

  .profile-info p {
    color:rgb(194, 187, 187);
    margin: 0;
    font-size: 12px;
  }

  .friend-profile {
    width: 60px;
    height: 60px;
  }

  .profile-scroll-container::-webkit-scrollbar {
  width: 12px;
  }

  .profile-scroll-container::-webkit-scrollbar-thumb {
  background-color: rgba(177, 164, 164, 0.5);
  border-radius: 4px;
  }

  .profile-scroll-container::-webkit-scrollbar-track {
  background-color: rgba(243, 235, 235, 0.1);
  }
</style>