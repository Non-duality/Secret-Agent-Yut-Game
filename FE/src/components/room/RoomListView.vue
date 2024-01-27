<template>
  <div class="room-info">
    <!-- 방 목록 -->
    <div class="room-list">
      <h2>방 목록</h2>
      <RoomList 
      @show-room-info="handleShowRoomInfo"
      />
    </div>
    <!-- 방 클릭 시 나오는 상세 정보 및 방 생성 -->
    <div class="room-list">
      <h2>세부사항</h2>
      <div class="room-detail-make">
        <RoomInfo 
        v-if="isShowDetail" 
        :room-detail="roomDetail"
        />
        <button @click="makeRoom" class="room-make-btn">방 생성</button>
      </div>
    </div>
    <RoomMaking 
    v-if="isShowMaking"
    class="room-make-modal"
    @room-make-cancel="makeRoom"/>
  </div>
</template>

<script>
  // 컴포넌트
  import RoomList from '@/components/room/RoomList.vue';
  import RoomInfo from '@/components/room/RoomInfo.vue';
  import RoomMaking from '@/components/room/RoomMaking.vue';

  export default {
    components: {
      RoomList,
      RoomInfo,
      RoomMaking
    },

    data() {
      return {
        isShowDetail: false,
        isShowMaking: false,
        roomDetail: null
      }
    },

    methods: {
      // 방 상세 정보 모달 띄우기
      handleShowRoomInfo(roomInfo) {
        this.roomDetail = roomInfo 
        this.isShowDetail = true 
      },

      // 방 생성 모달 띄우거나 취소하기
      makeRoom() {
        this.isShowMaking = !this.isShowMaking
      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomListView.css";
</style>