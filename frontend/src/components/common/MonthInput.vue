<template>
  <v-text-field
    :label="`${props.label}`"
    v-model:model-value="modelValue"
    type="month"
    :min="props.min"
    :max="props.max"
  >
  </v-text-field>
</template>
<script lang="ts" setup>
import { computed } from '@vue/reactivity';

const props = defineProps(['label', 'month', 'min', 'max']);
const emits = defineEmits(['update:month']);

const modelValue = computed({
  get() {
    return props.month;
  },
  set(v: string) {
    emits('update:month', v);
  },
});
</script>
<style></style>
