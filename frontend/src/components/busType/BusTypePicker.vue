<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="BusTypePickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { busTypeApiResource } from '../../api/resources/busTypeResource';

const props = defineProps(['id', 'name', 'params']);
const emits = defineEmits(['update:id', 'update:name']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.id && props.name ? { id: props.id, name: props.name } : null;
  },
  set(e: any) {
    emits('update:id', e.id ?? null);
    emits('update:name', e.name ?? null);
  },
});

const { call, response, status } = useApi();

const BusTypePickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Bus Type') }));
// const showHint = computed(
//   () => typeof props.BusTypeId != 'undefined' && !props.BusTypeId
// );

const onApiCall = async (params: any) => {
  await call(busTypeApiResource.getBusTypes, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
</script>
