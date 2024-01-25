<template>
  <!-- 닉네임 변경 모달 창 -->
  <div id="changeNicknameModal" class="modal">
    <div class="modal-content">
      <input type="text" placeholder="|" v-model="nickname" />
      <h4 class="nickname-text">변경할 닉네임을 입력하시오</h4>
      <button @click="closeModal('Nick')" class="modal-change-nickname-btn">확인</button>
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
    nicknameChangeTest() {
      this.$emit("nicknameChangeTest")
    },

    // // 서버로 새로운 닉네임을 전송하고 업데이트하는 로직을 추가해야함.
    // // axios 요청
    // changNickname() {
    //     axios.post('/api/change-nickname', { newNickname: this.newNickname })
    //     .then(response => {
    //       // 업데이트 시 로직
    //     })
    //     .catch(error => {

    //     });
    // },
  }
}
</script>

<style>
@import "@/assets/css/user/changeNick.css";
</style>