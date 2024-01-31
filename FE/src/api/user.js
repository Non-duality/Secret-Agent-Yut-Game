import { localAxios, stationAxios } from "@/util/http-commons";
import { useUserStore } from "@/store/userStore";

const local = localAxios();

async function userConfirm(param, success, fail) {
  await local.post(`/auth/login`, param).then(success).catch(fail);
}

async function userJoin(param, success, fail) {
  await local.post(`/user/join`, param).then(success).catch(fail);
}

async function findById(userid, success, fail) {
  local.defaults.headers["Authorization"] = "Bearer " + useUserStore().accessToken;
  await local.get(`/user/info/${userid}`).then(success).catch(fail);
}

async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] = sessionStorage.getItem("refreshToken"); //axios header에 refresh-token 셋팅
  await local.post(`/user/refresh`, user).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await local.get(`/user/logout/${userid}`).then(success).catch(fail);
}

export { userConfirm, userJoin, findById, tokenRegeneration, logout };
