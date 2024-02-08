<template>
  <div class="background">
    <div class="timer">{{ remainingTime }}</div>
    <div class="content">
      <!-- (시작) OpenVidu -->
      <div id="main-container" class="container">
        <div id="session" v-if="session">
          <!-- 카메라 영역 -->
          <div class="rtc-container">
            <div id="video-container">
              <user-video
                :stream-manager="publisher"
                @click="updateMainVideoStreamManager(publisher)"
              />
              <user-video
                v-for="sub in subscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              />
            </div>
          </div>
        </div>
      </div>
      <!-- (끝) OpenVidu -->

      <div class="characters-wrapper">
        <div class="character-container">
          <div
            v-for="user in users"
            :key="user"
            @click="toggleCharacterSelection(user)"
          >
            <div class="character-item">
              <img
                src="@/assets/img/sample.png"
                alt="sample-img"
                class="sample-img"
              />
              <p class="select">
                {{ isCharacterSelected(user) ? "선택완료" : "선택중..." }}
              </p>
            </div>
          </div>
        </div>
        <div class="character-box">
          <div v-for="character in characters" :key="character" class="box">
            <div class="character">
              <img
                :src="character"
                class="select-img"
                @click="selectCharacter(character)"
                :style="{
                  filter: isCharacterSelected(character)
                    ? 'grayscale(100%)'
                    : 'none',
                }"
              />
            </div>
            <span v-if="character.includes('king')">왕</span>
            <span v-if="character.includes('spearman')">창병</span>
            <span v-if="character.includes('cavalry')">기병</span>
            <span v-if="character.includes('peasant')">농민</span>
            <span v-if="character.includes('slave')">노비</span>
          </div>
        </div>
        <button @type="submit" @click="openModal('spy')" class="ready">
          준비완료
        </button>
        <spyModal
          v-if="showSpyModal"
          @close="showSpyModal = false"
          class="spy-modal"
        />
      </div>
    </div>
  </div>
</template>

<script>
import spyModal from "@/view/game/pick/spyModal.vue";
import { useUserStore } from "@/store/userStore";
import { usePickStore } from "@/store/pickStore";
import { storeToRefs } from "pinia";

import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/game/openvidu/UserVideo.vue";

import king from "@/assets/img/game/pick/king.png";
import cavalry from "@/assets/img/game/pick/cavalry.png";
import peasant from "@/assets/img/game/pick/peasant.png";
import slave from "@/assets/img/game/pick/slave.png";
import spearman from "@/assets/img/game/pick/spearman.png";

axios.defaults.headers.post["Content-Type"] = "application/json";

export default {
  components: {
    spyModal,
    UserVideo,
  },

  data() {
    return {
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      remainingTime: 30,
      timerInterval: null,

      mySessionId: "",
      myUserName: "",
    };
  },

  setup() {
    const store = useUserStore();
    const { showSpyModal } = storeToRefs(store);

    const users = ["준희", "지훈", "성규", "수빈", "희웅"];
    const characters = [king, spearman, cavalry, peasant, slave];
    const selectedCharacters = [];

    return {
      showSpyModal,
      openModal: store.openModal,
      users,
      characters,
      selectedCharacters,
      borderColor: "initialBorderColor",
    };
  },

  methods: {
    joinSession() {
      // --- 1) Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- 2) Init a session ---
      this.session = this.OV.initSession();

      // --- 3) Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {
        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // --- 5) Get your own camera stream with the desired properties ---

            // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
            // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "PREPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            // Set the main video in the page to display our webcam and store our Publish er
            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- 6) Publish your stream ---
            this.session.publish(this.publisher);
          })
          .catch((error) => {
            // console.log(
            //   "There was an error connecting to the session:",
            //   error.code,
            //   error.message
            // );
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
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
        audioSource: undefined, // The source of audio. If undefined default microphone
        videoSource: undefined, // The source of video. If undefined default webcam
        publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
        publishVideo: false, // Whether you want to start publishing with your video enabled or not
        resolution: "640x480", // The resolution of your video
        frameRate: 30, // The frame rate of your video
        insertMode: "PREPEND", // How the video is inserted in the target element 'video-container'
        mirror: false, // Whether to mirror your local video or not
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
      return response.data; // The sessionId
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
      return response.data; // The token
    },

    // 캐릭터를 선택했는지 확인하는 로직
    isCharacterSelected(user) {
      return this.selectedCharacters.some(
        (entry) => entry.user === user && entry.character !== null
      );
    },

    // 캐릭터를 선택 후 다시 해제 할 수 있는 로직
    toggleCharacterSelection(user, character) {
      const existingIndex = this.selectedCharacters.findIndex(
        (entry) => entry.user === user
      );

      if (existingIndex !== -1) {
        // 이미 선택한 캐릭터면 선택 해제
        this.selectedCharacters.splice(existingIndex, 1);
      } else {
        // 아직 캐릭터 선택 안했으면 선택
        const userHasSelectedCharacter = this.selectedCharacters.some(
          (entry) => entry.user === user && entry.character !== null
        );

        if (!userHasSelectedCharacter) {
          this.selectedCharacters.push({ user, character: null });
        }
      }
    },

    // 캐릭터 선택하는 로직 >> 캐릭터 하나만 선택할 수 있도록 하면 됨 !
    selectCharacter(character) {
      const user = "현재 사용자"; // 사용자 정보 가져오는 곳
      const existingIndex = this.selectedCharacters.findIndex(
        (entry) => entry.user === user
      );

      // 이미 선택한 캐릭터면 선택 해제
      if (existingIndex !== -1) {
        this.selectedCharacters[existingIndex].character = character;
      } else {
        // 아직 캐릭터 선택 안했으면 선택
        const userHasSelectedCharacter = this.selectedCharacters.some(
          (entry) => entry.user === user && entry.character !== null
        );

        if (!userHasSelectedCharacter) {
          // console.log(`User ${user} selected character ${character}`);
          this.selectedCharacters.push({ user, character });
        }
      }
    },
    changeBorderColor() {
      this.borderColor = "red";
    },

    startTimer() {
      // 1초마다 남은 시간을 감소시키는 타이머 설정
      this.timerInterval = setInterval(() => {
        if (this.remainingTime > 0) {
          this.remainingTime--;
        } else {
          // 시간이 다 되었을 때 타이머를 멈춤
          clearInterval(this.timerInterval);
        }
      }, 1000);
    },
  },

  mounted() {
    useUserStore().showSpyModal = false;
    useUserStore().showModalSide = false;

    // mounted 되면 바로 세션 접속
    this.myUserName = "A"; // userInfo.nickname
    this.mySessionId = "SessionA"; // {임의코드}/red 또는 {임의코드}/blue

    this.joinSession();

    // 서버로부터 픽 시작은 받으면, 타이머 시작
    this.startTimer();
  },

  unmounted() {
    this.leaveSession();
    useUserStore().initData();
    alert("홈으로!!");
    this.$router.push({ name: "/" });
  },
};
</script>


<style scoped>
@import url("../../../assets/css/game/character.css");
</style>