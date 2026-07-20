<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="busName"
    :loading="status == ApiStatus.LOADING"
    :disabled="!busTypeId"
    :label="t('Bus')"
    density="comfortable"
    variant="outlined"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { useI18n } from 'vue-i18n';
import { transportApiResource } from '../../api/resources/transportResource';

const props = defineProps(['busId', 'busName', 'params', 'busTypeId']);
const emits = defineEmits(['update:busId', 'update:busName']);
const { t } = useI18n();

const items = ref<any[]>([]);

const { call, response, status } = useApi();


const modelValue = computed({
  get() {
    return props.busId && props.busName
      ? { id: props.busId, busName: props.busName }
      : null;
  },
  set(e: any) {
    emits('update:busId', e?.id ?? null);
    emits('update:busName', e?.busName ?? null);
  },
});

const onApiCall = async () => {
  if (!props.busTypeId) {
    items.value = [];
    return;
  }

  await call(transportApiResource.getTransports, {
    params: {
      ...props.params,
      busTypeId: props.busTypeId,
      page: null,
      size: null,
      sort: 'busName,asc'
    }
  });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    
    const rawList = Array.isArray(data) ? data : (data['list'] || []);
    
    items.value = rawList.map((item: any) => {
      return {
        id: item.id,
        busName: item.busName
      };
    });
  }
};

watch(
  () => props.busTypeId,
  (busTypeId) => {
    if (!!busTypeId) {
      onApiCall();
    } else {
      items.value = [];
      modelValue.value = null;
    }
  },
  {
    immediate: true,
  },
);
</script>