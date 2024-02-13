/* 라이브러리 사용 */
import { Client } from "@stomp/stompjs";

/* LocalStorage 사용 */
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";
import { useFriendStore } from "@/store/friendStore";


/* .env 저장 주소 사용 */
const { VITE_WSS_API_URL } = import.meta.env;

/* 웹 소켓 연결을 위한 변수 */
let stompClient = null;
let connected = false;
let roomCode = null;


/* 접속 소켓 */
export function connectWebSocket(accessToken) {
  return new Promise((resolve, reject) => {
    stompClient = new Client({
      brokerURL: VITE_WSS_API_URL,
      connectHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },

      onConnect: () => {
        resolve()

        stompClient.subscribe("/sub/event", handleWebSocketMessage);
        stompClient.subscribe("/user/sub/event", handleWebSocketMessage);
      },

      // 정상적인 연결 해제 (초기화)
      onDisconnect: () => {
        stompClient = null
        connected = false
        console.log('[socket.onDisconnect] : 연결이 끊어졌습니다.')
      },

      // WebSocket이 닫힐 때 (초기회)
      onWebSocketClose: (closeEvent) => {
        connected = false
        console.log('[socket.onWebSocketClose] : 연결이 끊어졌습니다.')
        console.log('상세 에러 : ' + closeEvent)
      },

      // STOMP ERROR
      onStompError: (error) => {
        console.log('[socket.onStompError] : ' + error.headers['message'])
        console.log('STOMP 상세 에러 : ' + error.body)
        reject(new Error("STOMP ERROR"))
        alert('소켓 연결이 끊어졌습니다.')
      },

      // WebSocket ERROR
      onWebSocketError: (error) => {
        console.log('[socket.onWebSocketError] : ' + error.headers['message'])
        console.log('WebSocket 상세 에러 : ' + error.body)
        reject(new Error("WebSocket error"));
        alert('소켓 연결이 끊어졌습니다.');
      },

      reconnectDelay: 5000, //자동재연결
    })

    try {
      stompClient.activate();
      connected = true;
      console.log('소켓 연결 성공');
    } catch (error) {
      connected = false;
      console.log("소켓 에러 발생 : " + error);
    }
  }) 
}

// 로그인 이벤트 메시지 보내기
export function sendLoginEvent(event) {
  stompClient.publish({
    destination: "/pub/login",
    body: JSON.stringify(event)
  })
}
// 로그아웃 이벤트 메시지 보내기
export function sendLogoutEvent(event) {
  stompClient.publish({
    destination: "/pub/logout",
    body: JSON.stringify(event)
  })
}
// 일반 이벤트 메시지 보내기
export function sendEvent(event) {
  stompClient.publish({
    destination: "/pub/event",
    body: JSON.stringify(event)
  })
}

// 웹소켓으로 받은 메시지 처리
function handleWebSocketMessage(message) {
  const event = JSON.parse(message.body);
  const friendStore = useFriendStore()

  switch(event.eventCategory) {
    case 1:
      friendStore.receiveChatMessage({ friendID: event.fromUserEmail, message: event.message })
      break
    case 2:
      friendStore.receiveFriendRequest({ fromUserEmail: event.fromUserEmail, message: event.message })
      break
    case 3:
      friendStore.receiveGameInvitation({ fromUserEmail: event.fromUserEmail, message: event.message })
      break
  } 
}


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
      /* 연결이 끊어지면 값 초기화 */
      onWebSocketClose: (closeEvent) => {
        connected = false;
        console.log("WebSocket closed", closeEvent);
      },
      /* 연결이 끊어지면 값 초기화 */
      onWebSocketError: (error) => {
        connected = false;
        console.log("WebSocket error: ", error);
        reject(error);
      },
      /* 연결이 끊어지면 값 초기화 */
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
export function connectRoom(type, router, from) {
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

        if (type === "Room") {
          // 구독한다
          initRoom(router, from);
          resolve();
        } else if (type === "Pick") {
          initPick(router, from);
          resolve();
        }
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
        router.push("/");
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
        router.push("/");
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

/*
 * 방 구독 함수
 *
 * 방과 관련된 정보를 수신하는 함수이다.
 *
 * router : 해당 vue 라우터 변수
 * from : 방 생성인지 그냥 참여하는지 확인하는 변수
 */
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
              useRoomStore().seatInfo[seatKey].state = seat.state;
            }
          }

          // useRoomStore().receivedMessage.data.roomDetailDto.currentUserCount;
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
              useRoomStore().seatInfo[seatKey].state = seat.state;
            }
          }
        );
      } else if (useRoomStore().receivedMessage.type === "ROOM_READY") {
        useRoomStore().receivedMessage.data.forEach((seat, index) => {
          const seatKey = `seatnum${index + 1}`;
          if (useRoomStore().seatInfo[seatKey]) {
            useRoomStore().seatInfo[seatKey].nickname = seat.nickname;
            useRoomStore().seatInfo[seatKey].ready = seat.ready;
            useRoomStore().seatInfo[seatKey].state = seat.state;
          }
        });
      } else if (useRoomStore().receivedMessage.type === "ROOM_START_PICK") {
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
 * 픽창 구독 함수
 *
 * 픽창과 관련된 정보를 수신한다.
 *
 * router : 해당 vue 라우터 변수
 * from : 자신의 팀 이름 변수
 */
export function initPick(router, from) {
  // 먼저, create된 roomCode를 가져와서 방 구독
  useRoomStore().stompClient.subscribe(
    "/sub/room/" + currentRoomCode.value + "/" + from,
    (message) => {
      useRoomStore().receivedMessage = JSON.parse(message.body);
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

// 구독한 방에 알리기.
export function pubRoom(destination, email) {
  stompClient.publish({
    destination: destination,
    body: email,
  });
}

// 픽창 넘어가기 전 게임 정보 알리기.
export function pubPick(destination){
  stompClient.publish({
    destination: destination,
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