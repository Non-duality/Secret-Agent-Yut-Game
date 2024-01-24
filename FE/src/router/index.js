import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/view/home/HomeView.vue'
import RoomViewVue from '@/view/room/RoomView.vue'
import UserViewVue from '@/view/user/UserView.vue'
import GameWaitingRoomVue from '@/components/room/GameWaitingRoom.vue'
import roomtest from '@/components/room/roomtest.vue'
import InitialViewVue from '@/view/home/InitialView.vue'

const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    // 로그인 전 초기 화면
    {
      path: '/',
      name: 'initial',
      component: InitialViewVue
    },
    // 로그인 후 메인 화면
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    // 방 리스트, 방생성, 게임 대기방
    {
      path: '/room',
      name: 'room',
      component: RoomViewVue,
      redirect: '/room/list',
      children: [
        {
          path: '/room/list',
          name: 'room/list',
          component: roomtest
        },
        {
          path: '/room/wait',
          name: 'room/wait',
          component: GameWaitingRoomVue
        },
      ]
    },
    // 사용자 회원정보
    {
      path: '/profile',
      name: 'profile',
      component: UserViewVue
    },
  ]
})

export default router
