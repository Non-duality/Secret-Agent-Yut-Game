<template>
  <div class="video-display">
    <div id="session" v-if="session">
      <!-- (시작) 카메라 영역 -->
      <div class="rtc-container">
        <div id="video-container">
          <user-video
            :stream-manager="publisher"
            @click="updateMainVideoStreamManager(publisher)"
            :nickname="currentUserNickname"
          />

          <user-video
            v-for="sub in filteredSubscribers"
            :key="sub.stream.connection.connectionId"
            :stream-manager="sub"
            @click="updateMainVideoStreamManager(sub)"
          />
        </div>
      </div>
      <!-- (끝) 카메라 영역 -->
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/game/openvidu/UserVideo.vue";

import { useGameStore } from "@/store/gameStore";
import { useUserStore } from "@/store/userStore";

export default {
  components: {
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

      mySessionId: "",
      myUserName: "",
      myTeamName: "",
    };
  },

  computed: {
    currentUserNickname() {
      return useUserStore().userInfo.nickname;
    },

    // 현재 팀에 따라 구독자를 필터링
    filteredSubscribers() {
      // 레드 팀에 해당하는 구독자들을 필터링하여 반환
      if (this.myTeamName === "red") {
        return this.subscribers.filter((sub) => {
          // 구독자의 닉네임이 레드 팀에 속하는지 확인
          return useGameStore().redUser.some(
            (user) => user.nickname === sub.nickname
          );
        });
      }

      // 블루 팀에 해당하는 구독자들을 필터링하여 반환
      else if (this.myTeamName === "blue") {
        return this.subscribers.filter((sub) => {
          // 구독자의 닉네임이 블루 팀에 속하는지 확인
          return useGameStore().blueUser.some(
            (user) => user.nickname === sub.nickname
          );
        });
      }

      // 팀에 속하지 않은 경우 빈 배열을 반환
      else return [];
    },
  },

  methods: {
    joinSession() {
      this.OV = new OpenVidu();
      this.session = this.OV.initSession();

      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
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
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined,
              videoSource: undefined,
              publishAudio: true,
              publishVideo: true,
              resolution: "640x480",
              frameRate: 30,
              insertMode: "PREPEND",
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

    // 자신의 팀 정보 받아오기
    const myNickname = useUserStore().userInfo.nickname;

    for (let i = 0; i < 3; i++) {
      if (myNickname === useGameStore().redUser[i].nickname)
        this.myTeamName = "red";
    }

    if (!this.myTeamName) {
      for (let i = 0; i < 3; i++) {
        if (myNickname === useGameStore().blueUser[i].nickname)
          this.myTeamName = "blue";
      }
    }

    this.joinSession();
  },
};
</script>

<style scoped>
.test {
  width: 180px;
  height: 130px;
  background-color: black;
  border: 1px solid white;
  border-radius: 10px;
  margin-top: 5px;
}
</style>
