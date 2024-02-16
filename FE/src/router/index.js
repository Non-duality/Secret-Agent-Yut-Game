import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/view/home/HomeView.vue'
import RoomViewVue from '@/view/room/RoomView.vue'
import UserViewVue from '@/view/user/UserView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/room',
      name: 'room',
      component: RoomViewVue
    },
    {
      path: '/profile',
      name: 'profile',
      component: UserViewVue
    },
  ]
})

export default router
