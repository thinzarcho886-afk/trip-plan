<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    return-object
    item-value="id"
    item-title="name"
    :loading="status == ApiStatus.LOADING"
    ref="PaymentPickerRef"
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { nextTick } from 'vue';
import { useI18n } from 'vue-i18n';
import { paymentMethodApiResource } from '../../api/resources/paymentMethodResource';

const props = defineProps(['paymentMethodId', 'paymentMethodName', 'params']);
const emits = defineEmits(['update:paymentMethodId', 'update:paymentMethodName']);
const { t } = useI18n();

const items = ref([]);

const modelValue = computed({
  get() {
    return props.paymentMethodId && props.paymentMethodName ? { id: props.paymentMethodId, name: props.paymentMethodName } : null;
  },
  set(e: any) {
    emits('update:paymentMethodId', e.id ?? null);
    emits('update:paymentMethodName', e.name ?? null);
  },
});

const { call, response, status } = useApi();

const PaymentPickerRef = ref<null | any>(null);
const hintText = computed(() => t('selectFirst', { value: t('Payment Method') }));
// const showHint = computed(
//   () => typeof props.BusTypeid != 'undefined' && !props.BusTypeid
// );

const onApiCall = async (params: any) => {
  await call(paymentMethodApiResource.getPaymentMethods, { params:{status:"ACTIVE"}});

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'];
  }
};
onMounted(()=> {
  const params = {
    ...props.params,
    id: props.paymentMethodId,
      page: null,
      size: null,
      sort: 'name,asc',
  }
  onApiCall(params);
})
</script>
