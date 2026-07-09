<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="hostelName"
    :loading="status == ApiStatus.LOADING"
    ref="hostelPublicPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { hostelPublicApiResource } from '../../api/resources/hostelPublicResource';

const props = defineProps(['hostelPublicId', 'hostelName', 'params']);
const emits = defineEmits(['update:hostelPublicId', 'update:hostelName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.hostelPublicId && props.hostelName ? { id: props.hostelPublicId, hostelName: props.hostelName } : null;
  },
  set(e: any) {
    emits('update:hostelPublicId', e.id ?? null);
    emits('update:hostelName', e.hostelName ?? null);
  },
});

const { call, response, status } = useApi();

const hostelPublicPickerRef = ref<null | any>(null);

const onApiCall = async (params: any) => {
  await call(hostelPublicApiResource.getHostels, { params });

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
      sort: 'hostelName,asc',
  }
  onApiCall(params);
})

</script>