<template>
  <div class="user-profile">
    <div class="left-section">
      <img src="@/assets/img/profile_picture.png" alt="profile-img" class="profile-picture"/>
      <h3>{{ userInfo.name }}</h3>
      <button @type="submit" @click="openModal('Nick')" class="nickname-change-btn">
        닉네임 변경
      </button>
    </div>

    <div class="right-section">
      <!-- 닉네임 변경 모달 -->
      <transition name="fade">
        <ChangeNicknameModal v-if="showUserInfoNick" />
      </transition>
  
      <h4>이메일  {{ userInfo.email }}</h4>
      <h4>비밀번호  {{ user.password }}</h4>
      <h4>생년월일  {{ userInfo.birthDate }}</h4>
      <!-- 성별 체크박스로 넣어서 completed 정보로 표시하기 -->
      <div class="gender-font">
        <label for="male">성별 </label>
        <label for="male" v-if="userInfo.gender=='남'">남성</label>
        <label for="female" v-if="userInfo.gender=='여'">여성</label>
      </div>
      <button @type="submit" @click="openModal('out')" class="memberout-btn">회원 탈퇴</button>
    </div>
    <transition>
      <DropoutModal v-if="showDropOutModal" @close="showDropOutModal = false"/>
    </transition>
  </div>
</template>

<script>
import ChangeNicknameModal from "@/components/profile/ChangeNicknameModal.vue";
import DropoutModal from "@/components/profile/DropoutModal.vue";
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
        nickname: "",
        email: "",
        password: "********",
        birthdate: "",
        gender: "",
      },
    };
  },

  computed: {
    userInfo() {
      const userInfo = useUserStore().userInfo;
      return userInfo ? userInfo : "";
    },
  },

  methods: {
    // 회원 탈퇴 메서드
    withdraw() {},
  },
};
</script>

<style scoped>
@import "@/assets/css/profile/userModify.css";
</style>