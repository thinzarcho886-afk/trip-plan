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
import { ref, computed, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { busTypeApiResource } from '../../api/resources/busTypeResource';

const props = defineProps(['busTypeId', 'busTypeName', 'params']);
const emits = defineEmits(['update:busTypeId', 'update:busTypeName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.busTypeId && props.busTypeName ? { id: props.busTypeId, name: props.busTypeName } : null;
  },
  set(e: any) {
    emits('update:busTypeId', e.id ?? null);
    emits('update:busTypeName', e.name ?? null);
  },
});

const { call, response, status } = useApi();

const BusTypePickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Bus Type') }));
// const showHint = computed(
//   () => typeof props.BusTypeid != 'undefined' && !props.BusTypeid
// );


const onApiCall = async (params: any) => {
  await call(busTypeApiResource.getBusTypes, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.busTypeId,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})
</script>
