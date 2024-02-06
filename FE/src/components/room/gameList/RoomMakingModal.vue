<template>
  <!-- 방 생성 모달 -->
  <div class="room-make-modal">
    <!-- 방 생성 form -->
    <h2>방 생성</h2>
    <form class="room-make-form" @submit.prevent>
      <div class="form-group">
        <label for="title">방 제목</label>
        <input type="text" id="title" maxlength="6" v-model="roomInfo.title" />
      </div>
      <div class="form-group">
        <label for="isPublic">공개 여부</label>
        <div name="theme" id="theme">
          <button
            id="theme-btn"
            @click="selectScope(true)"
            :class="{ selected: roomInfo.public === true }"
          >
            공개
          </button>
          <button
            id="theme-btn"
            @click="selectScope(false)"
            :class="{ selected: roomInfo.public === false }"
          >
            비공개
          </button>
        </div>
      </div>
      <div class="form-group" v-if="!roomInfo.public">
        <label for="password">비밀번호</label>
        <!-- 악성 유저 고려해서 비밀번호 길이 제한 -->
        <input
          type="text"
          id="password"
          maxlength="10"
          v-model="roomInfo.password"
        />
      </div>
      <div class="form-group">
        <label for="theme">맵 테마</label>
        <div>
          <div name="theme" id="theme">
            <button
              id="theme-btn"
              @click="selectTheme('설날')"
              :class="{ selected: roomInfo.theme === '설날' }"
            >
              설날
            </button>
            <button
              id="theme-btn"
              @click="selectTheme('추석')"
              :class="{ selected: roomInfo.theme === '추석' }"
            >
              추석
            </button>
          </div>
        </div>
      </div>
      <div class="form-group">
        <label for="speed">속도</label>
        <div>
          <button
            id="theme-btn"
            @click="selectSpeed(0)"
            :class="{ selected: roomInfo.speed === 0 }"
          >
            느림
          </button>
          <button
            id="theme-btn"
            @click="selectSpeed(1)"
            :class="{ selected: roomInfo.speed === 1 }"
          >
            보통</button
          ><button
            id="theme-btn"
            @click="selectSpeed(2)"
            :class="{ selected: roomInfo.speed === 2 }"
          >
            빠름
          </button>
        </div>
      </div>
      <div class="button-group">
        <button type="submit" class="create" @click="makeGame(roomInfo)">
          생성
        </button>
        <button type="button" class="cancle" @click="closeModal('roomMaking')">
          취소
        </button>
      </div>
    </form>
  </div>
</template>

<script>
// 방 정보 데이터
import { useRoomStore } from "@/store/roomStore";
import { useUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";

const router = useRouter();

export default {
  data() {
    return {
      // 생성할 방 정보
      roomInfo: {
        title: "",
        public: true,
        password: "",
        theme: "설날",
        speed: 1,
      },
    };
  },

  methods: {
    /* 방 생성 함수 */
    makeGame(roomInfo) {
      // 만약 로그인 실패거나 소켓 연결 실패라면, "초기화면"으로 이동
      if (!useUserStore().isLogin || !useRoomStore().isConnected) {
        alert("연결이 끊어졌습니다.");
        useUserStore().initData();
        router.push("/");
      }

      // 만약 입력된 곳에서 빈 공백이 있다면, 해당 부분 alert 표시
      if (roomInfo.title === "") {
        alert("방 제목을 작성해주세요");
      } else if (roomInfo.public === false && roomInfo.password.trim() === "") {
        alert("비공개 방으로 비밀번호를 입력하세요");
      } else {

        // 생성하기 위한 방 정보를 v-model로 된 객체 가져옴
        useRoomStore().createRoomInfo = {
          ...this.roomInfo,
        };

        // 방 생성을 시작
        useRoomStore()
          .createRoom()
          .then(() => {

            // 먼저, create된 roomCode를 가져와서 방 구독
            useRoomStore().subscription.room =
              useRoomStore().stompClient.subscribe(
                "/sub/room/" + useRoomStore().createRoomInfo.roomCode,

                // 구독 메시지 이벤트 처리
                (message) => {
                  console.log(message.body);
                  useRoomStore().receivedMessage = JSON.parse(message.body);

                  // (경우1) 입장 정보 타입
                  if (
                    useRoomStore().receivedMessage.type === "ROOM_ENTER_INFO"
                  ) {
                    useRoomStore().roomChatMessages.push(
                      useRoomStore().receivedMessage.data.message
                    );

                    useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
                      (seat, index) => {
                        const seatKey = `seatnum${index + 1}`;
                        if (useRoomStore().seatInfo[seatKey]) {
                          useRoomStore().seatInfo[seatKey].nickname =
                            seat.nickname? seat.nickname : '';
                          useRoomStore().seatInfo[seatKey].ready = seat.ready;
                        }
                      }
                    );
                    // 방 생성 모달 닫기
                    useRoomStore().showRoomMakingModal = false;

                    this.$router.push({
                      name: "wait",
                    });
                  } else if (
                    useRoomStore().receivedMessage.type === "ROOM_EXIT_INFO"
                  ) {
                    useRoomStore().roomChatMessages.push(
                      useRoomStore().receivedMessage.data.message
                    );
                    useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
                      (seat, index) => {
                        const seatKey = `seatnum${index + 1}`;
                        if (useRoomStore().seatInfo[seatKey]) {
                          useRoomStore().seatInfo[seatKey].nickname =
                            seat.nickname;
                          useRoomStore().seatInfo[seatKey].ready = seat.ready;
                        }
                      }
                    );
                  } else if (
                    useRoomStore().receivedMessage.type === "ROOM_CHAT"
                  ) {
                    useRoomStore().roomChatMessages.push(
                      useRoomStore().receivedMessage.data.nickname +
                        " : " +
                        useRoomStore().receivedMessage.data.message
                    );
                  }
                }
              );
          })
          .then(() => {
            useUserStore().currentRoomInfo = {
              ...useRoomStore().createRoomInfo,
            };

            useRoomStore().stompClient.publish({
              destination:
                "/pub/room/" +
                useRoomStore().createRoomInfo.roomCode +
                "/enter",

              body: useUserStore().userInfo.email,
            });
          })

          // 방 생성 실패시, 방 삭제 요청
          // (아직 삭제 요청 미구현)
          .catch((error) => {
            alert("방 생성 실패");
            console.log(error);
          });
      }
    },

    // 모달 관리
    closeModal(modalType) {
      const roomStore = useRoomStore();
      roomStore.closeModal(modalType);
    },

    selectScope(val) {
      this.roomInfo.public = val;
    },

    selectTheme(val) {
      this.roomInfo.theme = val;
    },

    selectSpeed(val) {
      this.roomInfo.speed = val;
    },
  },

  // 공개 여부를 계속 감시
  watch: {
    "roomInfo.isPublic": function (newValue) {
      if (newValue) {
        this.roomInfo.password = ""; // 공개면 비밀번호를 초기화
      }
    },
  },
};
</script>

<style scoped>
@import "@/assets/css/room/roomMaking.css";
</style>