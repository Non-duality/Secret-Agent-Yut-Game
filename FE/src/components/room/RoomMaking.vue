<template>
  <!-- 방 생성 모달 -->
  <div class="room-make-modal">
    <!-- 방 생성 form -->
    <h2>방 생성</h2>
    <form class="room-make-form" @submit.prevent>
      <div class="form-group">
        <label for="title">방 제목</label>
        <!-- 악성 유저 고려해서 방 제목 길이 제한 -->
        <input type="text" id="title" maxlength="30" v-model="roomInfo.title">
      </div>
      <div class="form-group">
        <label for="isPublic">공개 여부</label>
        <div>
          공개 <input type="radio" name="isPublic" id="public" :value="true" v-model="roomInfo.isPublic">
          비공개 <input type="radio" name="isPublic" id="private" :value="false" v-model="roomInfo.isPublic">
        </div>
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <!-- 악성 유저 고려해서 비밀번호 길이 제한 -->
        <input type="text" id="password" :disabled="roomInfo.isPublic" maxlength="10" v-model="roomInfo.password">
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
          느림 <input type="radio" name="speed" id="slow" value="1" v-model="roomInfo.speed">
          보통 <input type="radio" name="speed" id="normal" value="2" v-model="roomInfo.speed">
          빠름 <input type="radio" name="speed" id="fast" value="3" v-model="roomInfo.speed">
        </div>
      </div>
      <div class="button-group">
        <button type="button" 
        class="cancle" 
        @click="$emit('roomMakeCancel')">취소</button>
        <button type="submit" 
        class="create"
        @click="roomMake(roomInfo, user)">생성</button>
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
          isPublic: true,
          password: null,
          theme: '',
          speed: 2,
        }
      }
    },

    methods: {
      roomMake(roomInfo) {
        if (roomInfo.title === '') {
          alert('방 제목을 작성해주세요')
        } else if (roomInfo.isPublic === false && roomInfo.password === null) {
          alert('비공개 방으로 비밀번호를 입력하세요')
        } else if (roomInfo.theme === '') {
          alert('테마를 선택하세요')
        } else {

        }
      }
    },

    // 공개 여부를 계속 감시
    watch: {
      'roomInfo.isPublic': function(newValue) {
        if (newValue) {
          this.roomInfo.password = ''; // 공개면 비밀번호를 초기화
        }
      }
    }
  }
</script>

<style scoped>
@import "@/assets/css/room/roomMaking.css";
</style>