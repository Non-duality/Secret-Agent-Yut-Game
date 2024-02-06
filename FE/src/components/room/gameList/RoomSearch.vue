<template>
  <div>
    <!-- 방 검색 form -->
    <form class="room-search-form" @submit.prevent="searchRooms">
      <label for="room-search-input"></label>
      <input v-model="searchQuery" placeholder="검색어를 입력하시오" type="text" id="room-search-input" class="room-search-input">
      <font-awesome-icon :icon="['fas', 'magnifying-glass']" style="color: #000000;" class="room-search-btn"/>
    </form>

    <!-- 임시: 검색된 방 목록 출력 -->
    <div v-for="room in filteredRooms" :key="room.id">
      <p>{{ room.title }}</p>
    </div>
  </div>
</template>

<script>
  // 아이콘
  import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
  import { library } from '@fortawesome/fontawesome-svg-core';
  import axios from 'axios';
  library.add(faMagnifyingGlass)

  export default {
    data() {
      return {
        searchQuery: '',
        // DB에서 받아온 방 정보
        rooms: []
      };
    },

    computed: {
      filteredRooms() {
        const lowerCaseQuery = this.searchQuery.toLowerCase();
        return this.rooms.filter(room => room.title.toLowerCase().includes(lowerCaseQuery));
      }
    },
    
    mounted() {
      this.getRooms()
    },

    methods: {
      async getRooms() {
        try {
          const { response } = await axios.get('url');
          // this.rooms = response.data;
        } catch (error) {
          console.error('error message:', error);
        }
      },
    }
  }

</script>

<style scoped>
@import "../../../assets/css/room/roomSearch.css";
</style>