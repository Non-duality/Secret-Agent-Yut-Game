<template>
  <button @click="closeSidebar">message-close</button>
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