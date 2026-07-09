<template>
  <v-autocomplete
    :items="items"
    v-model:model-value="modelValue"
    :item-value="customItemValue"
    :item-title="customItemTitle"
    :loading="status == ApiStatus.LOADING"
    ref="userPickerRef"
    :label="label"
    :rules="rules"
    :readonly="readonly"
    clearable
  ></v-autocomplete>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { userApiResource } from '../../api/resources/userResource';

const props = defineProps(['studentId', 'ownerId', 'params', 'label', 'rules', 'readonly']);
const emits = defineEmits(['update:studentId', 'update:ownerId']);
const { t } = useI18n();

const items = ref([]);

const customItemValue = computed(() => {
  if (props.params?.roleName === 'OWNER') {
    return 'ownerId';
  }
  return 'studentId';

});

const customItemTitle = computed(() => {
  if (props.params?.roleName === 'OWNER') {
    return 'ownerName';
  }
  return 'studentName';

});

const modelValue = computed({
  get() {
    return props.studentId || props.ownerId || null;
  },
  set(newValue: any) {
    if (!newValue) {
      emits('update:studentId', null);
      emits('update:ownerId', null);
      return;
    }

    if (props.params?.roleName === 'STUDENT') {
      emits('update:studentId', newValue);
      emits('update:ownerId', null);
    } else if (props.params?.roleName === 'OWNER') {
      emits('update:ownerId', newValue);
      emits('update:studentId', null);
    }
  },
});

const { call, response, status } = useApi();
const userPickerRef = ref<null | any>(null);

const onApiCall = async () => {
  console.log('Picker params:', props.params);

  const params = {
    ...props.params,
    page: null,
    size: null,
    sort: 'id,asc',
  };

  await call(userApiResource.getList, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;
    items.value = data['list'] || data;
  }
};

watch(
  () => props.params?.role,
  async (newRole) => {
    if (newRole) {
      await onApiCall();
    }
  }
);

onMounted(() => {
  onApiCall();
});
</script>
