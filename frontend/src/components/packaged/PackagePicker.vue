<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="PackagePickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { packageApiResource } from '../../api/resources/packageResource';

const props = defineProps(['packageId', 'packageName', 'params']);
const emits = defineEmits(['update:packageId', 'update:packageName','change']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.packageId && props.packageName ? { id: props.packageId, name: props.packageName } : null;
  },
  set(e: any) {
    emits('update:packageId', e.id ?? null);
    emits('update:packageName', e.name ?? null);
    emits('change',e);
  },
});

const { call, response, status } = useApi();

const PackagePickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Package') }));
// const showHint = computed(
//   () => typeof props.BusTypeid != 'undefined' && !props.BusTypeid
// );

const onApiCall = async (params: any) => {
  await call(packageApiResource.getPackages, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.packageId,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})
</script>
