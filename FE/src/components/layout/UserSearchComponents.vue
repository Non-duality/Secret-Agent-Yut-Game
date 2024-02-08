<template>
  <div class="search-main">
    <form class="search-form" @submit.prevent="searchUser">
      <label for="search-input"></label>
      <input
        v-model="searchName"
        placeholder="닉네임을 입력하시오"
        type="text"
        id="search-input"
        class="search-input"
      />
      <font-awesome-icon
        :icon="['fas', 'magnifying-glass']"
        style="color: #000000"
        class="search-btn"
      />
    </form>
    <div class="profile-container">
      <div class="profile-image">
        <img
          src="@/assets/img/profile.png"
          alt="friend-profile"
          class="friend-profile"
        />
        <img
          class="status"
          v-if="isActive"
          src="@/assets/img/online.png"
          alt="status"
        />
        <img
          class="status"
          v-else
          src="@/assets/img/offline.png"
          alt="status"
        />
      </div>
      <div class="profile-info">
        <h4>{{ User.nickName }}님</h4>
        <p>채팅</p>
      </div>
    </div>
  </div>
</template>

<script>
import { useFriendStore } from "@/store/friendStore";

export default {
  data() {
    return {
      searchName: "",
      isActive: true,
    };
  },
  methods: {
    searchUser() {
      const useFriend = useFriendStore();
      useFriend.searchUser(this.searchName);
    },
  },
  computed: {
    // friends 데이터를 computed 속성으로 정의하여 스토어의 상태를 컴포넌트에 연결합니다.
    User() {
      return useFriendStore().findUser;
    },
  },
};
</script>

<style>
@import "@/assets/css/layout/search.css";
</style>