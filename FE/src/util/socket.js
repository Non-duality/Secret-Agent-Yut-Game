/* 라이브러리 사용 */
import { Client } from "@stomp/stompjs";

import GameBoardVue from "@/components/game/GameBoard.vue";

/* LocalStorage 사용 */
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";
import { usePickStore } from "@/store/pickStore";
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

      reconnectDelay: 3000, //자동재연결
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
  console.log('로그인 소켓 이벤트 메시지 발송')
  stompClient.publish({
    destination: "/pub/login",
    body: JSON.stringify(event)
  })
}
// 로그아웃 이벤트 메시지 보내고 연결 끊기
export function sendLogoutEvent(event) {
  if (stompClient) {
    stompClient.publish({
      destination: "/pub/logout",
      body: JSON.stringify(event)
    })
    console.log('로그아웃 소켓 이벤트 메시지 발송')
    stompClient.deactivate()
    stompClient = null
    connected = false
    console.log('소켓 연결이 끊어졌습니다.');
  }
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
  console.log(event.eventCategory)
  switch(event.eventCategory) {
    case "1":
      friendStore.receiveChatMessage({ friendID: event.fromUserEmail, message: event.message })
      break
    case "2":
      friendStore.receiveFriendRequest(event.fromUserEmail)
      break
    case "3":
      friendStore.receiveGameInvitation({ fromUserEmail: event.fromUserEmail, message: event.message })
      break
    case "4":
      friendStore.updateOnlineFriends(event.fromUserEmail)
      break
    case "5":
      friendStore.updateOfflineFriends(event.fromUserEmail)
      break
  } 
}


