<template>
  <!-- 실제 게임 방 목록 -->
  <div class="room-list">
    <div class="room-search">
      <!-- 게임 방 검색바 -->
      <RoomSearch />
      <!-- 게임 방 새로고침 아이콘 -->
      <font-awesome-icon :icon="['fas', 'arrows-rotate']" size="2xl" style="color: #ffffff;" />
    </div>
    <div class="room-list-subtitle">
      <th scope="col">플레이어</th>
      <th scope="col">게임방 이름</th>
      <th scope="col">공개</th>
    </div>
    <div class="room-list-content">
      <!-- 실제 게임 방 요약 정보 v-for를 통한 반복 컴포넌트 구성 -->
      <!-- 나중에 페이지네이션이나 스크롤로 바꿔야 함 -->
      <RoomListComponent 
      v-for="roomInfo in roomInfos" 
      :key="roomInfo.id"
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

    computed: {
      roomInfos() {
        const roomStore = useRoomStore();
        return roomStore.roomListData;
      }
    },

    methods: {
      // 방 디테일을 나타내는 함수
      showRoomInfo(roomInfo) {
        this.$emit('showRoomInfo', roomInfo)
        
      }
    },

  }
</script>

<style scoped>
@import "@/assets/css/room/roomList.css";
</style>