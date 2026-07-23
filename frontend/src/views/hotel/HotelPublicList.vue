<template>
  <v-container fluid class="mt-5 px-6">
    <!-- Loading State -->
    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="primary" size="48"></v-progress-circular>
    </v-row>

    <!-- Empty State -->
    <v-row v-else-if="hotelList.length === 0" class="justify-center py-10">
      <div class="text-grey-darken-1 text-subtitle-1">
        {{ t('No hotel data found') }}
      </div>
    </v-row>

    <!-- 📍 1. Initial State: Destination Name & Hotel Name သာ ပြသသော Card Grid -->
    <v-card v-else class="mt-5 border-0 bg-transparent" flat>
      <v-data-iterator :items="hotelList" :items-per-page="9">
        <template v-slot:default="{ items }">
          <v-row>
            <v-col
              v-for="item in items"
              :key="item.raw.id"
              cols="12"
              sm="6"
              md="4"
              class="mb-4"
            >
              <v-card class="pa-4 rounded-lg border elevation-1 h-100 bg-white d-flex flex-column justify-space-between" flat>
                <div>
                    <!-- 🖼️ Hotel Image (Initial Display) -->
                  <v-img
                    :src="item.raw.imageUrl || 'https://via.placeholder.com/400x250'"
                    height="180"
                    cover
                    class="bg-grey-lighten-2"
                  ></v-img>
                  <!-- Destination Name -->
                  <div class="text-subtitle-1 font-weight-bold text-primary mb-1" >
                    📍 {{ item.raw.destinationName || item.raw.destination }}
                  </div>

                  <!-- Hotel Name -->
                  <div class="text-h6 font-weight-black text-grey-darken-3 mb-3" >
                    {{ item.raw.name }}
                  </div>
                </div>

                <!-- View Detail Button -->
                <div>
                  <v-btn
                    color="primary"
                    variant="flat"
                    size="small"
                    class="text-capitalize rounded-lg font-weight-bold"
                    block
                    @click="openHotelDetail(item.raw)"
                  >
                    {{ t('View Detail') }}
                  </v-btn>
                </div>
              </v-card>
            </v-col>
          </v-row>
        </template>
      </v-data-iterator>
    </v-card>

    <!-- 📍 2. View Detail နှိပ်မှ ပေါ်လာမည့် All Info Dialog (Pop-up) -->
    <v-dialog v-model="detailDialog" max-width="600" scrollable>
      <v-card v-if="selectedHotel" class="rounded-xl overflow-hidden pa-0">
        <!-- Hotel Image -->
        <v-img
          :src="selectedHotel.imageUrl || 'https://via.placeholder.com/600x300'"
          height="250"
          cover
        ></v-img>

        <v-card-text class="pa-6">
          <!-- Hotel Name & Destination -->
          <h2 class="text-h5 font-weight-black text-primary mb-1">
            {{ selectedHotel.name }}
          </h2>
          <div class="text-subtitle-1 font-weight-bold text-grey-darken-1 mb-4">
            📍 {{ selectedHotel.destinationName || selectedHotel.destination }}
          </div>

          <v-divider class="mb-4"></v-divider>

          <!-- All Details List -->
          <div class="d-flex align-center mb-3">
            <v-icon :icon="mdiMapMarker" color="primary" class="mr-3"></v-icon>
            <div>
              <div class="text-caption text-grey">{{ t('Address') }}</div>
              <div class="text-body-1 text-grey-darken-3 font-weight-medium">
                {{ selectedHotel.address || '-' }}
              </div>
            </div>
          </div>

          <div class="d-flex align-center mb-3">
            <v-icon :icon="mdiCash" color="success" class="mr-3"></v-icon>
            <div>
              <div class="text-caption text-grey">{{ t('Price Per Night') }}</div>
              <div class="text-body-1 text-success font-weight-bold">
                {{ selectedHotel.pricePerNight ? `${selectedHotel.pricePerNight}` : '-' }}{{t('MMK')}}
              </div>
            </div>
          </div>

          <div class="d-flex align-start mb-3">
            <v-icon :icon="mdiTextSearch" color="info" class="mr-3 mt-1"></v-icon>
            <div>
              <div class="text-caption text-grey">{{ t('Description') }}</div>
              <div class="text-body-2 text-grey-darken-3 leading-relaxed">
                {{ selectedHotel.description || '-' }}
              </div>
            </div>
          </div>

          <div class="d-flex align-center mb-2">
            <v-icon :icon="mdiCheckCircle" color="green" class="mr-3"></v-icon>
            <div>
              <div class="text-caption text-grey">{{ t('Status') }}</div>
              <v-chip size="small" color="success" class="font-weight-bold" variant="tonal">
                {{ selectedHotel.status }}
              </v-chip>
            </div>
          </div>
        </v-card-text>

        <!-- Close Action Button -->
        <v-card-actions class="pa-4 bg-grey-lighten-4 justify-end">
          <v-btn color="grey-darken-1" variant="text" @click="detailDialog = false">
            {{ t('Close') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api';
import { hotelPublicApiResource } from '../../api/resources/hotelPublicResource.js';
import { 
  mdiMapMarker, 
  mdiCash, 
  mdiTextSearch, 
  mdiCheckCircle 
} from '@mdi/js';

const { t } = useI18n();
const hotelList = ref<any[]>([]);
const selectedHotel = ref<any>(null);
const detailDialog = ref(false);

const { call, response, status } = useApi();

// Get Active Hotels
const onApiCall = async () => {
  try {
    await call(hotelPublicApiResource.getHotels, { 
      params: { status: 'ACTIVE' } 
    });

    if (status.value === ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      const list = resData?.list || resData?.data?.list || [];
      hotelList.value = list.filter((h: any) => h.status === 'ACTIVE');
    }
  } catch (error) {
    console.error('API Error:', error);
  }
};

// Handle Open Dialog Action
const openHotelDetail = (hotel: any) => {
  selectedHotel.value = hotel;
  detailDialog.value = true;
};

onMounted(() => {
  onApiCall();
});
</script>

<style scoped>
.leading-relaxed {
  line-height: 1.6;
}
</style>