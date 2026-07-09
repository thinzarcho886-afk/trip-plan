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
      <v-date-picker
        color="primary"
        v-model:model-value="dateModel"
        show-adjacent-months
        :title="`${props.label}`"
        :min="props.min"
        :max="props.max"
        rounded="lg"
      >
        <template v-slot:actions>
          <div class="pt-6 d-flex" style="gap: 0.5rem">
            <v-spacer></v-spacer>
            <v-btn variant="text" @click="pickerOpen = false">
              {{ t('Close') }}
            </v-btn>
            <v-btn color="primary" variant="flat" @click="confirmDate">
              {{ t('Confirm') }}
            </v-btn>
          </div>
        </template>
      </v-date-picker>
    </v-menu>
  </v-text-field>
</template>
<script lang="ts" setup>
import { computed } from '@vue/reactivity';
import { ref } from 'vue';
import { formatDate } from '../../utils';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const props = defineProps(['label', 'date', 'min', 'max']);
const emits = defineEmits(['update:date']);

const modelValue = computed({
  get() {
    if (props.date) {
      dateModel.value = new Date(props.date);
    }
    return props.date;
  },
  set(v: string) {
    emits('update:date', v);
  },
});

const pickerOpen = ref<boolean>(false);
const dateModel = ref<Date>(new Date());

const confirmDate = () => {
  modelValue.value = formatDate(dateModel.value, 'yyyy-MM-dd');
  pickerOpen.value = false;
};
</script>
<style></style>
