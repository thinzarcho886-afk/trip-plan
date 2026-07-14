<template>
  <v-autocomplete
    :items="items"
    v-model:model-Value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="customerPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { customerApiResource } from '../../api/resources/customerResource.js';

const props = defineProps(['id', 'name', 'params']);
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

const customerPickerRef = ref<null | any>(null);

const onApiCall = async (params: any) => {
  await call(customerApiResource.getCustomers, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.id,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})

</script>
