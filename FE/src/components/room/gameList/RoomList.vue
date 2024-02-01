<template>
  <!-- 실제 게임 방 목록 -->
  <div class="room-list">
    <div class="room-search">
      <!-- 게임 방 검색바 -->
      <RoomSearch />
      <!-- 게임 방 새로고침 아이콘 -->
      <font-awesome-icon @click="refresh" :class="{ 'rotate-animation': isRotating }" :icon="['fas', 'arrows-rotate']" size="2xl" style="color: #ffffff;" />
    </div>
    <div class="room-list-subtitle">
      <th scope="col">플레이어</th>
      <th scope="col">게임방 이름</th>
      <th scope="col">공개</th>
    </div>
    <div class="room-list-content">
      <RoomListComponent 
      v-for="roomInfo in roomInfos" 
      :key="roomInfo.roomId"
      :roomInfo="roomInfo"
      @click="$emit('showRoomInfo', roomInfo)"
      class="room-list-component"
        />
    </div>
  </div>
</template>

<script>
  // 방 정보 데이터
  import { useRoomStore } from '@/store/roomStore'

  // 아이콘
  import { faArrowsRotate } from '@fortawesome/free-solid-svg-icons';
  import { library } from '@fortawesome/fontawesome-svg-core';
  library.add(faArrowsRotate)

  // 자식 컴포넌트
  import RoomSearch from './RoomSearch.vue';
  import RoomListComponent from './RoomListComponent.vue';

  export default {
    components: {
      RoomSearch,
      RoomListComponent,
    },

    data() {
      return {
        isRotating: false
      }
    },

    computed: {
      // 방 정보를 실시간으로 계산
      roomInfos() {
        const roomStore = useRoomStore();
        return roomStore.roomListSomeData;
      }
    },

    methods: {
      // 방 디테일을 나타내는 함수
      showRoomInfo(roomInfo) {
        this.$emit('showRoomInfo', roomInfo)
      },

      refresh() {
        this.isRotating = true;
        // 애니메이션 종료 후 다시 false로
        setTimeout(() => {
          this.isRotating = false;
        }, 2000)
        
        const roomStore = useRoomStore();
        roomStore.getRoomSomeListData()

      }
    },

  }
</script>

<style scoped>
@import "@/assets/css/room/roomList.css";
</style>