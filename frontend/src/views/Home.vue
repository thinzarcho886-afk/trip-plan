<template>
  <v-img
  src="../assets/ttmountain.jpg"
  height="100%"
  width="100%"
  cover
  class="d-flex align-center justify-center text-center"
>
  <div class="d-flex flex-column align-center justify-center text-white pa-4">
    <h1 class="text-h3 text-md-h2 font-weight-black text-uppercase mb-2">
      {{ t('Budget Friendly') }}
    </h1>
    
    <h2 class="text-h5 text-md-h4 font-weight-bold text-white-darken-4 mt-3">
      {{ t('Trip Planner Management System') }}
    </h2>
  </div>
</v-img>
</template>

<script lang="ts" setup>
import { useAuthStore } from '../store/auth';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const authStore = useAuthStore();
</script>
