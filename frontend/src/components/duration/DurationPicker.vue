<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="DurationPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { durationApiResource } from '../../api/resources/durationResource';

const props = defineProps(['durationId', 'durationName', 'params']);
const emits = defineEmits(['update:durationId', 'update:durationName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.durationId && props.durationName ? { id: props.durationId, name: props.durationName } : null;
  },
  set(e: any) {
    emits('update:durationId', e.id ?? null);
    emits('update:durationName', e.name ?? null);
  },
});

const { call, response, status } = useApi();

const DurationPickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Duraiton') }));
// const showHint = computed(
//   () => typeof props.DurationdurationId != 'undefined' && !props.DurationdurationId
// );

const onApiCall = async (params: any) => {
  await call(durationApiResource.getDurations, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.durationId,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})
</script>
