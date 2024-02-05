import { localAxios } from "@/util/http-commons";
import { useUserStore } from "@/store/userStore";

const local = localAxios();

async function userConfirm(param, success, fail) {
  await local.post(`/auth/login`, param).then(success).catch(fail);
}

async function userDoJoin(param, success, fail) {
  await local.post(`/user/join`, param).then(success).catch(fail);
}

async function findByToken(success, fail) {
  local.defaults.headers["Authorization"] = "Bearer " + useUserStore().accessToken;
  await local.get(`/user/info`).then(success).catch(fail);
}

async function tokenRegeneration(success, fail) {
  local.defaults.headers["Authorization"] = "Bearer " + useUserStore().accessToken;
  local.defaults.headers["refreshToken"] = useUserStore().refreshToken;
  await local.post().then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await local.get(`/user/logout/${userid}`).then(success).catch(fail);
}

export { userConfirm, userDoJoin, findByToken, tokenRegeneration, logout };
