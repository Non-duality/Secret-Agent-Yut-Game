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
          <!-- 기본 프로필, 유저 들어오면 유저의 프로필 사진 넣기! -->
          <img v-if="user.name === null" src="@/assets/img/profile_picture.png" class="team-img" />
          {{ user.name }}  {{ user.status }}
          <div>
            <!-- 친구 추가 (친구인지 판단하는 로직 넣어야 함) -->
            <font-awesome-icon :icon="['fas', 'user-plus']" 
            size="xl" style="color: #ffffff;" 
            class="icon-user-plus" 
            v-if="user.name !== null"/>
            <!-- 강퇴 (방장 자신은 안뜨게 해야하는데 이건 user 정보가 필요) -->
            <font-awesome-icon :icon="['far', 'circle-xmark']" 
            size="xl" style="color: #ffffff;" 
            class="icon-circle-xmark" 
            v-if="isManager"/>
          </div>
        </div>
      </div>
    </div>
    <GameWaitChatVue />
  </div>
</template>

<script>
// 아이콘
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserPlus } from "@fortawesome/free-solid-svg-icons/faUserPlus";
import { faCircleXmark } from "@fortawesome/free-regular-svg-icons";
library.add(faUserPlus, faCircleXmark)

// 자식 컴포넌트
import GameWaitChatVue from "./GameWaitChat.vue";

export default {
  components: {
    GameWaitChatVue
  },
  data() {
    return {
      // 팀 정보 기본 세팅
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
      ],

      // 방장 여부
      isManager: false,
    };
  },

  // 들어올 때 user 세팅 (추후 수정 필요)
  created() {
    this.fetchUserFromQuery();
    // URL query의 isManager 가져와서 방장 여부 판단
    this.isManager = this.$route.query.isManager === 'true';
  },

  methods: {
    // user 세팅 (추후 수정 필요)
    fetchUserFromQuery() {
      const userInfoString = this.$route.query.userInfo;
      if (!userInfoString) return;

      try {
        // 문자열 파싱 (추후 수정 필요)
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