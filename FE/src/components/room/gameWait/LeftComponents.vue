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
          {{ user.name }}  {{ user.status }}
        </div>
      </div>
    </div>
    <GameWaitChatVue />
  </div>
</template>

<script>
import GameWaitChatVue from "./GameWaitChat.vue";

export default {
  components: {
    GameWaitChatVue
  },
  data() {
    return {
      teams: [
        { 
          name: "홍팀", 
          users: [
            { name: null, status: null },
            { name: null, status: null },
            { name: null, status: null },
          ], 
          maxUser: 3 
        },
        { 
          name: "청팀", 
          users: [
            { name: null, status: null },
            { name: null, status: null },
            { name: null, status: null },
          ], 
          maxUser: 3 }
      ]
    };
  },

  // 들어올 때 fetchUserFromQuery 실행 시키기!
  created() {
    this.fetchUserFromQuery();
  },

  methods: {
    fetchUserFromQuery() {
      console.log('Route query:', this.$route.query);
      const userInfoString = this.$route.query.userInfo;
      if (!userInfoString) return;

      try {
        // 문자열 파싱
        const userInfo = JSON.parse(userInfoString);
        // 홍팀부터 넣고 홍팀 다 차면 청팀에 넣기
        const redTeamSlot = this.teams[0].users.find(user => user.name === null);
        if (redTeamSlot) {
          redTeamSlot.name = userInfo.userNickname;
          redTeamSlot.status = "대기 중...!"
        } else {
          const blueTeamSlot = this.teams[1].users.find(user => user.name === null);
          if (blueTeamSlot) {
            blueTeamSlot.name = userInfo.userNickname;
            blueTeamSlot.status = "대기 중...";
          } else {
            console.log('두 팀 모두 꽉 찼습니다.')
          }
        }
      } catch (error) {
        console.error('Error parsing userInfo:', error);
      }

    }
  }
};
</script>

<style>
@import "@/assets/css/room/waitingRoomLeft.css";
</style>