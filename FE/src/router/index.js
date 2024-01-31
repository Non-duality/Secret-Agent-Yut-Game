import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/view/home/HomeView.vue'
import RoomViewVue from '@/view/room/RoomView.vue'
import UserViewVue from '@/view/user/UserView.vue'
import GameWaitingRoomVue from '@/view/room/GameWaitingRoom.vue'
import InitialViewVue from '@/view/home/InitialView.vue'
import UserInfoModifyVue from '@/components/profile/UserInfoModify.vue'
import ChangePasswordVue from '@/components/profile/ChangePassword.vue'
import ScoreCheckVue from '@/components/profile/ScoreCheck.vue'
import RoomListViewVue from '@/view/room/RoomListView.vue'
import CharacterVue from '@/view/game/pick/character.vue'

import { useUserStore } from "@/store/userStore";

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
      component: HomeView,
      meta: { background: 'home' }
    },
    // 방 리스트, 방생성, 게임 대기방
    // router name 알기 쉽게 수정
    {
      path: '/room',
      name: 'room',
      component: RoomViewVue,
      redirect: '/room/list',
      children: [
        {
          path: 'list',
          name: 'list',
          component: RoomListViewVue,
          meta: { background: 'room-list' }
        },
        {
          path: 'wait/:roomNum',
          name: 'wait',
          component: GameWaitingRoomVue,
          meta: { background: 'room-wait' }
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
    // 말 선택 페이지
    {
      path: '/pick',
      name: 'pick',
      component: CharacterVue
    },
  ]
});

// 로그인 여부에 따라 경로 "/" 또는 "/home"으로 계속 리다이렉션 수행
router.beforeEach((to, from, next) => {
  const isLogin = useUserStore().isLogin;

  // routes에 설정된 경로인 경우는 그대로 진행
  if (router.options.routes.some(route => route.path === to.path)) {
    next();
  } else {
    // 설정되지 않은 경로인 경우 로그인 여부에 따라 리다이렉션
    if (isLogin) {
      next("/home");
    } else {
      next("/");
    }
  }
});

export default router