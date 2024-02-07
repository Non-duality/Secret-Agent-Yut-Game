import { Client } from "@stomp/stompjs";
import { useUserStore } from "@/store/userStore";
import { useRoomStore } from "@/store/roomStore";

const { VITE_WSS_API_URL } = import.meta.env;

let stompClient = null;
let connected = false;

let stompClient2 = null;
let connected2 = false;

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

      onDisconnect: () => {},

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

/* 방, 픽창 소켓 */
export function connectWS() {
  return new Promise((resolve, reject) => {
    let token = useUserStore().accessToken;

    useRoomStore().stompClient = new Client({
      brokerURL: VITE_WSS_API_URL,

      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },

      beforeConnect: () => {},

      onConnect: () => {
        useRoomStore().isConnected = true;
        resolve();
      },

      reconnectDelay: 5000, //자동재연결,

      onDisconnect: () => {
        useRoomStore().isConnected = false;
        useUserStore().initData();
      },

      onWebSocketClose: (closeEvent) => {
        console.log("WebSocket closed", closeEvent);
      },

      onWebSocketError: (error) => {
        useRoomStore().isConnected = false;
        useUserStore().initData();
        useRoomStore().stompClient.deactivate();
        console.log("WebSocket error: ", error);
        reject(error);
      },

      // STOMP 수준의 오류 처리
      onStompError: (frame) => {
        useUserStore().initData();
        useRoomStore().stompClient.deactivate();
        console.error("[roomStore] : STOMP 오류 발생");
        reject(new Error("STOMP error"));
        alert("소켓이 끊어졌습니다.");
      },
    });

    // try {
    //   stompClient.activate();
    //   connected = true;
    //   // this.socketSend("/pub/game/b2bc27/start","start");
    //   console.log("연결 성공");
    // } catch (error) {
    //   connected = false;
    //   console.log("소켓 에러: " + error);
    // }
    try {
      useRoomStore().stompClient.activate();
    } catch (error) {}
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
