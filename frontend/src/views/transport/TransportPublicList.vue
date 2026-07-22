<template>
  <v-container fluid class="mt-5 px-6">
    <h2 class="text-h5 font-weight-bold text-center text-uppercase mb-6 text-grey-darken-3">
      {{ t('TRANSPORT CARS') }}
    </h2>

    <!-- Search Input Bar -->
    <v-row class="justify-center mb-6">
      <v-col cols="12" sm="8" md="6" lg="5">
        <v-text-field
          v-model="searchQuery"
          :placeholder="t('Search bus type or bus name..')"
          variant="outlined"
          density="comfortable"
          hide-details
          clearable
          :prepend-inner-icon="mdiMagnify"
          class="bg-white rounded-lg"
          @click:clear="onClearSearch"
        ></v-text-field>
      </v-col>
    </v-row>

    <!-- Loading State -->
    <v-row v-if="status === ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="primary" size="48"></v-progress-circular>
    </v-row>

    <!-- Empty State -->
    <v-row v-else-if="filteredBusTypeList.length === 0" class="justify-center py-10">
      <div class="text-grey-darken-1 text-subtitle-1">
        {{ t('No transport data found.') }}
      </div>
    </v-row>

    <!-- Bus Type Grid -->
    <v-row v-else>
      <v-col
        v-for="item in filteredBusTypeList"
        :key="item.id"
        cols="12"
        sm="6"
        md="4"
        class="mb-4"
      >
        <v-card class="rounded-lg border elevation-1 bg-white h-100 d-flex flex-column overflow-hidden" flat>
          <!-- 🖼️ Dynamic Image Display (Upload တင်ထားသော ပုံရိပ်ကို ပြသခြင်း) -->
          <v-img
            :src="getImageUrl(item.imageUrl || item.image)"
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
              <div class="text-h6 font-weight-black text-grey-darken-3 mb-2">
                🚌 {{ item.name }}
              </div>
              <div class="text-subtitle-2 text-amber-darken-3 mb-3">
                💺 {{ item.availableSeats ?? 0 }} {{ t('Seats Available') }}
              </div>
              <div class="text-body-2 text-grey-darken-1 mb-4">
                {{ item.description || t('No description available') }}
              </div>
            </div>

            <!-- Click button to go to BusPublicList -->
            <v-btn
              color="primary"
              variant="flat"
              block
              class="rounded-lg text-capitalize font-weight-bold"
              @click="goToBusList(item)"
            >
              {{ t('View Detail') }}
            </v-btn>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { busTypeApiResource } from '../../api/resources/busTypeResource.js';
import { routeNames } from '../../router/routes.js';
import { mdiMagnify } from '@mdi/js';

interface BusType {
  id: string | number;
  name: string;
  availableSeats?: number;
  description?: string;
  imageUrl?: string;
  image?: string;
}

const { t } = useI18n();
const router = useRouter();

const busTypeList = ref<BusType[]>([]);
const searchQuery = ref<string>('');

const { call, response, status } = useApi();

// 📍 Upload လုပ်ထားသော ပုံရိပ် Path ကို Nginx/Backend Base URL ဖြင့် ချိတ်ဆက်ပေးသည့် Function
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

// Client-side search filtering
const filteredBusTypeList = computed(() => {
  if (!searchQuery.value.trim()) return busTypeList.value;

  const query = searchQuery.value.toLowerCase().trim();
  return busTypeList.value.filter((item) => {
    const nameMatch = item.name?.toLowerCase().includes(query);
    const descMatch = item.description?.toLowerCase().includes(query);
    return nameMatch || descMatch;
  });
});

// Fetch Bus Type List
const fetchBusTypes = async () => {
  try {
    await call(busTypeApiResource.getBusTypes, {
      params: { status: 'ACTIVE', page: 0, size: 100 }
    });

    if (status.value === ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      busTypeList.value = resData?.content || resData?.list || resData || [];
    }
  } catch (err) {
    console.error('Fetch Bus Types Error:', err);
  }
};
// Search Query ကို Reset ပြန်လုပ်ပေးမည့် Function
const onClearSearch = () => {
  searchQuery.value = '';
};
const goToBusList = (item: BusType) => {
  router.push({
    name: routeNames.busPublicList,
    query: {
      busTypeId: String(item.id),
      busTypeName: item.name,
      availableSeats: String(item.availableSeats ?? 0),
    },
  });
};

onMounted(() => {
  fetchBusTypes();
});
</script>