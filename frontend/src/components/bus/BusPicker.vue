<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id" 
    item-title="busName"
    :loading="status == ApiStatus.LOADING"
    :label="t('Bus')"
    :disabled="!busTypeId" 
    variant="outlined"
    density="comfortable"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { useI18n } from 'vue-i18n';
import { transportApiResource } from '../../api/resources/transportResource';

// props မှာ busTypeId ကို လက်ခံထားပါ
const props = defineProps(['busId', 'busName', 'busTypeId', 'params']);
const emits = defineEmits(['update:busId', 'update:busName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.busId && props.busName ? { id: props.busId, busName: props.busName } : null;
  },
  set(e: any) {
    emits('update:busId', e.id ?? null);
    emits('update:busName', e.busName ?? null);
  },
});

const { call, response, status } = useApi();

const onApiCall = async (params:any) => {
  // busTypeId မရှိရင် API မခေါ်ပါ
  if (!props.busTypeId) {
    items.value = [];
    return;
  }
  await call(transportApiResource.getTransports, { params});

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    // Backend response format အရ data['list'] သို့မဟုတ် data.content ဖြစ်နိုင်ပါတယ်
    if(!data) return;
    items.value = data ['list'];
  }
};

onMounted(() => {
   const params = {
    ...props.params,
    id: props.busTypeId, // Backend က filter လုပ်ဖို့ပို့ပေးရပါမယ်
    page: null,
    size: null,
    sort: 'busName,asc',
  };
  onApiCall(params);
})
</script>