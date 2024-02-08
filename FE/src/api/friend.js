import { localAxios } from "@/util/http-commons";
import { useUserStore } from "@/store/userStore";

const local = localAxios();

async function getMyFriend(success, fail) {
  local.defaults.headers["Authorization"] =
    "Bearer " + useUserStore().accessToken;
  await local.get(`/friend/myfriend`).then(success).catch(fail);
}

export { getMyFriend };
