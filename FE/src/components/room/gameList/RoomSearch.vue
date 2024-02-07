<template>
  <div>
    <!-- 방 검색 form -->
    <form class="room-search-form" @submit.prevent="searchRooms">
      <label for="room-search-input"></label>
      <input
        v-model="searchQuery"
        placeholder="검색어를 입력하시오"
        type="text"
        id="room-search-input"
        class="room-search-input"
      />
      <font-awesome-icon
        :icon="['fas', 'magnifying-glass']"
        style="color: #000000"
        class="room-search-btn"
      />
    </form>

    <!-- 임시: 검색된 방 목록 출력 -->
    <!-- <div v-if="searchQuery !== ''">
      <div v-if="filteredRooms.length > 0">
        <div v-for="room in filteredRooms" :key="room.id">
          <p>{{ room.title }}</p>
        </div>
      </div>
      <div v-else>
        <p>검색 결과가 없습니다.</p>
      </div>
    </div> -->
  </div>
</template>

<script>
import { useRoomStore } from "@/store/roomStore";

// 아이콘
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { library } from "@fortawesome/fontawesome-svg-core";
library.add(faMagnifyingGlass);

export default {
  data() {
    return {
      searchQuery: "",

      // DB에서 받아온 방 정보
      // roomList: [],
    };
  },

  // computed: {
  //   filteredRooms() {
  //     const lowerCaseQuery = this.searchQuery.toLowerCase();
  //     return this.roomList.filter((room) =>
  //       room.title.toLowerCase().includes(lowerCaseQuery)
  //     );
  //   },
  // },

  // created() {
  //   this.roomList = useRoomStore().roomList;
  // },

  methods: {
    searchRooms() {
      // 검색어를 부모 컴포넌트로 전달
      this.$emit("search", this.searchQuery);
    },
  },
};
</script>

<style scoped>
@import "../../../assets/css/room/roomSearch.css";
</style>
