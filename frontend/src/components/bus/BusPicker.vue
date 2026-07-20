<template>
  <v-autocomplete
    :items="items"
    v-model="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    :disabled="!busTypeId" 
    :label="t('Bus')"
    ref="busPickerRef"
    density="comfortable"
    variant="outlined"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, ref, watch, nextTick } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { useI18n } from 'vue-i18n';
import { busApiResource } from '../../api/resources/busResource';

const props = defineProps(['busId', 'busName', 'params', 'busTypeId']);
const emits = defineEmits(['update:busId', 'update:busName']);
const { t } = useI18n();

const items = ref([]);
const busPickerRef = ref<null | any>(null);

// v-autocomplete အတွက် value ချိတ်ဆက်ခြင်း
const modelValue = computed({
  get() {
    return props.busId && props.busName
      ? { id: props.busId, name: props.busName }
      : null;
  },
  set(e: any) {
    emits('update:busId', e?.id ?? null);
    emits('update:busName', e?.name ?? null);
  },
});

const { call, response, status } = useApi();

const onApiCall = async (params: any) => {
  // busTypeId မရှိရင် API မခေါ်ဘဲ ပိတ်ထားမယ်
  if (!props.busTypeId) {
    items.value = [];
    return;
  }

  await call(busApiResource.getBuses, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'] || [];
  }
};

// watch ထဲမှာ immediate: true ပါဝင်ပြီးသားဖြစ်လို့ onMounted ထပ်ရေးစရာမလိုတော့ပါဘူး
watch(
  [() => props.busTypeId, () => props.busId],
  async ([busTypeId, busId], [oldBusTypeId, oldId]) => {
    const params: any = {
      ...props.params,
      page: null,
      size: null,
      sort: 'name,asc',
    };

    if (
      busTypeId == oldBusTypeId &&
      ((!!busId && oldId == '') || busId != oldId)
    ) {
      // value မပြောင်းဘဲ တူနေပါက ဘာမှမလုပ်ဘဲ ကျော်သွားရန်
      return;
    }

    if (!!busTypeId) {
      items.value = [];

      // ကနဦးတန်ဖိုး (Init value) ရှင်းလင်းမသွားစေရန် စစ်ဆေးခြင်း
      if (!(!oldBusTypeId && !oldId && !!busId)) {
        modelValue.value = { id: '', name: '' };

        await nextTick();

        const { resetValidation } = busPickerRef.value || {};
        if (typeof resetValidation == 'function') resetValidation();
      }

      params.busTypeId = busTypeId; // Backend ကို filter လုပ်ဖို့ busTypeId ထည့်ပေးလိုက်ခြင်း
    }

    if (
      (typeof props.busTypeId !== 'undefined' && params.busTypeId) ||
      typeof props.busTypeId === 'undefined'
    ) {
      onApiCall(params);
    }
  },
  {
    immediate: true,
  },
);
</script>