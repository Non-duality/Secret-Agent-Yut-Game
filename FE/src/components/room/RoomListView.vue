<template>
  <div class="room-info">
    <div>
      <h2>방 목록</h2>
      <RoomList 
      @show-room-info="handleShowRoomInfo"
      />
    </div>
    <div class="room-make-relate">
      <h2>세부사항</h2>
      <RoomInfo 
      v-if="isShow" 
      :room-detail="roomDetail"
      />
      <button class="room-make-btn">방 생성</button>
    </div>
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
        isShow: false,
        roomDetail: null
      }
    },

    methods: {
      handleShowRoomInfo(roomInfo) {
        this.roomDetail = roomInfo 
        this.isShow = true
      }
    }
    
  }
</script>

<style scoped>
@import "@/assets/css/room/roomListView.css";
</style>