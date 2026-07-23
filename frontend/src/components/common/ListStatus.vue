<template>
  <v-chip :color="color" density="compact">
    <v-icon :icon="mdiCircle" :color="color" size="sm" start> </v-icon>
    {{ t(props.status) }}
  </v-chip>
</template>
<script setup lang="ts">
import { mdiCircle } from '@mdi/js';
import { Status } from '../../constants/Status';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
const props = defineProps(['status']);
const { t } = useI18n({ useScope: 'global' });
const errorList = ['INACTIVE', 'CANCEL'];
const successList = ['ACTIVE', 'FINISH', 'CONFIRM'];
const infoList = ['PENDING'];
const warningList = ['DRAFT'];

const color = computed(() =>
  successList.includes(props.status)
    ? 'success'
    : errorList.includes(props.status)
    ? 'error'
    : infoList.includes(props.status)
    ? 'info'
    : warningList.includes(props.status)
    ? 'warning'
    : ''
);
</script>
