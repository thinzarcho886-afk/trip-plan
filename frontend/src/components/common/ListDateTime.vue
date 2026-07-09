<template>
  <span>
    {{ formatDate(props.milliseconds, 'yyyy-MM-dd') }}
  </span>
  <span class="text-caption text-grey">
    {{ ' ' + formatDate(props.milliseconds, 'hh:mm:ss a') }}
  </span>
</template>

<script setup lang="ts">
import { formatDate } from '../../utils';

const props = defineProps(['milliseconds']);
</script>
