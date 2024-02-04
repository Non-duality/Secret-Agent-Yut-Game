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
        <RoomInfo v-if="ShowDetail" :room-info="roomDetailInfo" />
        <RoomInfo v-else-if="ShowInitDetail" :room-info="roomInitDetailInfo" />
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
    />
  </div>
</template>

<script>
// 컴포넌트
import RoomList from "@/components/room/gameList/RoomList.vue";
import RoomInfo from "@/components/room/gameList/RoomInfo.vue";
import RoomMakingModal from "@/components/room/gameList/RoomMakingModal.vue";
import RoomPasswordCheckModal from "@/components/room/gameList/RoomPasswordCheckModal.vue";

import { useUserStore } from "@/store/userStore"
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
      ShowInitDetail: false,

      roomInfo: null,

      roomDetailInfo: null,
      roomInitDetailInfo: null,

      roomListLoaded: false,
      roomId: null,
    };
  },

  computed: {
    // 방 생성 모달
    showRoomMaking() {
      return useRoomStore().showRoomMakingModal;
    },

    // 비공개방 비밀번호 체크 모달
    showRoomPasswordCheckModal() {
      return useRoomStore().showRoomPasswordCheckModal;
    },
  },

  methods: {
    selectRoomInfo(val) {
      return new Promise(async (resolve) => {
        await useRoomStore().getRoomDetailData(val);
        this.roomDetailInfo = useRoomStore().roomDetailData;
        resolve();
      });
    },

    // 방 상세 정보 띄우기
    async handleShowRoomInfo(roomId) {
      useUserStore().roomId = roomId;
      await this.selectRoomInfo(roomId);
      this.ShowDetail = true;
    },

    // 모달 관리
    openModal(modalType) {
      const roomStore = useRoomStore();
      roomStore.openModal(modalType);
    },
  },

  async mounted() {
    this.ShowDetail = false;

    await useRoomStore().getRoomSomeListData();

    // 전체 방 리스트 조회
    this.roomInfo = useRoomStore().roomList;
    this.roomListLoaded = true;

    // 첫 번째 방만 상세 조회
    await this.selectRoomInfo(this.roomInfo[0].roomId);
    this.roomInitDetailInfo = useRoomStore().roomDetailData;
    this.ShowInitDetail = true;
  },
};
</script>

<style scoped>
@import "../../assets/css/room/roomListView.css";
</style>