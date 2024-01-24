<template>
  <div class="room-list">
    <RoomList 
    @show-room-info="handleShowRoomInfo"
    />
    <div>
      <RoomInfo 
      v-if="isShow" 
      :room-detail="roomDetail"
      />
      <button>방 생성</button>
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

<style>
@import "@/assets/css/room/roomList.css";
</style>