<template>
  <v-container class="mt-5">
    <v-card
      class="pa-6"
      elevation="6"
      rounded="lg"
      style="background-color: #c1db9b"
    >
      <h2
        class="text-h5 font-weight-bold #2C5E82--text text--darken-3 mb-6 text-center"
      >
        {{ t('Find the perfect student hostel for your stay') }}
      </h2>
    </v-card>
  </v-container>

  <v-container>
    <div class="d-flex justify-space-between align-center my-4">
      <h3>{{ t('Top Searched Hostels') }}</h3>
      <v-btn
        variant="flat"
        color="#06402B"
        rounded="lg"
        @click="goToDetails()"
        >{{ t('View All') }}</v-btn
      >
    </div>

    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular
        indeterminate
        color="#2C5E82-darken-3"
      ></v-progress-circular>
    </v-row>

    <!-- <v-row v-else-if="histroyList.length === 0" class="justify-center py-10">
      <div class="text-grey text-subtitle-1">
        {{ t('No hostel data found') }}
      </div>
    </v-row> -->
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../api';

const { t } = useI18n();
const gender = ref('');

import { useRouter } from 'vue-router';

const router = useRouter();
const goToDetails = () => {
  router.push({
    name: 'routeNames.hostelPublicList',
  });
};

const { call, response, status } = useApi();

// const onApiCall = async (params: any = {}) => {
//   try {
//     await call(historyPublicApiResource.getMostSearchedHostels, { params });
//     if (status.value == ApiStatus.SUCCESS) {
//       const resData = response.value?.data;
//       histroyList.value = resData?.list || resData?.data?.list || [];
//     }
//   } catch (error) {
//     console.error('API Error:', error);
//   }
// };

// const handleSearch = () => {
//   const activeParams = {
//     hostelName: searchParams.value.hostelName || null,

//     hostelStatus: searchParams.value.hostelStatus || null,
//     cityName: searchParams.value.cityName || null,
//     gender: gender.value || null,
//     streetName: searchParams.value.streetName || null,
//     townshipName: searchParams.value.townshipName || null,
//   };

//   console.log('Sending clean parameters to backend:', activeParams);
//   onApiCall(activeParams);
// };
// const handleClear = () => {
//   gender.value = '';
//   searchParams.value = SearchHistoryListPublicParamsModel();
//   onApiCall();
// };

// onMounted(() => {
//   onApiCall();
// });
</script>
