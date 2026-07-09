<template>
  <v-chip :color="color" density="compact">
    {{ props.isDefault ? 'Default' : 'N/A' }}
  </v-chip>
</template>
<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps(['isDefault']);
const color = computed(() => (props.isDefault === true ? 'success' : 'error'));
</script>
