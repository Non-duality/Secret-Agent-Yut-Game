<template>
  <button @click="closeSidebar">friend-close</button>
</template>

<script>
export default {
  methods: {
    closeSidebar() {
      this.$emit('closeSidebar');
    }
  }
}
</script>