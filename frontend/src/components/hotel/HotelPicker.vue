<template>
  <v-autocomplete
    :items="items"
    v-model:model-Value="modelValue"
    return-object
    item-value="id"
    item-title="hotelName"
    :loading="status == ApiStatus.LOADING"
    ref="hotelPublicPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { hotelApiResource } from '../../api/resources/hotelResource';

const props = defineProps(['hotelId', 'hotelName', 'params']);
const emits = defineEmits(['update:hotelId', 'update:hotelName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.hotelId && props.hotelName ? { id: props.hotelId, hotelName: props.hotelName } : null;
  },
  set(e: any) {
    emits('update:hotelId', e.id ?? null);
    emits('update:hotelName', e.hotelName ?? null);
  },
});

const { call, response, status } = useApi();

const hotelPickerRef = ref<null | any>(null);

const onApiCall = async (params: any) => {
  await call(hotelApiResource.getList, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};

onMounted(()=> {
  const params = {
    ...props.params,
      page: null,
      size: null,
      sort: 'hotelName,asc',
  }
  onApiCall(params);
})

</script>