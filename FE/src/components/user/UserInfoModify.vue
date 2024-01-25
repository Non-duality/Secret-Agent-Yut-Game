<template>
  <div class="user-profile">
    <div class="left-section">
      <img src="@/assets/img/profile_picture.png" alt="profile-img" class="profile-picture"/>
      <h3>{{ user.nickname }}</h3>
      <button @type="submit" @click="openModal('Nick')" class="nickname-change-btn">
        닉네임 변경
      </button>
    </div>

    <div class="right-section">
      <!-- 닉네임 변경 모달 -->
      <transition name="fade">
        <ChangeNicknameModal v-if="showUserInfoNick" />
      </transition>
  
      <h4>이메일 {{ user.email }}</h4>
      <h4>비밀번호 {{ user.password }}</h4>
      <h4>생년월일 {{ user.birthdate }}</h4>
      <!-- 성별 체크박스로 넣어서 completed 정보로 표시하기 -->
      <div class="gender-font">
        <label for="male">성별 </label>
        <input type="checkbox" id="male"/>
        <label for="male">남성</label>
        <input type="checkbox" id="femail"/>
        <label for="female">여성</label>
      </div>
      <button @type="submit" @click="openModal('out')" class="memberout-btn">회원 탈퇴</button>
    </div>
    <transition>
      <DropoutModal v-if="showDropOutModal" @close="showDropOutModal = false"/>
    </transition>
  </div>
</template>

<script>
import ChangeNicknameModal from "@/components/user/ChangeNicknameModal.vue";
import DropoutModal from "@/components/user/DropoutModal.vue";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";

export default {
  components: {
    ChangeNicknameModal,
    DropoutModal,
  },
  setup() {
    const store = useUserStore();
    const { showUserInfoNick, showDropOutModal } = storeToRefs(store);

    return {
      showUserInfoNick,
      showDropOutModal,
      openModal: store.openModal,
    };
  },
  data() {
    return {
      user: {
        nickname: "간ㅈiㅈrㄱ살",
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
@import "@/assets/css/user/userModify.css";
</style>