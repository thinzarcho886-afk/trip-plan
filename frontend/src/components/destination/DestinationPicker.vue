<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="DestinationPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { destinationApiResource } from '../../api/resources/destinationResource';

const props = defineProps(['destinationId', 'destinationName', 'params']);
const emits = defineEmits(['update:destinationId', 'update:destinationName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.destinationId && props.destinationName ? { id: props.destinationId, name: props.destinationName } : null;
  },
  set(e: any) {
    emits('update:destinationId', e.id ?? null);
    emits('update:destinationName', e.name ?? null);
  },
});

const { call, response, status } = useApi();

const DestinationPickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Destination') }));
// const showHint = computed(
//   () => typeof props.destinationdestinationId != 'undefined' && !props.destinationdestinationId
// );

const onApiCall = async (params: any) => {
  await call(destinationApiResource.getDestinations, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.destinationId,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})
</script>
