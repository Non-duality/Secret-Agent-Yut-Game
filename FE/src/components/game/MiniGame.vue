<template>
  <div class="mini-main">
    <div class="roulette-main">
        <MiniRoulette/>
    </div>
  </div>
</template>

<script>
import MiniRoulette from './item/MiniRoulette.vue'
export default {
    components : {
        MiniRoulette,
    }
}
</script>

<style>
.mini-main{
    position: relative;
    z-index: 1001;
    width: 100%;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
}
.roulette-main{
    position: absolute;
    width: 600px;
    height: 600px;
    top: 75px;
    left: 450px;
    background-color: rgb(0, 187, 187);
    border-radius: 20px;
    border: 5px solid white;
}
</style>