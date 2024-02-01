import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 게임 방 리스트 데이터 가져오기
async function getRoomSomeList(success, fail) {
  await local.get(`/api/v1/room`).then(success).catch(fail);
}

export {
  getRoomSomeList
}