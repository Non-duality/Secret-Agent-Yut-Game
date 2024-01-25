<template>
  <div class="wait-left">
    <!-- 백에서 받아온 정보를 for문을 돌려 나타낸다. -->
    <div
      v-for="team in teams"
      :key="team.name"
      :class="{
        'team-container': true, // 큰 틀
        'team-red': team.name === '홍팀', // 홍팀
        'team-blue': team.name === '청팀', // 청팀 의 색을 다르게 하기 위해 선언
      }"
    >
      <!-- 팀 이름을 나타낸다. -->
      <div class="team-name">{{ team.name }}</div>
      <!-- 유저의 정보를 나타낸다 -->
      <div v-for="user in team.users" :key="user.name" class="user-container">
        <div class="team-user">
          <img src="@/assets/img/profile_picture.png" class="team-img" />
          {{ user.name }} - {{ user.status }}
        </div>
      </div>
    </div>
    <GameWaitChatVue />
  </div>
</template>

<script>
import { ref } from "vue";
import GameWaitChatVue from "./GameWaitChat.vue";

export default {
  components: {
    GameWaitChatVue,
  },
  setup() {
    const teams = ref([
      {
        name: "홍팀",
        users: [
          { name: "user1", status: "준비!" },
          { name: "user2", status: "대기중..." },
          { name: "user33", status: "대기중..." },
        ],
      },
      {
        name: "청팀",
        users: [
          { name: "user4", status: "준비!" },
          { name: "user5", status: "대기중..." },
          { name: "user6", status: "대기중..." },
        ],
      },
    ]);

    return { teams };
  },
};
</script>

<style>
@import "@/assets/css/room/waitingRoomLeft.css";
</style>