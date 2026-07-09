<template>
  <v-autocomplete
    :items="items"
    v-model:model-Value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    :hint="showHint ? hintText : ''"
    :persistent-hint="showHint"
    ref="floorPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { floorApiResource } from '../../api/resources/floorResource.js';

const props = defineProps(['id', 'name', 'companyId', 'params']);
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

const floorPickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Company') }));
const showHint = computed(
  () => typeof props.companyId != 'undefined' && !props.companyId
);

const onApiCall = async (params: any) => {
  await call(floorApiResource.getList, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};

watch(
  [() => props.companyId, () => props.id],
  async ([companyId, id], [oldCompanyId, oldId]) => {
    const params = {
      ...props.params,
      page: null,
      size: null,
      sort: 'name,asc',
      warehouseId: null,
    };

    if (companyId == oldCompanyId && ((!!id && oldId == '') || id != oldId)) {
      // avoid clearing on change picker value
      return;
    }

    if (!!companyId) {
      items.value = [];

      // to avoid clearing init value
      if (!(!oldCompanyId && !oldId && !!id)) {
        // not init condition
        modelValue.value = { id: '', name: '' };

        await nextTick();

        const { resetValidation } = floorPickerRef.value;
        if (typeof resetValidation == 'function') resetValidation();
      }

      params.companyId = companyId;
    }

    if (
      (typeof props.companyId !== 'undefined' && params.companyId) ||
      typeof props.companyId === 'undefined'
    )
      onApiCall(params);
  },
  {
    immediate: true,
  }
);
</script>
