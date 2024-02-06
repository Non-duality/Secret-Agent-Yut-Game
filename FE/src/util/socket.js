/* 라이브러리 사용 */
import { Client } from "@stomp/stompjs";

/* LocalStorage 사용 */
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";

/* .env 저장 주소 사용 */
const { VITE_WSS_API_URL } = import.meta.env;

/* 웹 소켓 연결을 위한 변수 */
let stompClient = null;
let connected = false;
let roomCode = null;

/* 게임 소켓 */
export function connect(accessToken, recvCallback) {
  return new Promise((resolve, reject) => {
    let token = accessToken;
    stompClient = new Client({
      brokerURL: "ws://192.168.100.99:8080/api/v1/connect",

      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },

      beforeConnect: () => {},

      onConnect: () => {
        resolve();
        // 여기에서 구독 설정
        stompClient.subscribe("/sub/game/80ba0a", (message) => {
          // console.log("메시지 받음:", message.body);
          recvCallback(JSON.parse(message.body));
        });
      },

      /* 연결이 끊어지면 값 초기화 */
      onDisconnect: () => {
        stompClient = null;
        connected = false;
      },

      onWebSocketClose: (closeEvent) => {
        connected = false;
        console.log("WebSocket closed", closeEvent);
      },

      onWebSocketError: (error) => {
        connected = false;
        console.log("WebSocket error: ", error);
        reject(error);
      },

      // STOMP 수준의 오류 처리
      onStompError: (frame) => {
        connected = false;
        console.error("[roomStore] : STOMP 오류 발생");
        reject(new Error("STOMP error"));
        alert("소켓이 끊어졌습니다.");
      },
      reconnectDelay: 5000, //자동재연결
    });

    try {
      stompClient.activate();
      connected = true;
      // this.socketSend("/pub/game/b2bc27/start","start");
      console.log("연결 성공");
    } catch (error) {
      connected = false;
      console.log("소켓 에러: " + error);
    }
  });
}

/*
 * 방, 픽창을 위한 소켓
 *
 * 방 또는 픽창에서 방을 구독을 위해 사용한다.
 */
export function connectRoom(router, from) {
  return new Promise((resolve, reject) => {
    let token = useUserStore().accessToken;
    console.log(router);

    stompClient = new Client({
      brokerURL: VITE_WSS_API_URL,

      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },

      beforeConnect: () => {},

      onConnect: () => {
        console.log("[socket.onConnect] : 연결 실행합니다.");
        // 구독한다
        initRoom(router, from);
        resolve();
      },

      /*
       * 연결이 끊김
       *
       * 자동으로 연결이 끊어진다.
       *
       * 여기서 값을 초기화를 할지 정해야 한다.
       */
      onDisconnect: () => {
        console.log("[socket.onDisconnect] : 연결이 끊어졌습니다.");
        // stompClient = null
        // connected = false;
        // useUserStore().initData();
      },

      onWebSocketClose: (closeEvent) => {
        console.log("[socket.onWebSocketClose] : 연결이 끊어졌습니다.");
        console.log("상세 에러 : " + closeEvent);
      },

      /*
       * 웹 소켓 에러
       *
       * 이 부분은 에러가 발생하면 모든 값 초기화 후 초기화면으로 이동한다.
       */
      onWebSocketError: (error) => {
        fatalError("[socket.onWebSocketError] : 웹 소켓 에러", error);
        reject(new Error("WebSocket error"));
        alert("세션이 끊어짐");
        router.push("/")
      },

      /*
       * STOMP 에러
       *
       * 이 부분은 에러가 발생하면 모든 값 초기화 후 초기화면으로 이동합니다.
       */
      onStompError: (frame) => {
        fatalError("[roomStore] : STOMP 오류 발생", frame);
        reject(new Error("STOMP error"));
        alert("세션이 끊어짐");
        router.push("/")
      },

      reconnectDelay: 5000, //자동재연결
    });

    // 여기서 소켓을 시작한다.
    try {
      if (from === "make") {
        roomCode = useRoomStore().createRoomInfo.roomCode;
      } else if (from === "enter") {
        roomCode = useUserStore().roomCode;
      }

      stompClient.activate();
      connected = true;
      console.log("[socket] : activate() 성공");
    } catch (error) {
      connected = false;
      console.log("[socket] : activate() 시도 중 에러");
      console.log("상세 에러 : " + error);
    }
  });
}

