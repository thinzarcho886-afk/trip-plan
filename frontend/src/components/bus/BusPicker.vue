<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="busPickerRef"
    density="comfortable"
    variant="outlined"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { busApiResource } from '../../api/resources/busResource';
const props = defineProps(['busId', 'busName', 'params', 'busTypeId']);
const emits = defineEmits(['update:busId', 'update:busName']);
const { t } = useI18n();

const items = ref([]);

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

const busPickerRef = ref<null | any>(null);

const onApiCall = async (params: any) => {
  await call(busApiResource.getBuses, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'] || [];
  }
};
watch(
  [() => props.busTypeId],
  async ([newBusTypeId], [oldBusTypeId]) => {
    // Bus Type ပြောင်းသွားရင် Bus ကို reset လုပ်ရမယ်
    if (newBusTypeId !== oldBusTypeId) {
      modelValue.value = null; // Bus ကို ရှင်းလိုက်ပါ
    }

    // API ခေါ်မယ့် Params
    const params = {
      ...props.params,
      busTypeId: newBusTypeId, // Bus Type အလိုက် Bus စာရင်းယူမယ်
      page: null,
      size: null,
      sort: 'name,asc',
    };

    if (newBusTypeId) {
      await onApiCall(params);
    } else {
      items.value = []; // Bus Type မရွေးရသေးရင် List ကို ရှင်းထားမယ်
    }
  },
  { immediate: true }
);
// watch(
//   [() => props.busTypeId, () => props.busId],
//   async ([busTypeId, busId], [oldBusTypeId, oldId]) => {
//     const params = {
//       ...props.params,
//       page: null,
//       size: null,
//       sort: 'name,asc',
//     };

//     if (
//       busTypeId == oldBusTypeId &&
//       ((!!busId && oldId == '') || busId != oldId)
//     ) {
//       // value မပြောင်းဘဲ တူနေပါက ဘာမှမလုပ်ဘဲ ကျော်သွားရန်
//       return;
//     }

//     if (!!busTypeId) {
//       items.value = [];

//       // ကနဦးတန်ဖိုး (Init value) ရှင်းလင်းမသွားစေရန် စစ်ဆေးခြင်း
//       if (!(!oldBusTypeId && !oldId && !!busId)) {
//         modelValue.value = { id: '', name: '' };

//         await nextTick();

//         const { resetValidation } = busPickerRef.value;
//         if (typeof resetValidation == 'function') resetValidation();
//       }

//       params.busTypeId = busTypeId;
//     }

//     if (
//       (typeof props.busTypeId !== 'undefined' && params.busTypeId) ||
//       typeof props.busTypeId === 'undefined'
//     )
//       onApiCall(params);
//   },
//   {
//     immediate: true,
//   },
// );
</script>