<template>
  <v-text-field
    :label="`${props.label}`"
    v-model:model-value="modelValue"
    type="datetime-local"
    :min="props.min"
    :max="props.max"
  >
  </v-text-field>
</template>
<script lang="ts" setup>
import { computed } from '@vue/reactivity';

const props = defineProps(['label', 'datetime', 'min', 'max']);
const emits = defineEmits(['update:datetime']);

const modelValue = computed({
  get() {
    return props.datetime;
  },
  set(v: string) {
    emits('update:datetime', v);
  },
});
</script>
<style></style>
