<template>
  <div class="game-main">
    <transition name="fade">
      <Loading v-if="showStartModal" @close-start-modal="closeStartModal" />
    </transition>

    <MessageModal />
    <!-- <span class="game-red-team-name">홍팀</span> -->
    <div class="game-video-team1">
      <div v-if="myTeamIdx === 1" class="video-display">
        <div id="session" v-if="session">
          <!-- (시작) 카메라 영역 -->
          <div class="rtc-container">
            <div id="video-container">
              <user-video
                :stream-manager="publisher"
                :nickname="currentUserNickname"
              />

              <user-video
                v-for="sub in filteredRedSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
              />
            </div>
          </div>
          <!-- (끝) 카메라 영역 -->
        </div>
      </div>

      <div v-else-if="myTeamIdx === 2" class="video-display">
        <div id="session" v-if="session">
          <!-- (시작) 카메라 영역 -->
          <div class="rtc-container">
            <div id="video-container">
              <user-video
                v-for="sub in filteredRedSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
              />
            </div>
          </div>
          <!-- (끝) 카메라 영역 -->
        </div>
      </div>
    </div>

    <GameBoard class="game-board-main" />

    <!-- <span class="game-blue-team-name">청팀</span> -->
    <div class="game-video-team2">
      <div v-if="myTeamIdx === 1" class="video-display">
        <div id="session" v-if="session">
          <!-- (시작) 카메라 영역 -->
          <div class="rtc-container">
            <div id="video-container">
              <user-video
                v-for="sub in filteredBlueSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
              />
            </div>
          </div>
          <!-- (끝) 카메라 영역 -->
        </div>
      </div>

      <div v-else-if="myTeamIdx === 2" class="video-display">
        <div id="session" v-if="session">
          <!-- (시작) 카메라 영역 -->
          <div class="rtc-container">
            <div id="video-container">
              <user-video
                :stream-manager="publisher"
                :nickname="currentUserNickname"
              />

              <user-video
                v-for="sub in filteredBlueSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
              />
            </div>
          </div>
          <!-- (끝) 카메라 영역 -->
        </div>
      </div>
    </div>

    <MiniGame v-show="isMission" />
    <GameChat class="game-chat-main" />
    <GameEnd
      v-if="isGameEnd"
      class="game-end"
      :winMessage="winMessage"
      @closeModal="closeModal"
    />
  </div>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/game/UserVideo.vue";

import { useUserStore } from "@/store/userStore";
import { useGameStore } from "@/store/gameStore";

import GameBoard from "@/components/game/GameBoard.vue";
import RedVideo from "@/components/game/RedVideo.vue";
import BlueVideo from "@/components/game/BlueVideo.vue";
import GameChat from "@/components/game/GameChat.vue";
import MiniGame from "@/components/game/MiniGame.vue";
import MessageModal from "@/components/layout/MessageModal.vue";
import GameEnd from "@/components/game/GameEnd.vue";
import Loading from "@/components/game/Loading.vue";

export default {
  components: {
    GameBoard,
    RedVideo,
    BlueVideo,
    GameChat,
    MiniGame,
    MessageModal,
    GameEnd,
    Loading,
    UserVideo,
  },

  data() {
    return {
      winMessage: null,
      showStartModal: true,

      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      mySessionId: "",
      myUserName: "",
      myTeamIdx: null,

      currentUserNickname: "",
    };
  },

  created() {
    this.myTeamIdx = useUserStore().myTeamIdx;
  },

  computed: {
    // 미션 장소 체크.
    isMission() {
      const gameStore = useGameStore();
      return gameStore.isMission;
    },
    // 게임 상태 체크
    isGameEnd() {
      if (useGameStore().redEnd === 5) {
        this.redWin();
        return true;
      }
      if (useGameStore().blueEnd === 5) {
        this.blueWin();
        return true;
      }
      return false;
    },

    currentUserNickname() {
      return useUserStore().userInfo.nickname;
    },

    // 현재 팀에 따라 구독자를 필터링
    filteredRedSubscribers() {
      return this.subscribers.filter((sub) => sub.teamNumber === 1);
    },

    // 현재 팀에 따라 구독자를 필터링
    filteredBlueSubscribers() {
      console.log(this.subscribers);
      return this.subscribers.filter((sub) => sub.teamNumber === 2);
    },
  },

  methods: {
    redWin() {
      this.winMessage = 1;
      this.isShowEnd = true;
    },
    blueWin() {
      this.winMessage = 2;
      this.isShowEnd = true;
    },
    closeModal() {
      this.$router.push({ name: "home" });
    },

    closeStartModal(value) {
      if (value === "start") {
        this.showStartModal = false;
      }

      // 게임 시작
    },

    joinSession() {
      this.OV = new OpenVidu();
      this.session = this.OV.initSession();

      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        subscriber.teamNumber = useUserStore().myTeamIdx;
        subscriber.email = useUserStore().userInfo.email;
        this.subscribers.push(subscriber);
      });

      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      this.session.on("exception", ({ exception }) => {});

      this.getToken(this.mySessionId).then((token) => {
        this.session
          .connect(token, {
            clientData: this.myUserName,
            teamNumber: useUserStore().myTeamIdx,
            email: useUserStore().userInfo.email,
          })
          .then(() => {
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined,
              videoSource: undefined,
              publishAudio: true,
              publishVideo: true,
              resolution: "640x480",
              frameRate: 30,
              insertMode: "APPEND",
              mirror: false,
            });

            this.mainStreamManager = publisher;
            this.publisher = publisher;

            this.session.publish(this.publisher);
          })
          .catch((error) => {});
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      window.removeEventListener("beforeunload", this.leaveSession);
    },

    updateMainVideoStreamManager(stream) {
      let publisher = this.OV.initPublisher(undefined, {
        audioSource: undefined,
        videoSource: undefined,
        publishAudio: true,
        publishVideo: false,
        resolution: "640x480",
        frameRate: 30,
        insertMode: "PREPEND",
        mirror: false,
      });

      if (this.publisher === publisher) return;
      this.publisher = publisher;
    },

    async getToken(mySessionId) {
      const sessionId = await this.createSession(mySessionId);
      return await this.createToken(sessionId);
    },

    async createSession(sessionId) {
      const response = await axios.post(
        "https://i10d205.p.ssafy.io/api/v1" + "/sessions",
        { customSessionId: sessionId },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      return response.data;
    },

    async createToken(sessionId) {
      const response = await axios.post(
        "https://i10d205.p.ssafy.io/api/v1" +
          "/sessions/" +
          sessionId +
          "/connections",
        {},
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      return response.data;
    },
  },

  mounted() {
    this.myUserName = useUserStore().userInfo.nickname;
    this.mySessionId = "A";
    this.joinSession();

    // 로딩창 7.5초동안 데이터 받는 시간 확보
    setTimeout(() => {}, 7800);
  },
};
</script>

<style scoped>
@import "@/assets/css/game/gameView.css";
</style>