export function initRoom(router, from) {
  // 먼저, create된 roomCode를 가져와서 방 구독
  useRoomStore().subscription.room = stompClient.subscribe(
    "/sub/room/" + roomCode,

    // 구독 메시지 이벤트 처리
    (message) => {
      console.log(message.body);
      useRoomStore().receivedMessage = JSON.parse(message.body);

      // (경우1) 입장 정보 타입
      if (useRoomStore().receivedMessage.type === "ROOM_ENTER_INFO") {
        useRoomStore().roomChatMessages.push(
          useRoomStore().receivedMessage.data.message
        );

        useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
          (seat, index) => {
            const seatKey = `seatnum${index + 1}`;
            if (useRoomStore().seatInfo[seatKey]) {
              useRoomStore().seatInfo[seatKey].nickname = seat.nickname
                ? seat.nickname
                : "";
              useRoomStore().seatInfo[seatKey].ready = seat.ready;
            }
          }
        );

        const storedRoomData = JSON.parse(localStorage.getItem("room"));

        const updatedRoomData = {
          ...storedRoomData,
          seatInfo: {
            ...useRoomStore().seatInfo,
          },

          roomChatMessages: {
            ...useRoomStore().roomChatMessages,
          },
        };

        // 로컬 스토리지에 업데이트된 데이터 저장
        localStorage.setItem("room", JSON.stringify(updatedRoomData));

        // 방 생성 처리라면 push 필요
        if (from === "make") {
          // 방 생성 모달 닫기
          useRoomStore().showRoomMakingModal = false;

          router.push({ name: "wait" });
        }
      } else if (useRoomStore().receivedMessage.type === "ROOM_EXIT_INFO") {
        useRoomStore().roomChatMessages.push(
          useRoomStore().receivedMessage.data.message
        );
        useRoomStore().receivedMessage.data.currentSeatDtoList.forEach(
          (seat, index) => {
            const seatKey = `seatnum${index + 1}`;
            if (useRoomStore().seatInfo[seatKey]) {
              useRoomStore().seatInfo[seatKey].nickname = seat.nickname;
              useRoomStore().seatInfo[seatKey].ready = seat.ready;
            }
          }
        );
      } else if (useRoomStore().receivedMessage.type === "ROOM_CHAT") {
        useRoomStore().roomChatMessages.push(
          useRoomStore().receivedMessage.data.nickname +
            " : " +
            useRoomStore().receivedMessage.data.message
        );

        const storedRoomData = JSON.parse(localStorage.getItem("room"));

        const updatedRoomData = {
          ...storedRoomData,
          roomChatMessages: {
            ...useRoomStore().roomChatMessages,
          },
        };

        // 로컬 스토리지에 업데이트된 데이터 저장
        localStorage.setItem("room", JSON.stringify(updatedRoomData));
      }
    }
  );
}

/*
 * 치명적 오류
 *
 * 여기서는 소켓을 모두 초기화하고 사용자 LocalStoage를 초기화 후
 * 초기 화면으로 이동하게 된다.
 */
function fatalError(error, msg) {
  stompClient = null;
  connected = false;
  useUserStore().initData();
  useRoomStore().stompClient.deactivate();
  console.log("[socket.fatalError] : " + error);
  console.log("상세 에러 : " + msg);
}

// 방 구독하기
export function pubRoom(destination, email) {
  stompClient.publish({
    destination: destination,
    body: email,
  });
}

// 서버로 보내기.
export function socketSend(destination, msg) {
  console.log(destination);
  stompClient.publish({
    destination: destination,
    body: JSON.stringify(msg),
  });
}