import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/userStore";

import HomeView from "@/view/home/HomeView.vue";
import RoomViewVue from "@/view/room/RoomView.vue";
import UserViewVue from "@/view/user/UserView.vue";
import GameWaitingRoomVue from "@/view/room/GameWaitingRoom.vue";
import InitialViewVue from "@/view/home/InitialView.vue";
import UserInfoModifyVue from "@/components/profile/UserInfoModify.vue";
import ChangePasswordVue from "@/components/profile/ChangePassword.vue";
import ScoreCheckVue from "@/components/profile/ScoreCheck.vue";
import RoomListViewVue from "@/view/room/RoomListView.vue";
import CharacterVue from "@/view/game/pick/character.vue";
import OpenViduVue from "@/view/game/pick/openVidu.vue";

const router = createRouter({
  history: createWebHistory("/"),
  routes: [
    // 로그인 전 초기 화면
    {
      path: "/",
      name: "initial",
      component: InitialViewVue,
    },
    // 로그인 후 메인 화면
    {
      path: "/home",
      name: "home",
      component: HomeView,
      meta: { background: "home" },
    },
    // 방 리스트, 방생성, 게임 대기방
    // router name 알기 쉽게 수정
    {
      path: "/room",
      name: "room",
      component: RoomViewVue,
      redirect: "/room/list",
      children: [
        {
          path: "/room/list",
          name: "/room/list",
          component: RoomListViewVue,
          meta: { background: "room-list" },
        },
        {
          path: "/room/wait/:roomNum",
          name: "wait",
          component: GameWaitingRoomVue,
          meta: { background: "room-wait" },
        },
      ],
    },

    // 사용자 회원정보
    {
      path: "/user",
      name: "user",
      component: UserViewVue,
      redirect: "/user/info",
      children: [
        {
          path: "/user/info",
          name: "user/info",
          component: UserInfoModifyVue,
        },
        {
          path: "/user/password",
          name: "user/password",
          component: ChangePasswordVue,
        },
        {
          path: "/user/records",
          name: "user/records",
          component: ScoreCheckVue,
        },
      ],
    },

    // 말 선택 페이지
    {
      path: "/pick",
      name: "pick",
      component: CharacterVue,
    },

    // (테스트) WebRTC
    {
      path: "/rtc",
      name: 'rtc',
      component: OpenViduVue
    },
  ],
});

// to.path와 일치하는 자식 라우트 찾기
function findChildRouteByPath(routes, pathToFind) {
  let isTrue = false;

  routes.forEach((route) => {
    if (route.children) {
      // 자식 라우트가 있는 경우
      for (let i = 0; i < route.children.length; i++) {
        const path = route.children[i].path;

        // 동적 세그먼트에 대한 처리 추가
        const regexPath = path.replace(/:\w+/g, ".*");
        const regex = new RegExp(`^${regexPath}$`);
        if (regex.test(pathToFind)) {
          // to.path와 일치하는 자식 라우트를 찾음
          isTrue = true;
          break;
        }
      }
    }
  });

  return isTrue;
}

// 리다이렉션 처리
router.beforeEach((to, from, next) => {
  if (from.path.startsWith("/room/wait")) {
    const confirmMessage = "정말 대기방에서 나가시겠습니까?";
    if (confirm(confirmMessage)) {
      // 사용자가 예를 선택하면 다음 단계로 이동
      next();
    } else {
      // 사용자가 아니요를 선택하면 현재 경로에 남아 있음
      next(false);
    }
  }

  const isLogin = useUserStore().isLogin;

  // "/" 경로 처리
  if (to.path === "/") {
    if (isLogin) {
      next("/home");
    } else {
      next();
    }
  }

  // routes에 설정된 경로 중에서 현재 이동하려는 경로가 있는지 확인
  const isRouteExist = router.options.routes.some(
    (route) => route.path === to.path
  );

  // routes 중에 children을 가지고 있는 부모 경로들만을 확인하는 변수
  const matchingChildRoute = findChildRouteByPath(
    router.options.routes,
    to.path
  );

  if (isRouteExist || matchingChildRoute) {
    if (isLogin) {
      next();
    } else {
      next("/");
    }
  } else {
    next("/");
  }
});

export default router;
