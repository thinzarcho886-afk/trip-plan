<template>
  <v-row>
    <!-- Destination Name Search -->
    <v-col cols="12" md="6">
      <v-text-field
        density="compact"
        variant="outlined"
        hide-details
        :label="t('Destination')"
        v-model="searchParams.name"
      ></v-text-field>
    </v-col>

    <!-- Status Search (All, Active, Inactive) -->
    <v-col cols="12" md="6">
      <v-select
        v-model="searchParams.status"
        :label="t('Status')"
        :items="[
          { title: t('All'), value: null },
          { title: t('ACTIVE'), value: Status.ACTIVE },
          { title: t('INACTIVE'), value: Status.INACTIVE }
        ]"
        hide-details
        density="compact"
        variant="outlined"
      ></v-select>
    </v-col>
  </v-row>

  <!-- Buttons -->
  <div class="pt-6 d-flex" style="gap: 0.5rem">
    <v-btn variant="text" @click="onClose"> {{ t('Close') }} </v-btn>
    <v-spacer></v-spacer>
    <v-btn color="primary" variant="text" @click="onReset">{{ t('Reset') }}</v-btn>
    <v-btn color="primary" variant="flat" @click="onSearch">{{ t('Search') }}</v-btn>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { Status } from '../../constants/Status.js';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes.js';
import { useI18n } from 'vue-i18n';
import { DestinationListParams, DestinationListParamsModel } from '../../models/DestinationModel.js';

const { t } = useI18n({ useScope: 'global' });
const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();

const searchParams = ref<DestinationListParams>(DestinationListParamsModel());

const onSearch = async () => {
  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.destinationList,
      query: { ...searchParams.value },
    })
    .catch(() => {});

  onClose();
};

const onReset = () => {
  searchParams.value = DestinationListParamsModel();
  onSearch();
};

const onClose = () => {
  emits('close');
};

defineExpose({
  onReset,
});

onMounted(() => {
  // Route query ထဲက အချက်အလက်များ ရှိမရှိ စစ်ဆေးပြီး အလိုအလျောက် Search လုပ်ခြင်း
  if (Object.keys(route.query).length > 0) {
    searchParams.value = { ...searchParams.value, ...route.query };
  }
  onSearch();
});
</script>
