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
      <div
        v-for="seatKey in Object.keys(team.users)"
        :key="seatKey"
      >
        <div v-if="!team.users[seatKey].nickname">
          <img src="@/assets/img/profile_picture.png" class="team-img" />
          <span>비어있음</span>
        </div>
        <div v-else>
          <img src="@/assets/img/profile_picture.png" class="team-img" />
          <span>{{ team.users[seatKey].nickname }}</span>
          <!-- {{ team.users[seatKey].ready }} -->
        </div>

        <!-- 추방은 후순위 -->
        <!-- <div>
          <button class="ban" @click="banActive">추방</button>
        </div> -->
      </div>
    </div>
    <GameWaitChatVue />
  </div>
</template>

<script>
import { useRoomStore } from "@/store/roomStore";

// 아이콘
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserPlus } from "@fortawesome/free-solid-svg-icons/faUserPlus";
library.add(faUserPlus);

// 자식 컴포넌트
import GameWaitChatVue from "./GameWaitChat.vue";

export default {
  components: {
    GameWaitChatVue,
  },

  mounted() {
    // seatInfo 데이터를 가져와서 팀에 할당
    let seatInfo = useRoomStore().seatInfo;

    // 홍팀에 1부터 3까지의 seatInfo 할당
    for (let i = 1; i <= 3; i++) {
      this.teams[0].users[`seatnum${i}`] = seatInfo[`seatnum${i}`];
    }

    // 청팀에 4부터 6까지의 seatInfo 할당
    for (let i = 4; i <= 6; i++) {
      this.teams[1].users[`seatnum${i}`] = seatInfo[`seatnum${i}`];
    }

    console.log(this.teams);
  },

  data() {
    return {
      // 팀 정보 기본 세팅
      teams: [
        {
          name: "홍팀",
          users: [],
          maxUser: 3,
        },
        {
          name: "청팀",
          users: [],
          maxUser: 3,
        },
      ],
    };
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
        const redTeamSlot = this.teams[0].users.find(
          (user) => user.name === null
        );
        if (redTeamSlot) {
          redTeamSlot.name = userInfo.userNickname;
          redTeamSlot.status = "대기 중...!";
        } else {
          const blueTeamSlot = this.teams[1].users.find(
            (user) => user.name === null
          );
          if (blueTeamSlot) {
            blueTeamSlot.name = userInfo.userNickname;
            blueTeamSlot.status = "대기 중...";
          } else {
            console.log("두 팀 모두 꽉 찼습니다.");
          }
        }
      } catch (error) {
        console.error("Error parsing userInfo:", error);
      }
    },

    banActive() {
      this.$emit();
    },
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/waitingRoomLeft.css";
</style>