<template>
  <!-- 실제 게임 방 목록 -->
  <div class="room-list">
    <div class="room-search">
      <!-- 게임 방 검색바 -->
      <RoomSearch />
      <!-- 게임 방 새로고침 아이콘 -->
      <font-awesome-icon :icon="['fas', 'arrows-rotate']" spin size="2xl" style="color: #ffffff;" />
    </div>
    <table class="room-content">
      <tr>
        <th scope="col">플레이어</th>
        <th scope="col">게임방 이름</th>
        <th scope="col">공개</th>
      </tr>
      <!-- 실제 게임 방 요약 정보 v-for를 통한 반복 컴포넌트 구성 -->
      <!-- 나중에 페이지네이션이나 스크롤로 바꿔야 함 -->
      <RoomListComponent 
      v-for="roomInfo in roomInfos" 
      :key="roomInfo.id"
      :roomInfo="roomInfo"
      @click="$emit('showRoomInfo', roomInfo)"
      class="room-list-component"
      />
    </table>
  </div>
</template>

<script>
  // 더미 데이터 
  let RoomData = [ 
    { id: 1, currentPlayers: 3, roomName: '하이룽', isPublic: false, speed: 1, theme: '설날' },
    { id: 2, currentPlayers: 1, roomName: '메롱', isPublic: true, speed: 3, theme: '설날' },
    { id: 3, currentPlayers: 6, roomName: '어쩔티비', isPublic: true, speed: 2, theme: '설날' }
  ]

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
        // 방 데이터 저장하는 배열
        roomInfos: []
      }
    },

    methods: {
      // 방 데이터를 집어 넣는 함수
      async enterRoomInfo() {
        if (this.roomInfos.length === 0) {
          RoomData.forEach(item => {
            this.roomInfos.push(item)
          })
        }
      },

      // 방 디테일을 나타내는 함수
      showRoomInfo(roomInfo) {
        this.$emit('showRoomInfo', roomInfo)
        
      }
    },

    mounted() {
      // 초기 방 목록 업데이트
      this.enterRoomInfo()
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomList.css";
</style>