<template>
  <v-container fluid class="mt-5 px-6">
    <!-- Back Button -->
    <v-row class="mb-3">
      <v-col cols="12">
        <v-btn
          variant="outlined"
          color="primary"
          rounded="lg"
          class="text-capitalize font-weight-bold"
          @click="router.back()"
        >
          <v-icon :icon="mdiArrowLeft" class="mr-1"></v-icon>
          {{ t('Back to Bus Types') }}
        </v-btn>
      </v-col>
    </v-row>

    <!-- Header Section -->
    <v-card v-if="busTypeName" class="pa-4 mb-6 rounded-lg border bg-grey-lighten-4" flat>
      <h3 class="text-h6 font-weight-bold text-primary">
        🚌 {{ t('Buses in Category') }}: {{ busTypeName }}
      </h3>
      <div class="text-subtitle-1 text-amber-darken-3 font-weight-bold mt-1">
        💺 {{ availableSeats }} {{ t('Seats Available') }}
      </div>
    </v-card>

    <!-- Loading State -->
    <v-row v-if="status === ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="primary" size="48"></v-progress-circular>
    </v-row>

    <!-- Empty State -->
    <v-row v-else-if="busList.length === 0" class="justify-center py-10">
      <div class="text-grey-darken-1 text-subtitle-1">
        {{ t('No bus found for this category.') }}
      </div>
    </v-row>

    <!-- Bus List Grid -->
    <v-row v-else>
      <v-col
        v-for="item in busList"
        :key="item.id"
        cols="12"
        sm="6"
        md="4"
        class="mb-4"
      >
        <v-card class="rounded-lg border elevation-1 bg-white h-100 d-flex flex-column overflow-hidden" flat>
          
          <!-- 🖼️ Corrected Image Display Component -->
          <v-img
            :src="getImageUrl( item.imageUrl || item.image)"
            height="180"
            cover
            class="bg-grey-lighten-2"
          >
            <template #placeholder>
              <v-row class="fill-height ma-0" align="center" justify="center">
                <v-progress-circular indeterminate color="primary"></v-progress-circular>
              </v-row>
            </template>
          </v-img>

          <!-- Card Content -->
          <div class="pa-4 flex-grow-1 d-flex flex-column justify-space-between">
            <div>
              <div class="d-flex align-center justify-space-between mb-2">
                <span class="text-h6 font-weight-bold text-grey-darken-3">
                  🚍 {{ item.busName || item.bus?.name || item.name }}
                </span>
                <v-chip color="success" size="x-small" label>
                  {{ item.status || item.bus?.status || 'ACTIVE' }}
                </v-chip>
              </div>
            </div>
          </div>

        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { mdiArrowLeft } from '@mdi/js';
import useApi, { ApiStatus } from '../../api';
import { transportPublicApiResource } from '../../api/resources/transportPublicResource.js';

interface BusItem {
  id: string | number;
  busName?: string;
  name?: string;
  status?: string;
  imageUrl?: string;
  image?: string;
  busImage?: string;
  bus?: {
    name?: string;
    imageUrl?: string;
    status?: string;
  };
}

const { t } = useI18n();
const route = useRoute();
const router = useRouter();

const busTypeName = ref<string>((route.query.busTypeName as string) || '');
const availableSeats = ref<string | number>((route.query.availableSeats as string) || 0);
const busList = ref<BusItem[]>([]);
const { call, response, status } = useApi();

const getImageUrl = (imgPath?: string) => {
  if (!imgPath) return '';

  // HTTP သို့မဟုတ် HTTPS ပါပြီးသားဖြစ်ပါက တိုက်ရိုက်ယူပါမည်
  if (imgPath.startsWith('http://') || imgPath.startsWith('https://')) {
    return imgPath;
  }

  // Nginx / Backend Base URL ကို ချိန်ညှိပါ (ဥပမာ - http://localhost:8080)
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
  return `${baseUrl}${imgPath.startsWith('/') ? '' : '/'}${imgPath}`;
};
const fetchBusesByBusType = async () => {
  const selectedBusTypeId = route.query.busTypeId ? Number(route.query.busTypeId) : null;

  try {
    await call(transportPublicApiResource.getTransports, {
      params: { 
        busTypeId: selectedBusTypeId 
      }
    });

    if (status.value === ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      busList.value = resData?.transportList || resData?.content || resData?.list || resData || [];
    }
  } catch (err) {
    console.error('Fetch Buses Error:', err);
  }
};

onMounted(() => {
  fetchBusesByBusType();
});
</script>