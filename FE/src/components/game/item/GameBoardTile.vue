<template>
    <div class="tile" :class="tile.position">
        {{ tile.id }}
    </div>
  </template>
  
  <script>
  export default {
    props: ['tile'],
  };
  </script>
  
  <style>
  </style>
  