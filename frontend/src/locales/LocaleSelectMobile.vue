<template>
  <v-list-group>
    <template v-slot:activator="{ props }">
      <v-list-item
        v-bind="props"
        :title="i18n.t(currentLocale)"
        :prepend-icon="parentProps.item.icon"
      ></v-list-item>
    </template>
    <v-list-item
      v-for="locale in availableLocales"
      :key="locale.value"
      :value="locale.text"
      @click="(menu = false), setLocale(locale.value)"
    >
      <v-list-item-title>{{ i18n.t(locale.text) }}</v-list-item-title>
    </v-list-item>
  </v-list-group>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { availableLocales, setLocale } from '../plugins/i18n';
import { useI18n } from 'vue-i18n';

const parentProps = defineProps(['item', 'menu']);
const emits = defineEmits(['update:menu']);

const menu = computed({
  get() {
    return parentProps.menu;
  },
  set(v: boolean) {
    emits('update:menu', v);
  },
});

const i18n = useI18n({ useScope: 'global' });
const currentLocale = computed(
  () =>
    availableLocales.find((l) => i18n.locale.value === l.value)?.text ??
    i18n.locale.value
);
</script>
