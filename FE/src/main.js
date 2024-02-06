import { createApp } from "vue";
import { useStore } from "./store";

import App from "./App.vue";
import router from "./router";

// fontawsome 사용 위한 라이브러리 설정
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const app = createApp(App);
app.use(useStore);
app.use(router);
app.component("font-awesome-icon", FontAwesomeIcon);

app.mount("#app");