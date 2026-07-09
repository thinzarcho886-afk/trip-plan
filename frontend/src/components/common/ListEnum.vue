<template>
  <v-chip :color="data.color" label variant="outlined">
    <v-icon v-if="data.icon" :icon="data.icon" :color="data.color" start>
    </v-icon>
    {{ props.value }}
  </v-chip>
</template>

<script setup lang="ts">
import { mdiAccountCog } from '@mdi/js';
import { computed } from 'vue';
import { Menu } from '../../constants/EnumMenu';
import { Role } from '../../constants/Role';

const props = defineProps(['value', 'menu']);

interface ChipData {
  value: string;
  menu: Menu | string;
  color: string;
  icon: string;
}

const dataList: ChipData[] = [
  {
    value: Role.SYSADMIN,
    menu: Menu.User,
    color: 'primary',
    icon: mdiAccountCog,
  },
];

const data = computed<ChipData>(
  () =>
    dataList.find((d) => d.value === props.value && d.menu === props.menu) ?? {
      value: props.value,
      menu: '',
      color: '',
      icon: '',
    }
);
</script>
