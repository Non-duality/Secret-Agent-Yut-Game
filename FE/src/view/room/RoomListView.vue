<template>
  <div class="room-info">
    <!-- 방 목록 -->
    <div>
      <h2>방 목록</h2>
      <RoomList @show-room-info="handleShowRoomInfo" class="room-list-box" />
    </div>
    <div class="room-list">
      <!-- 방 세부사항 및 생성 버튼 -->
      <h2>세부사항</h2>
      <div class="room-detail-make">
        <RoomInfo v-if="ShowDetail" :room-info="roomInfo" />
        <RoomInfo v-else :room-info="defaultRoomInfo" />
        <button @click="openModal('roomMaking')" class="room-make-btn">
          방 생성
        </button>
      </div>
    </div>
    <!-- 방 생성 모달 -->
    <transition name="fade">
      <RoomMakingModal
        v-if="showRoomMaking"
        class="room-make-modal"
        @room-make-cancel="makeRoom"
      />
    </transition>
    <!-- 비공개방 비밀번호 체크 모달 -->
    <RoomPasswordCheckModal
      v-if="showRoomPasswordCheckModal"
      class="room-password-modal"
      :room-info="defaultRoomInfo"
    />
  </div>
</template>

<script>
// 컴포넌트
import RoomList from "@/components/room/gameList/RoomList.vue";
import RoomInfo from "@/components/room/gameList/RoomInfo.vue";
import RoomMakingModal from "@/components/room/gameList/RoomMakingModal.vue";
import RoomPasswordCheckModal from "@/components/room/gameList/RoomPasswordCheckModal.vue";

import { useRoomStore } from "@/store/roomStore";

export default {
  components: {
    RoomList,
    RoomInfo,
    RoomMakingModal,
    RoomPasswordCheckModal,
  },

  data() {
    return {
      ShowDetail: false,
      roomInfo: null,
    };
  },

  computed: {
    defaultRoomInfo() {
      const roomStore = useRoomStore();
      return roomStore.roomListData[0] || {};
    },

    // 방 생성 모달
    showRoomMaking() {
      const roomStore = useRoomStore();
      return roomStore.showRoomMakingModal;
    },

    computed: {
      defaultRoomInfo() {
        const roomStore = useRoomStore();
        return roomStore.roomListSomeData[0] || {};
      },
      // 비공개방 비밀번호 체크 모달
      showRoomPasswordCheckModal() {
        const roomStore = useRoomStore();
        return roomStore.showRoomPasswordCheckModal;
      },
    },

    methods: {
      // 방 상세 정보 띄우기
      handleShowRoomInfo(roomInfo) {
        this.roomInfo = roomInfo;
        this.ShowDetail = true;
      },

      methods: {
        // 방 상세 정보 띄우기
        handleShowRoomInfo(roomInfo) {
          this.roomInfo = roomInfo;
          this.ShowDetail = true;
        },

        // 모달 관리
        openModal(modalType) {
          const roomStore = useRoomStore();
          roomStore.openModal(modalType);
        },
      },

      created() {
        const roomStore = useRoomStore();
        roomStore.getRoomSomeListData();
      },
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/room/roomListView.css";
</style>