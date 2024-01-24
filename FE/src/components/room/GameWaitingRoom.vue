<template>
  <div>
    <button @click="goTolist">나가기</button>
  </div>
</template>

<script>
import { useUserStore } from "@/store/userStore";
import { useRouter } from 'vue-router';

export default {
  setup() {
    const store = useUserStore();
    const router = useRouter();

    const goTolist = () => {
      store.toggleNav();
      router.push("/room");
    };

    return {
      goTolist,
    };
  },
};
</script>

<style>
</style>