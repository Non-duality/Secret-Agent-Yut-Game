import { createApp } from "vue";
import { useStore } from "./store";

import { useRoomStore } from "@/store/roomStore";
import { useUserStore } from "./store/userStore";

import App from "./App.vue";
import router from "./router";


// fontawsome 사용 위한 라이브러리 설정
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const app = createApp(App);
app.use(useStore);
app.use(router);
app.component("font-awesome-icon", FontAwesomeIcon);

// 로그인 상태인 경우에만 초기 웹 소켓 연결 수행
if (useUserStore().isLogin) {
    useRoomStore().connectWS().then(() => {
      // 애플리케이션 마운트
      app.mount('#app');
    });
  } else {
    // 로그인되지 않은 경우, 바로 애플리케이션 마운트
    app.mount('#app');
  }
