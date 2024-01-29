<template>
  <div>
    <div class="side-bar" v-if="showFlag">
      <div class="side-title">
        <span class="side-text" v-if="showMessageFlag">알림</span>
        <span class="side-text" v-if="showFriendFlag">친구목록</span>
        <button class="btn" @click="closeSidebar">
          <img class="side-close" src="@/assets/icon/close.png" />
        </button>
      </div>
      <div class="side-line"></div>
      <MessageComponents v-if="showMessageFlag" />
      <FriendComponents v-if="showFriendFlag" />
    </div>
    <SettingComponents v-if="showSettingFlag" @close-setting="closeSettingModal"/>
    <div class="side-btn">
      <button class="btn" @click="showMessage"> 
        <img class="btn-img" src="@/assets/icon/message.png" />
      </button>
      <button class="btn" @click="showFriend">
        <img class="btn-img" src="@/assets/icon/friend.png" />
      </button>
      <button class="btn" @click="showSetting">
        <img class="btn-img" src="@/assets/icon/setting.png" />
      </button>
      <button class="btn" @click="showFriend">
        <img class="btn-img" src="@/assets/icon/logout.png" />
      </button>
    </div>
  </div>
</template>
  
<script>
import MessageComponents from "./MessageComponents.vue";
import FriendComponents from "./FriendComponents.vue";
import SettingComponents from "./SettingComponents.vue";

export default {
  // 컴포넌트
  components: {
    MessageComponents,
    FriendComponents,
    SettingComponents,
},
  
  data() {
    return {
      // 사이드 바 관리
      showFlag: false,
      // 사이드 바 내 모달 관리
      showMessageFlag: false,
      showFriendFlag: false,
      showSettingFlag: false
    }
  },

  methods: {
    // 알림창
    showMessage() {
      this.showFlag = true;
      this.showFriendFlag = false;
      this.showMessageFlag = true;
      this.showSettingFlag = false;
    },
    
    // 친구 목록
    showFriend() {
      this.showFlag = true;
      this.showMessageFlag = false;
      this.showFriendFlag = true;
      this.showSettingFlag = false;
    },

    // 설정 열기
    showSetting() {
      this.showSettingFlag = true;
      console.log(this.showSettingFlag)
    },

    // 설정 닫기
    closeSettingModal() {
      this.showSettingFlag = false;
    },

    // 사이드바 닫기
    closeSidebar() {
      this.showMessageFlag = false;
      this.showFriendFlag = false;
      this.showFlag = false;
    }
  },
};
</script>
  
  <style>
@import "@/assets/css/layout/sideBar.css";
</style>
  