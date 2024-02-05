<template>
  <div class="mini-main">
    <div class="card-main">
        <MiniCard/>
    </div>
  </div>
</template>

<script>
import MiniCard from './item/MiniCard.vue';
export default {
    components : {
        MiniCard,
    }
}
</script>

<style>
@import "@/assets/css/game/miniGame.css";
</style>