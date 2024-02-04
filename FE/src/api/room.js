import { localAxios } from "@/util/http-commons";

const local = localAxios();

function getRoomList(success, fail){
  local.get('/room').then(success).catch(fail);
}

function getRoomDetail(roomId, success, fail){
  local.get('/room/detail/' + `${roomId}`).then(success).catch(fail);
}

function canEnterRoom(roomId, password, success, fail){
  local.post('/room/' + `${roomId}`, JSON.stringify(password)).then(success).catch(fail);
}

export {getRoomList, getRoomDetail, canEnterRoom};