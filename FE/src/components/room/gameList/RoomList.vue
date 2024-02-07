<template>
  <!-- 실제 게임 방 목록 -->
  <div class="room-list">
    <div class="room-search">
      <!-- 게임 방 검색바 -->
      <RoomSearch />
      <!-- 게임 방 새로고침 아이콘 -->
      <font-awesome-icon
        @click="refresh"
        :class="{ 'rotate-animation': isRotating }"
        :icon="['fas', 'arrows-rotate']"
        size="2xl"
        style="color: #ffffff; margin-right: 50px"
      />
    </div>
    <div>
      <div class="room-list-subtitle">
        <th scope="col">인원</th>
        <th scope="col">게임방 이름</th>
        <th scope="col">공개</th>
      </div>
      <div class="room-list-content">
        <RoomListComponent
          v-for="roomInfo in roomInfos"
          :key="roomInfo.roomId"
          :roomInfo="roomInfo"
          @click="selectRoom(roomInfo)"
          :class="{
            'room-list-component-selected': roomInfo.roomId === selectedRoomId,
          }"
        />
      </div>
    </div>
  </div>
</template>

<script>
// 방 정보 데이터
import { useRoomStore } from "@/store/roomStore";

// 아이콘
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { library } from "@fortawesome/fontawesome-svg-core";
library.add(faArrowsRotate);

// 자식 컴포넌트
import RoomSearch from "./RoomSearch.vue";
import RoomListComponent from "./RoomListComponent.vue";

export default {
  components: {
    RoomSearch,
    RoomListComponent,
  },

  data() {
    return {
      isRotating: false,
      selectedRoomId: null,
    };
  },

  computed: {
    // 방 정보를 실시간으로 계산
    roomInfos() {
      return useRoomStore().roomList;
    },
  },

  methods: {
    // 방 디테일을 나타내는 함수
    showRoomInfo(roomInfo) {
      console.log("실시간 계산");
      this.$emit("showRoomInfo", roomInfo);
    },

    refresh() {
      this.isRotating = true;
      // 애니메이션 종료 후 다시 false로
      setTimeout(() => {
        this.isRotating = false;
        const roomStore = useRoomStore();
        roomStore.getRoomSomeListData();
      }, 2000);
    },

    // 방 선택 로직 추가
    selectRoom(roomInfo) {
      if (this.selectedRoomId === roomInfo.roomId) {
        // 이전에 선택한 방과 현재 클릭한 방이 같은 경우 아무 동작도 하지 않고 리턴
        return;
      }

      // 이전에 선택한 방과 현재 클릭한 방이 다른 경우에만 선택 상태를 변경
      this.selectedRoomId = roomInfo.roomId;
      this.$emit("show-room-info", roomInfo.roomId);
    },
  },

  mounted() {
    setTimeout(() => {
      if (this.roomInfos.length > 0) {
        this.selectedRoomId = this.roomInfos[0].roomId;
        this.$emit("show-room-info", this.selectedRoomId);
      }
    }, 100);
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/roomList.css";
</style>