/* 게임 소켓 */
export function connect(team, accessToken, recvCallback) {
  return new Promise((resolve, reject) => {
    let token = accessToken;
    stompClient = new Client({
      brokerURL: VITE_WSS_API_URL,

      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },

      beforeConnect: () => {},

      onConnect: () => {
        resolve();
        // 여기에서 구독 설정
        stompClient.subscribe(`/sub/game/720ca5/${team}`, (message) => {
          console.log("메시지 받음:", message.body);
          recvCallback(JSON.parse(message.body));
        });
        stompClient.subscribe("/sub/game/720ca5", (message) => {
          console.log("메시지 받음:", message.body);
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
      // reconnectDelay: 5000, //자동재연결
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
        alert("세션이 끊어짐");
        router.push("/");
        reject(new Error("WebSocket error"));
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
              useRoomStore().seatInfo[seatKey].team = seat.team;
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
        // 방장이 방을 삭제한 경우 모두 alert를 받고 나간다.
        if (
          useRoomStore().receivedMessage.data.message.includes(
            "삭제되었습니다."
          )
        ) {
          alert("방장이 방을 나갔습니다.");

          // 구독 취소한 뒤 방 정보에 대해 모두 리셋한다.
          useRoomStore().subscription.room.unsubscribe();
          // const initialStateRoom = useRoomStore().$reset();
          // Object.assign(this, initialStateRoom);
          router.push({ name: "room" });
        } else {
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
                useRoomStore().seatInfo[seatKey].team = seat.team;
              }
            }
          );
        }
      } else if (useRoomStore().receivedMessage.type === "ROOM_READY") {
        useRoomStore().receivedMessage.data.forEach((seat, index) => {
          const seatKey = `seatnum${index + 1}`;
          if (useRoomStore().seatInfo[seatKey]) {
            useRoomStore().seatInfo[seatKey].nickname = seat.nickname;
            useRoomStore().seatInfo[seatKey].ready = seat.ready;
            useRoomStore().seatInfo[seatKey].state = seat.state;
            useRoomStore().seatInfo[seatKey].team = seat.team;
          }
        });
      }
      // 방장이 게임을 시작을 눌렀을 경우
      else if (useRoomStore().receivedMessage.type === "ROOM_START_PICK") {
        /* 자신의 자리 번호 */
        // (1~3) red, (4~6) blue
        let myTeamName = null;
        let isOwner = false;

        const seatInfo = useRoomStore().seatInfo;
        const seatKeys = Object.keys(seatInfo);

        // (임시)
        // 자신의 팀 번호 확인
        for (let i = 0; i < seatKeys.length; i++) {
          const seatKey = seatKeys[i];
          if (seatInfo[seatKey].nickname === useUserStore().userInfo.nickname) {
            // 자신이 방장인지 확인
            if (seatInfo[seatKey].state === 2) {
              isOwner = true;
            }

            // 팀 이름 결정
            if (seatInfo[seatKey].team === 1) {
              myTeamName = "red";
            } else {
              myTeamName = "blue";
            }
            break;
          }
        }

        // 소켓을 이제 Pick 타입으로 전환
        // 구독이 완료가 되면, 이제 픽창으로 시작
        connectRoom("Pick", router, myTeamName).then(() => {
          // 여기서는 유닛 정보, 사용자 픽 정보를 초기화로 받는다.
          // (방장만 하면 됨.)
          pubPick(
            "/pub/pick/" +
              useUserStore().currentRoomInfo.roomCode +
              "/get-pre-info"
          );

          // 픽창으로 넘어가기.
          setTimeout(() => {
            router.push({ name: "pick" });
          }, 3000);
        });
      }

      // 채팅
      else if (useRoomStore().receivedMessage.type === "ROOM_CHAT") {
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

      // 자리 변경
      else if (useRoomStore().receivedMessage.type === "ROOM_CHANGE_TEAM") {
        useRoomStore().receivedMessage.data.forEach((seat, index) => {
          const seatKey = `seatnum${index + 1}`;
          if (useRoomStore().seatInfo[seatKey]) {
            useRoomStore().seatInfo[seatKey].nickname = seat.nickname;
            useRoomStore().seatInfo[seatKey].ready = seat.ready;
            useRoomStore().seatInfo[seatKey].state = seat.state;
            useRoomStore().seatInfo[seatKey].team = seat.team;
          }
        });

        const storedRoomData = JSON.parse(localStorage.getItem("room"));

        const updatedRoomData = {
          ...storedRoomData,
          seatInfo: {
            ...useRoomStore().seatInfo,
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
  usePickStore().subscription.pick = stompClient.subscribe(
    "/sub/room/" + useUserStore().currentRoomInfo.roomCode + "/" + from,
    (message) => {
      usePickStore().receivedMessage = JSON.parse(message.body);

      // 홍팀, 청팀 정보를 받아오는 것
      if (usePickStore().receivedMessage.type === "PICK_GET_PRE_INFO") {
        usePickStore().code = usePickStore().receivedMessage.code;
        usePickStore().unitInfo = usePickStore().receivedMessage.data.unitInfo;
        usePickStore().userInfo = usePickStore().receivedMessage.data.userInfo;

        // 로컬 스토리지에 업데이트된 데이터 저장
        const storedPickData = JSON.parse(localStorage.getItem("pick"));
        const updatedPickData = {
          ...storedPickData,
          code: usePickStore().code,
          unitInfo: {
            ...usePickStore().unitInfo,
          },
          userInfo: {
            ...usePickStore().userInfo,
          },
        };
        localStorage.setItem("pick", JSON.stringify(updatedPickData));
      }

      // 처음에 "PICK_ORDER"로 첫 번째 순서를 배정받고,
      else if (usePickStore().receivedMessage.type === "PICK_ORDER") {
        usePickStore().nowPickPlayerInfo.email =
          usePickStore().receivedMessage.data.email;
        usePickStore().nowPickPlayerInfo.time =
          usePickStore().receivedMessage.data.time;

        // 로컬 스토리지에 업데이트된 데이터 저장
        const storedPickData = JSON.parse(localStorage.getItem("pick"));
        const updatedPickData = {
          ...storedPickData,
          nowPickPlayerInfo: {
            email: usePickStore().nowPickPlayerInfo.email,
            time: usePickStore().nowPickPlayerInfo.time,
          },
        };

        localStorage.setItem("pick", JSON.stringify(updatedPickData));
      }

      // "PICK_NEXT"로 다음 순서를 배정받고
      // done한 결과이므로, 다음 플레이어 차례를 알린다.
      else if (usePickStore().receivedMessage.type === "PICK_NEXT") {
        // usePickStore().finished = !usePickStore().finished;

        setTimeout(() => {
          usePickStore().nowPickPlayerInfo.email =
            usePickStore().receivedMessage.data.email;
          usePickStore().nowPickPlayerInfo.time =
            usePickStore().receivedMessage.data.time;

          // 로컬 스토리지에 업데이트된 데이터 저장
          const storedPickData = JSON.parse(localStorage.getItem("pick"));
          const updatedPickData = {
            ...storedPickData,
            nowPickPlayerInfo: {
              email: usePickStore().nowPickPlayerInfo.email,
              time: usePickStore().nowPickPlayerInfo.time,
            },
          };

          localStorage.setItem("pick", JSON.stringify(updatedPickData));
        }, 70);
      }

      // 픽을 실시간으로 무엇을 선택하고 있는지 받아오는 것
      else if (usePickStore().receivedMessage.type === "PICK_SELECT") {
        usePickStore().userInfo = usePickStore().receivedMessage.data.userInfo;

        // 로컬 스토리지에 업데이트된 데이터 저장
        const storedPickData = JSON.parse(localStorage.getItem("pick"));
        const updatedPickData = {
          ...storedPickData,
          userInfo: {
            ...usePickStore().userInfo,
          },
        };
        localStorage.setItem("pick", JSON.stringify(updatedPickData));
      }

      // 픽한 결과를 받는 것
      else if (usePickStore().receivedMessage.type === "PICK_SELECT_DONE") {
        usePickStore().finished = !usePickStore().finished;

        setTimeout(() => {
          usePickStore().userInfo =
            usePickStore().receivedMessage.data.userInfo;
          usePickStore().unitInfo =
            usePickStore().receivedMessage.data.unitInfo;

          // 로컬 스토리지에 업데이트된 데이터 저장
          const storedPickData = JSON.parse(localStorage.getItem("pick"));

          const updatedPickData = {
            ...storedPickData,
            userInfo: {
              ...usePickStore().userInfo,
            },
            unitInfo: {
              ...usePickStore().unitInfo,
            },
          };
          localStorage.setItem("pick", JSON.stringify(updatedPickData));
        }, 20);
      }

      // 자신의 팀 픽만 끝났다면, 대기 모달 띄우기
      else if (usePickStore().receivedMessage.type === "PICK_WAIT") {
        setTimeout(() => {
          usePickStore().pickFinished = !usePickStore().pickFinished;
        }, 500);
      }

      // 양 팀 모두 픽 성공했다면, 스파이 모달창 띄우기
      else if (usePickStore().receivedMessage.type === "PICK_SELECT_SPY") {
        setTimeout(() => {
          usePickStore().pickRealFinished = !usePickStore().pickRealFinished;
        }, 1000);
      }

      // 자신의 팀 밀정 픽만 끝났다면, 대기 모달 띄우기
      else if (usePickStore().receivedMessage.type === "PICK_SPY_WAIT") {
        setTimeout(() => {
          usePickStore().pickSpyWait = !usePickStore().pickSpyWait;
        }, 1000);
      }

      // 양 팀 모두 밀정 픽 성공했다면, 게임 START을 알림.
      else if (usePickStore().receivedMessage.type === "GAME_START") {
        setTimeout(() => {
          // (1) 소켓 끊고 다음 게임 소켓으로 연결하기??
          const gameBoard = new GameBoardVue;
          gameBoard.connectSocket();

          // (2) 그리고 나서 현재 방, 픽 창 리셋하기 --> 구독 정보 없애기

          // 게임으로 푸쉬하기.
          router.push({name: "game"});
        }, 5000);
      }
    }
  );
}

// 구독한 방에 알리기.
export function pubRoom(destination, email) {
  stompClient.publish({
    destination: destination,
    body: email,
  });
}

// 픽창 넘어가기 전 게임 정보 알리기.
export function pubPick(destination) {
  stompClient.publish({
    destination: destination,
  });
}

// 캐릭터 픽 정보 알리기.
export function pubPickInfo(destination, content) {
  stompClient.publish({
    destination: destination,
    body: JSON.stringify(content),
  });
}

// 서버로 보내기.
export function socketSend(destination, msg) {
  stompClient.publish({
    destination: destination,
    body: JSON.stringify(msg),
  });
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
