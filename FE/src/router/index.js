import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/view/home/HomeView.vue'
import RoomViewVue from '@/view/room/RoomView.vue'
import UserViewVue from '@/view/user/UserView.vue'
import GameWaitingRoomVue from '@/view/room/GameWaitingRoom.vue'
import InitialViewVue from '@/view/home/InitialView.vue'
import UserInfoModifyVue from '@/components/profile/UserInfoModify.vue'
import ChangePasswordVue from '@/components/profile/ChangePassword.vue'
import ScoreCheckVue from '@/components/profile/ScoreCheck.vue'
import RoomListViewVue from '@/components/room/RoomListView.vue'

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
          component: RoomListViewVue
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
      path: '/user',
      name: 'user',
      component: UserViewVue,
      redirect: '/user/info',
      children: [
        {
          path: '/user/info',
          name: 'user/info',
          component: UserInfoModifyVue
        },
        {
          path: '/user/password',
          name: 'user/password',
          component: ChangePasswordVue
        },
        {
          path: '/user/records',
          name: 'user/records',
          component: ScoreCheckVue
        },
      ]
    },
  ]
})

export default router
