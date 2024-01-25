<template>
  <div class="container">
    <div class="box">
      <!-- 회원정보 수정 -->
      <router-link to="/user/info" class="router-link">회원정보 수정</router-link>
  
      <!-- 비밀번호 변경 -->
      <router-link to="/user/password" class="router-link">비밀번호 변경</router-link>
  
      <!-- 전적 조회 -->
      <router-link to="/user/records" class="router-link">전적 조회</router-link>
  
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  
}
</script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.box {
  padding: 20px;
  width: 700px;
  height: 400px;
  box-shadow: 0 4px 8px rgba(1, 1, 1, 0.7);
  border-radius: 8px;
  text-align: center;
  background-color: rgba(6, 6, 6, 0.7);
}

.router-link {
  display: inline-block;
  width: 130px;
  margin: 10px;
  padding: 10px;
  background-color: #050505;
  color: #fff;
  text-decoration: none;
  text-align: center;
  border: 2px solid white;
  border-radius: 10px;
  transition: background-color 0.3s ease;
  font-weight: bold;
}
</style>
