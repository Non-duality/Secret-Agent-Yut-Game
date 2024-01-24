<template>
  <div>
    <h1>회원 정보 수정 페이지</h1>
    <img src="@/assets/img/profile_picture.png" alt="profile-img" />

    <h3>{{ user.nickname }}</h3>
    <button @type="submit" @click="openModal('Nick')">
      닉네임 변경
    </button>

    <!-- 닉네임 변경 모달 -->
    <transition name="fade">
      <ChangeNicknameModal v-if="showUserInfoNick" />
    </transition>

    <h4>이메일</h4>
    <span>이메일{{ user.email }}</span>
    <h4>비밀번호</h4>
    <span>{{ user.password }}</span>
    <h4>생년월일</h4>
    <span>{{ user.birthdate }}</span>
    <!-- 성별 체크박스로 넣어서 completed 정보로 표시하기 -->
    <div>
      <h4>성별</h4>
      <input type="checkbox" />
    </div>

    <button @type="submit" @click="withdraw">회원 탈퇴</button>
  </div>
</template>

<script>
import ChangeNicknameModal from "@/components/user/ChangeNicknameModal.vue";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";

export default {
  components: {
    ChangeNicknameModal,
  },
  setup() {
    const store = useUserStore();
    const { showUserInfoNick } = storeToRefs(store);

    return {
      showUserInfoNick,
      openModal: store.openModal,
    };
  },
  data() {
    return {
      user: {
        nickname: "간지작살",
        email: "ssafy@ssafy.com",
        password: "********",
        birthdate: "1998. 02. 01.",
        gender: "여성",
      },
    };
  },

  methods: {
    // 회원 탈퇴 메서드
    withdraw() {},
  },
};
</script>

<style scoped>
</style>