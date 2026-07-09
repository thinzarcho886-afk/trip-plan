<template>
  <v-text-field
    :label="`${props.label}`"
    v-model:model-value="modelValue"
    readonly
  >
    <v-menu
      activator="parent"
      :close-on-content-click="false"
      v-model="pickerOpen"
    >
      <v-time-picker
        color="primary"
        v-model:model-value="value"
        :title="`${props.label}`"
        :min="props.min"
        :max="props.max"
        rounded="lg"
        ampm-in-title
      >
        <template v-slot:actions>
          <div class="pt-6 d-flex" style="gap: 0.5rem">
            <v-spacer></v-spacer>
            <v-btn
              variant="text"
              @click="(pickerOpen = false), (value = modelValue)"
            >
              {{ t('Close') }}
            </v-btn>
            <v-btn color="primary" variant="flat" @click="confirmTime">
              {{ t('Confirm') }}
            </v-btn>
          </div>
        </template>
      </v-time-picker>
    </v-menu>
  </v-text-field>
</template>
<script lang="ts" setup>
import { computed, ref } from '@vue/reactivity';
import { useI18n } from 'vue-i18n';
import { VTimePicker } from 'vuetify/labs/VTimePicker';

const { t } = useI18n();

const props = defineProps(['label', 'time', 'min', 'max']);
const emits = defineEmits(['update:time']);

const modelValue = computed({
  get() {
    value.value = props.time;
    return props.time;
  },
  set(v: string) {
    emits('update:time', v);
  },
});

const value = ref('');
const pickerOpen = ref<boolean>(false);

const confirmTime = () => {
  modelValue.value = value.value;
  pickerOpen.value = false;
};
</script>
<style></style>
