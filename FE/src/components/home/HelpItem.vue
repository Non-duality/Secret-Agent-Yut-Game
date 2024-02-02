<template>
  <div class="modal">
    <div id="work-npc-main">
      <img
        class="work-npc-avatar"
        src="../../assets/img/home/test06.png"
        alt="이미지"
      />
      <div class="work-npc-box">
        <div class="work-npc-name">도움말</div>
        <img src="@/assets/img/help/cancel.png" alt="cancel-button" class="cancel-button" @click="closeModal('help')">
        <div class="work-npc-content" v-html="npcMessage"></div>
        <button @click="showNextIntroMessage" v-if="step === 0" class="modal-yes-btn">응, 알려줘</button>
        <img src="@/assets/img/help/next.png" alt="next-vector" class="next-vector" @click="showNextMessage" v-if="step === 1">
        <!-- <img src="@/assets/img/help/next.png" alt="next-vector" class="next-vector" @click="showNextIntroMessage" v-if="step === 0"> -->
        <section class="work-npc-buttons">
          <button v-if="step === 1" @click="showCheckScore()" class="modal-exp-btn" :class="{ changedColor: isClikedScore }"># 전적 조회</button>
          <button v-if="step === 1" @click="showCreateGameroom()" class="modal-exp-btn" :class="{ changedColor: isClikedGameRoom }"># 게임방 생성</button>
          <button v-if="step === 1" @click="showSelectCharacter()" class="modal-exp-btn" :class="{ changedColor: isClikedCharacter }"># 말선택</button>
          <button v-if="step === 1" @click="showSelectSpy()" class="modal-exp-btn" :class="{ changedColor: isClikedSpy }"># 밀정 선택</button>
          <button v-if="step === 1" @click="showDoMission()" class="modal-exp-btn" :class="{ changedColor: isClikedMisson }"># 미션 수행</button>
          <button v-if="step === 1" @click="showManageCam()" class="modal-exp-btn" :class="{ changedColor: isClikedCam }"># 캠 관리</button>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import ChangePassword from '../profile/ChangePassword.vue';

export default {
  data() {
    return {
      npcMessage: "",
      messageDelay: [],
      selectedMessages: [],
      selectedIndex: 0,
      isClikedScore: false,
      isClikedGameRoom: false,
      isClikedCharacter: false,
      isClikedSpy: false,
      isClikedMisson: false,
      isClikedCam: false,

      // 시작
      mainMessages: [
        "게임에 대해서 설명해줄까?",
        "너가 궁금한 점을 클릭하면 설명해줄게.",
      ],
      step: 0,

      //전적 조회
      checkScoreMessages: [
      "마이페이지의 전적 조회를 누르면<br> 너의 최근 10개의 전적을 확인할 수 있어.",
      "전적을 클릭하면<br> 승패 여부, 미션 성공률, 말을 잡은 수 등<br> 상세 정보도 확인할 수 있어.",
      "다른 정보가 궁금하다면 다른 태그를 눌러보고,<br> 궁금한게 없다면 다음에 궁금할 때 또 와."
      ],
      currentCheckScoreIndex: 0,

      // 게임방 생성
      createGameroomMessages: [
        "여기는",
        "게임방",
        "하하호호"
      ],
      currentCreateGameroomIndex: 0,

      // 말선택
      selectCharacterMessages: [
        "나를 고를래",
        "쟤를 고를래",
        "나야 쟤야",
        "선택해"
      ],
      currentSelectCharacterIndex: 0,

      // 밀정 선택
      selectSpyMessages: [
        "나를 고를래",
        "쟤를 고를래",
        "나야 쟤야",
        "선택해"
      ],
      currentSelectSpyIndex: 0,

      // 미션 수행
      doMissionMessages: [
        "가서 저기 벽찍고 온나",
      ],
      currentDoMissionIndex: 0,

      // 캠 관리
      manageCamMessages: [
        "A",
        "B",
        "C",
        "캍"
      ],
      currentManageCamIndex: 0,
    };
  },

  methods: {
    // 모달창 닫기
    closeModal(modalId) {
      this.$emit("close-modal", modalId);
    },

    // setTimeout
    Setmessage(messages) {
      this.clearMessageDelay();
      this.npcMessage = "";

      let i = 0;
      const delay = messages.length >= 20 ? 35 : 45;
      
      const processNextMessage = () => {
        if (i < messages.length) {
          this.npcMessage += messages[i];
          i++;
          setTimeout(processNextMessage, delay);
        }
      };

      processNextMessage();
    },

    clearMessageDelay() {
      if (this.messageDelay.length > 0) {
        this.messageDelay.forEach((element) => {
          clearTimeout(element);
        });
        this.messageDelay = [];
        this.npcMessage = "";
      }
    },

    showNextIntroMessage() {
      if (this.step < this.mainMessages.length - 1) {
        this.step++;
        this.Setmessage(this.mainMessages[this.step]);
      } else {
        this.closeModal('help');
      }
    },
    
    showNextMessage() {
      if (this.selectedIndex < this.selectedMessages.length-1) {
        this.selectedIndex++;
        this.Setmessage(this.selectedMessages[this.selectedIndex]);
      } else {
        this.closeModal('help');
      }
    },
    
    // 각각 상황별 처리

    // 전적 조회
    showCheckScore() {
      this.selectedMessages = this.checkScoreMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = true
      this.isClikedGameRoom = false
      this.isClikedCharacter = false
      this.isClikedSpy = false
      this.sClikedMisson = false
      this.isClikedCam = false
    },

    // 게임방 생성
    showCreateGameroom(){
      this.selectedMessages = this.createGameroomMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = false
      this.isClikedGameRoom = true
      this.isClikedCharacter = false
      this.isClikedSpy = false
      this.sClikedMisson = false
      this.isClikedCam = false
    },

    // 말 선택
    showSelectCharacter(){
      this.selectedMessages = this.selectCharacterMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = false
      this.isClikedGameRoom = false
      this.isClikedCharacter = true
      this.isClikedSpy = false
      this.sClikedMisson = false
      this.isClikedCam = false
    },

    // 밀정 선택
    showSelectSpy(){
      this.selectedMessages = this.selectSpyMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = false
      this.isClikedGameRoom = false
      this.isClikedCharacter = false
      this.isClikedSpy = true
      this.sClikedMisson = false
      this.isClikedCam = false
    },

    // 미션 수행
    showDoMission(){
      this.selectedMessages = this.doMissionMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = false
      this.isClikedGameRoom = false
      this.isClikedCharacter = false
      this.isClikedSpy = false
      this.sClikedMisson = true
      this.isClikedCam = false
    },

    // 캠 관리
    showManageCam(){
      this.selectedMessages = this.manageCamMessages
      this.selectedIndex = 0
      this.Setmessage(this.selectedMessages[this.selectedIndex])
      this.isClikedScore = false
      this.isClikedGameRoom = false
      this.isClikedCharacter = false
      this.isClikedSpy = false
      this.sClikedMisson = false
      this.isClikedCam = true
    },

    showExpMessage() {
      if (this.check < this.checkScoreMessages.length - 1) {
        this.check++;
        this.Setmessage(this.checkScoreMessages[this.check]);
      } else {
        this.closeModal('help');
      }
    },
  },

  mounted() {
    this.Setmessage(this.mainMessages[this.step]);
  },
};
</script>

<style scoped>
@import url("../../assets/css/home/help.css");
</style>
