<template>
  <v-img
    src="../assets/images/uni.jpg"
    height="100%"
    width="100%"
    cover
  ></v-img>
</template>

<script lang="ts" setup>
import { useAuthStore } from '../store/auth';

const authStore = useAuthStore();
</script>
