<template>
  <div class="wait-div">
    <!-- 왼쪽 사용자 화면이다 참가한 유저가 보이고 채팅이 보인다 -->
    <div class="wait-left">
      <LeftComponentsVue />
    </div>
    <!-- 방 상세 정보와 준비, 나가기 버튼이 있다 -->
    <div class="wait-right">
      <RightComponentsVue />
      <button v-if="isOwner" class="wait-btn" @click="gameStart">시작</button>
      <button v-else class="wait-btn" @click="doReady">준비</button>
      <button class="wait-btn" @click="goTolist">나가기</button>
    </div>
    <!-- <banModal @close-ban-modal="closeBanModal" v-if="ban"/> -->
  </div>
</template>

<script>
import { connectRoom, pubRoom, pubPick } from "@/util/socket.js";
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";

// 자식 컴포넌트
import RightComponentsVue from "@/components/room/gameWait/RightComponents.vue";
import LeftComponentsVue from "@/components/room/gameWait/LeftComponents.vue";
// import banModal from '@/components/room/gameWait/banModal.vue';

export default {
  components: {
    RightComponentsVue, // 방 상세
    LeftComponentsVue, // 참여 유저 정보
    // banModal // 금지 모달
  },

  data() {
    return {
      // 방 정보
      roomInfo: null,

      // 방 주인이지 확인
      isOwner: false,

      // 방을 나갔는지 확인
      isOut: false,
    };
  },

  created() {
    setTimeout(() => {
      let state = 0;

      Object.keys(useRoomStore().seatInfo).forEach((seatKey) => {
        if (
          useRoomStore().seatInfo[seatKey].nickname ===
          useUserStore().userInfo.nickname
        ) {
          state = useRoomStore().seatInfo[seatKey].state;
        }
      });

      if (state == 2) this.isOwner = true;
    }, 100);
  },

  methods: {
    // 픽창으로 게임 시작
    gameStart() {
      /* 자신의 자리 번호 */
      // (1~3) red, (4~6) blue
      let myTeamName = null;
      let canStart = true;

      // 한 번더 주인인지 체크함.
      if (this.isOwner) {
        const seatInfo = useRoomStore().seatInfo;
        const seatKeys = Object.keys(seatInfo);

        for (let i = 1; i < seatKeys.length; i++) {
          const seatKey = seatKeys[i];
          if (!seatInfo[seatKey].nickname) {
            alert("빈 자리가 존재합니다.");
            canStart = false;
            return;
            break;
          }
        }

        for (let i = 1; i < seatKeys.length; i++) {
          const seatKey = seatKeys[i];
          if (!seatInfo[seatKey].ready) {
            alert("모두 준비완료가 되지 않았습니다.");
            canStart = false;
            break;
          }
        }

        if (!canStart) return;

        // 게임 시작하는 메시지 알림.
        pubRoom(
          "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/start",
          useUserStore().userInfo.email
        );

        for (let i = 0; i < seatKeys.length; i++) {
          const seatKey = seatKeys[i];
          if (seatInfo[seatKey].nickname === useUserStore().userInfo.nickname) {
            if (0 <= i < 2) {
              myTeamName = "red";
            } else {
              myTeamName = "blue";
            }
            break;
          }
        }
      }

      connectRoom("Pick", this.$router, myTeamName).then(() => {
        pubPick(
          "/pub/pick/" + useRoomStore().currentRoomInfo.roomCode + "/get-info"
        );
      });
    },

    // 준비완료
    doReady() {
      pubRoom(
        "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/ready",
        useUserStore().userInfo.email
      );
    },

    // 나갈 때 로직
    goTolist() {
      const confirmMessage = "정말 나가시겠습니까?";

      if (confirm(confirmMessage)) {
        // 방을 나가기 위해 pub로 알린다.
        pubRoom(
          "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/exit",
          useUserStore().userInfo.email
        );

        // 구독 취소한 뒤 방 정보에 대해 모두 리셋한다.
        useRoomStore().subscription.room.unsubscribe();
        const initialStateRoom = useRoomStore().$reset();
        Object.assign(this, initialStateRoom);

        this.isOut = true;

        this.$router.push({ name: "home" });
      }
    },
  },

  unmounted() {
    if (!this.isOut) {
      // 방을 나가기 위해 pub로 알린다.
      pubRoom(
        "/pub/room/" + useUserStore().currentRoomInfo.roomCode + "/exit",
        useUserStore().userInfo.email
      );

      // 구독 취소한 뒤 방 정보에 대해 모두 리셋한다.
      useRoomStore().subscription.room.unsubscribe();
      const initialStateRoom = useRoomStore().$reset();
      Object.assign(this, initialStateRoom);
    }
  },
};
</script>

<style scoped>
@import "@/assets/css/room/waitingRoom.css";
</style>