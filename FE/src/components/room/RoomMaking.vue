<template>
  <!-- 방 생성 모달 -->
  <div class="room-make-modal">
    <!-- 방 생성 form -->
    <h2>방 생성</h2>
    <form class="room-make-form" @submit.prevent>
      <div class="form-group">
        <label for="title">방 제목</label>
        <input type="text" id="title" maxlength="30" v-model="roomInfo.title">
      </div>
      <div class="form-group">
        <label for="isPublic">공개 여부</label>
        <div>
          공개 <input type="radio" name="isPublic" id="public" value="public" v-model="roomInfo.isPublic">
          비공개 <input type="radio" name="isPublic" id="private" value="private" v-model="roomInfo.isPublic">
        </div>
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="text" id="password" :disabled="isPublic === 'public'" maxlength="10" v-model="roomInfo.password">
      </div>
      <div class="form-group">
        <label for="theme">맵 테마</label>
        <div>
          <select name="theme" id="theme" v-model="roomInfo.theme">
            <option value="">테마 선택</option>
            <option value="lunaNewYear">설날</option>
            <option value="Chuseok">추석</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label for="speed">속도</label>
        <div>
          느림 <input type="radio" name="speed" id="slow" value="slow" v-model="roomInfo.speed">
          보통 <input type="radio" name="speed" id="normal" value="normal" v-model="roomInfo.speed">
          빠름 <input type="radio" name="speed" id="fast" value="fast" v-model="roomInfo.speed">
        </div>
      </div>
      <div class="button-group">
        <button type="button" 
        class="cancle" 
        @click="$emit('roomMakeCancel')">취소</button>
        <button type="submit" 
        class="create"
        @click="handleSubmit(roomInfo, user)">생성</button>
      </div>
    </form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        // 생성할 방 정보
        roomInfo: {
          title: '',
          isPublic: 'public',
          password: null,
          theme: '',
          speed: 'normal',
        }
      }
    },

    methods: {
      handleSubmit(roomInfo, user) {

      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomMaking.css";
</style>