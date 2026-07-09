<template>
  <v-menu :open-delay="48" :close-delay="48">
    <template #activator="{ props: menuProps }">
      <v-chip
        class="mx-4"
        color="white"
        variant="outlined"
        v-bind="menuProps"
      >
        <v-icon :icon="mdiEarth" start></v-icon>
        {{ i18n.t(currentLocale) }}
      </v-chip>
    </template>
    <v-list>
      <v-list-item
        v-for="locale in availableLocales"
        :key="locale.value"
        :value="locale.text"
        @click="setLocale(locale.value)"
      >
        <v-list-item-title>{{ i18n.t(locale.text) }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script setup lang="ts">
import { mdiEarth } from '@mdi/js';
import { useI18n } from 'vue-i18n';
import { availableLocales, setLocale } from '../plugins/i18n';
import { computed } from 'vue';

interface Props {
  vertical?: undefined | boolean;
}

const props = defineProps<Props>();

const i18n = useI18n({ useScope: 'global' });
const currentLocale = computed(
  () =>
    availableLocales.find((l) => i18n.locale.value === l.value)?.text ??
    i18n.locale.value
);
</script>
