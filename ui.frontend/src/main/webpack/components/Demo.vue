<script setup>
  import { defineProps } from 'vue';
  const props = defineProps(['message']);

  const defaultMessage = 'World';
  const getMessage = () => props.message || defaultMessage;
</script>

<template>
  <p>Hello {{ getMessage() }}!</p>
</template>

<style scoped>
  p {
    color: #42d392;
  }
</style>

