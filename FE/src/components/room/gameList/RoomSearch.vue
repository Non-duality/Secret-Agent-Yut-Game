<template>
  <div>
    <!-- 방 검색 form -->
    <form class="room-search-form">
      <label for="room-search-input"></label>
      <input placeholder="검색어를 입력하시오" type="text" id="room-search-input" class="room-search-input">
      <font-awesome-icon :icon="['fas', 'magnifying-glass']" style="color: #000000;" class="room-search-btn"/>
    </form>
  </div>
</template>

<script>
  // 아이콘
  import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
  import { library } from '@fortawesome/fontawesome-svg-core';
  library.add(faMagnifyingGlass)
  export default {
}
</script>

<style scoped>
@import "@/assets/css/room/roomSearch.css";
</style>