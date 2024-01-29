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
          component: RoomListViewVue
        },
        {
          path: 'wait/:roomNum',
          name: 'wait',
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
});

// 로그인 여부에 따라 경로 "/" 또는 "/home"으로 계속 리다이렉션 수행
router.beforeEach((to, from, next) => {
  const isLogin = sessionStorage.getItem("isLogin");

  if (to.meta.requiresAuth && !isLogin) {
    // 로그인이 필요한 페이지인데 로그인이 안된 경우
    next("/");
  } else if (to.name === 'initial' && isLogin) {
    // 초기 화면인데 이미 로그인된 경우
    next("/home");
  } else {
    // 그 외의 경우는 정상적으로 진행
    next();
  }
});

export default router
