import { createApp } from 'vue'
import { useStore } from './store';

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(useStore);
app.use(router)

app.mount('#app')