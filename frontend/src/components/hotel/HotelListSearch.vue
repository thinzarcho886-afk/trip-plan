<!-- Inside frontend/src/components/hotel/HotelListSearch.vue -->
<template>
  <div class="pa-4" style="min-width: 350px; background-color: white;">
    <v-row dense>
      <v-col cols="12" sm="4">
        <v-text-field
          v-model="searchParams.name"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Hotel Name')"
        ></v-text-field>
      </v-col>

      <!-- Destination Selection Dropdown -->
      <v-col cols="12" sm="4">
        <v-select
          v-model="searchParams.destinationId"
          :items="destinationList"
          item-title="name"
          item-value="id"
          clearable
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Destination')"
          :loading="destinationApi.status.value === ApiStatus.LOADING"
          :no-data-text="t('No data available')"
        ></v-select>
      </v-col>

      <v-col cols="12" sm="4">
        <EnumPicker
          v-model:value="searchParams.status"
          :label="t('Status')"
          :enum="{ [t('ACTIVE')]: Status.ACTIVE, [t('INACTIVE')]: Status.INACTIVE }"
          :add-all="true"
          hide-details
          density="compact"
          variant="outlined"
        ></EnumPicker>
      </v-col>
    </v-row>

    <div class="pt-6 d-flex" style="gap: 0.5rem">
      <v-btn variant="text" @click="onClose"> {{ t('Close') }} </v-btn>
      <v-spacer></v-spacer>
      <v-btn color="primary" variant="text" @click="onReset">
        {{ t('Reset') }}
      </v-btn>
      <v-btn color="primary" variant="flat" @click="onSearch">
        {{ t('Search') }}
      </v-btn>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { Status } from '../../constants/Status';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes';
import { useRouteFilter } from '../../utils/useRouteFilter';
import { useI18n } from 'vue-i18n';
import EnumPicker from '../common/EnumPicker.vue';
import { HotelListParams, HotelListParamsModel } from '../../models/HotelModel';
import useApi, { ApiStatus } from '../../api/index.js';
import { destinationApiResource } from '../../api/resources/destinationResource.js';

const { t } = useI18n({ useScope: 'global' });
const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();
const searchParams = ref<HotelListParams>(HotelListParamsModel());

const destinationApi = useApi();
const destinationList = ref([]);

const onSearch = async () => {
  emits('search', searchParams.value);
  await router
    .replace({
      name: routeNames.hotelList,
      query: searchParams.value,
    })
    .catch(() => {});
  onClose();
};

const onReset = () => {
  searchParams.value = HotelListParamsModel();
  onSearch();
};

const onClose = () => {
  emits('close');
};

const loadActiveDestinations = async () => {
  // Path variable ဖြစ်တဲ့ :status နေရာကို ACTIVE ထည့်သွင်းပြီး ခေါ်ယူပါတယ်
  await destinationApi.call(destinationApiResource.getByStatus, null, { status: 'ACTIVE' });
  if (destinationApi.status.value === ApiStatus.SUCCESS) {
    destinationList.value = destinationApi.response.value?.data || [];
  }
};

defineExpose({ onReset });

onMounted(async () => {
  await loadActiveDestinations();
  const { routeFilter, isFiltered } = useRouteFilter<HotelListParams>(
    searchParams.value,
    route.query
  );
  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>