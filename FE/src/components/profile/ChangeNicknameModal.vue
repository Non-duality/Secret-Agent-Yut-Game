<template>
  <!-- 닉네임 변경 모달 창 -->
  <div id="changeNicknameModal" class="modal">
    <div class="modal-content">
      <input type="text" placeholder="|" v-model="nickname" />
      <h4 class="nickname-text">변경할 닉네임을 입력하시오</h4>
      <button @click="nicknameChange" class="modal-change-nickname-btn">확인</button>
    </div>
  </div>

</template>

<script>
import { useUserStore } from "@/store/userStore";

export default {
  setup() {
    const store = useUserStore();

    return {
      closeModal: store.closeModal,
    };
  },
  data() {
    return {
      nickname: '',
    };
  },
  methods: {
    nicknameChange() {
      if(this.nickname.length < 3)  return;

      useUserStore().changeNick(this.nickname);
      this.closeModal('Nick');
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/profile/changeNick.css";
</style>