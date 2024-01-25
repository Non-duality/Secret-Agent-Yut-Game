<template>
  <div>
    <h2>방 목록</h2>
    <div>
      <RoomSearch />
      <button>새로고침</button>
    </div>
    <table>
      <tr>
        <th scope="col">플레이어</th>
        <th scope="col">게임방 이름</th>
        <th scope="col">공개</th>
      </tr>
      <RoomListComponent 
      v-for="roomInfo in roomInfos" 
      :key="roomInfo.id"
      :roomInfo="roomInfo"
      @click="$emit('showRoomInfo', roomInfo)"/>
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

<style>

</style>