<template>
  <v-select
    v-model:model-value="modelValue"
    :items="list"
    item-title="label"
    item-value="value"
  ></v-select>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const props = defineProps(['value', 'enum', 'addAll']);
const emits = defineEmits(['update:value']);
const { t } = useI18n({ useScope: 'global' });

const modelValue = computed({
  get() {
    return props.value;
  },
  set(v) {
    emits('update:value', v);
  },
});

const list = computed(() => {
  if (props.addAll)
    return [
      { label: t('All'), value: '' },
      ...Object.entries(props.enum).map((s) => {
        return { label: s[0], value: s[1] };
      }),
    ];
  else
    return [
      ...Object.entries(props.enum).map((s) => {
        return { label: s[0], value: s[1] };
      }),
    ];
});
</script>
