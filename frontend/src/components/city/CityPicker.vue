<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="cityName"
    :loading="status == ApiStatus.LOADING"
    ref="cityPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { CityApiResource } from '../../api/resources/cityResource';

const props = defineProps(['cityId', 'cityName', 'params']);
const emits = defineEmits(['update:cityId', 'update:cityName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.cityId && props.cityName ? { id: props.cityId, cityName: props.cityName } : null;
  },
  set(e: any) {
    emits('update:cityId', e.id ?? null);
    emits('update:cityName', e.cityName ?? null);
  },
});

const { call, response, status } = useApi();

const cityPickerRef = ref<null | any>(null);

const onApiCall = async (params: any) => {
  await call(CityApiResource.getList, { params });

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
      sort: 'cityName,asc',
  }
  onApiCall(params);
})

</script>
