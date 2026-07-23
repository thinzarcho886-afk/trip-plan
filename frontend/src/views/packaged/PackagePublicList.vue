<template>
  <v-container fluid class="mt-5 px-6">
    <!-- Loading State -->
    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="primary" size="48"></v-progress-circular>
    </v-row>

    <!-- Error State -->
    <v-row v-else-if="status == ApiStatus.ERROR" class="justify-center py-10">
      <div class="text-error text-subtitle-1">
        {{ error?.message || t('Failed to load package details') }}
      </div>
    </v-row>

    <!-- Main Content Grid -->
    <v-container v-else-if="packageModel && packageModel.id" class="pa-0">
      <v-row>
        
        <!-- 📍 Left Side: Package Information & Booking -->
        <v-col cols="12" sm="6" md="6" class="pr-sm-4">
          <!-- Destination & Package Name -->
          <div class="mb-4">
            <h2 class="text-h6 font-weight-bold text-light-green-darken-3 mb-1">
              {{t('Destination')}}: {{ packageModel.destinationName || '-' }}
            </h2>
            <h1 class="text-h5 font-weight-black text-light-green-darken-4 mb-2">
              {{t('Package Name')}}: {{ packageModel.name || '-' }}
            </h1>
            
            <div v-if="packageModel.description" class="d-flex align-center my-2 text-body-1 font-weight-medium text-grey-darken-3">
              <span class="mr-2">👉</span>
              <span>{{ packageModel.description }}</span>
            </div>
          </div>

          <!-- Places to Visits (List format) -->
          <div class="mb-6">
            <div class="d-flex align-center font-weight-bold text-subtitle-1 text-grey-darken-3 mb-2">
              <span class="mr-2">🚌</span>
              <span>{{t('Places to visits')}}</span>
            </div>
            <ul class="pl-8 text-body-2 font-weight-bold text-grey-darken-3">
              <li 
                v-for="(place, index) in packageModel.packageDetails" 
                :key="place.id || index"
                class="mb-1"
              >
                {{ place.placeToVisit }}
              </li>
              <li v-if="!packageModel.packageDetails || packageModel.packageDetails.length === 0" class="text-grey">
                {{t('No places listed')}}
              </li>
            </ul>
          </div>

          <!-- Price & Fees Breakdown -->
          <div class="mb-6 text-body-2 font-weight-bold text-grey-darken-3">
            <div class="d-flex mb-2">
              <span style="width: 140px;">{{t('Budget Amount')}}:</span>
              <span>{{ packageModel.budgetAmount ? `${packageModel.budgetAmount}` : '-' }}{{t('MMK')}}</span>
            </div>
            <div class="d-flex mb-1">
              <span style="width: 140px;">{{t('Transport Fee')}}:</span>
              <span>: {{ packageModel.transportFee ? `${packageModel.transportFee}` : '-' }}{{t('MMK')}}</span>
            </div>
            <div class="d-flex mb-1">
              <span style="width: 140px;">{{t('Hotel Fee')}}:</span>
              <span>: {{ packageModel.hotelFee ? `${packageModel.hotelFee}` : '-' }}{{t('MMK')}}</span>
            </div>
            <div class="d-flex mb-1">
              <span style="width: 140px;">{{t('Service Fee')}}:</span>
              <span>: {{ packageModel.serviceFee ? `${packageModel.serviceFee}` : '-' }}{{t('MMK')}}</span>
            </div>
          </div>

          <!-- Transport, Hotel & Additional Services Info -->
          <div class="mb-6 text-body-2 font-weight-bold text-grey-darken-3">
            <div class="d-flex mb-2">
              <span style="width: 140px;">{{t('Bus Type')}}:</span>
              <span>{{ packageModel.busTypeName || '-' }}</span>
            </div>
            <div class="d-flex mb-2">
              <span style="width: 140px;">{{t('Bus Name')}}:</span>
              <span>{{ packageModel.busName || '-' }}</span>
            </div>
            <div class="d-flex mb-2">
              <span style="width: 140px;">{{t('Hotel Name')}}:</span>
              <span>{{ packageModel.hotelName || '-' }}</span>
            </div>
            <div class="d-flex mb-2">
              <span style="width: 140px;">{{t('Duration')}}:</span>
              <span>{{ packageModel.durationName || '-' }}</span>
            </div>
            <div class="d-flex align-start mb-2">
              <span style="width: 140px;" class="flex-shrink-0">{{t('Extra Services')}}:</span>
              <span>{{ packageModel.extraService || '-' }}</span>
            </div>
          </div>

          <!-- Booking Button -->
          <div class="mt-8">
            <v-btn
              color="#82B139"
              variant="flat"
              size="large"
              class="text-white text-capitalize rounded-lg font-weight-bold px-10"
              style="width: 280px;"
              @click="handleBooking"
            >
              {{t('Booking')}}
            </v-btn>
          </div>
        </v-col>

        <!-- 📍 Right Side: Places to Visit Images & Titles -->
        <v-col cols="12" sm="6" md="6" class="pl-sm-4">
          <div class="d-flex flex-column align-center">
            <div 
              v-for="(item, idx) in packageModel.packageDetails" 
              :key="item.id || idx"
              class="mb-6 w-100 text-center"
              style="max-width: 450px;"
            >
              <!-- Image with Fallback handling -->
             <v-img
  :src="getImageUrl(item)"
  height="220"
  cover
  class="rounded-lg mb-2 border elevation-1 bg-grey-lighten-2"
>
  <template v-slot:placeholder>
    <div class="d-flex align-center justify-center fill-height">
      <v-progress-circular indeterminate color="primary" size="24"></v-progress-circular>
    </div>
  </template>
</v-img>

              <!-- Place Title Below Image -->
              <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mt-1">
                {{ item.placeToVisit }}
              </div>
            </div>
          </div>
        </v-col>

      </v-row>
    </v-container>
  </v-container>
</template>

<script setup lang="ts">
import { useAppStore } from '../../store/app';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../../api/index.js';
import { packageApiResource } from '../../api/resources/packageResource.js';
import { Package } from '../../models/PackageModel.js';

const appStore = useAppStore(); 

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const { call, response, error, status } = useApi();

const packageModel = ref<Package | any>({});

// Image URL Selector Function
// Backend Domain URL (လိုအပ်ပါက မိမိ API Domain ဖြင့် ပြောင်းပါ)
const API_BASE_URL = 'http://localhost:8080';

const getImageUrl = (item: any) => {
  // 1. packageImageUrl သို့မဟုတ် အခြား image key များကို စစ်ဆေးခြင်း
  const imagePath = item.packageImageUrl || item.imageUrl || item.image || item.photo;

  if (!imagePath) {
    return 'https://via.placeholder.com/500x250?text=No+Image';
  }

  // 2. Full URL (http/https) ပါပြီးသားဆိုလျှင် တိုက်ရိုက်ပြန်ပေးမည်
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath;
  }

  // 3. Relative Path ဖြစ်ပါက Domain URL နှင့် ဆက်ပေးမည်
  return `${API_BASE_URL}/${imagePath.replace(/^\//, '')}`;
};

const getPackageDetail = async (id: any) => {
  await call(packageApiResource.getById, null, { id });
  if (status.value === ApiStatus.SUCCESS) {
    packageModel.value = response.value?.data || response.value || {};
    if (!packageModel.value.packageDetails) {
      packageModel.value.packageDetails = [];
    }
  }
};

const handleBooking = async() => {

   try {
    appStore.setBookingData({
      destinationId: packageModel.value.destinationId,
      destinationName:
        packageModel.value.destinationName ,
      packageId: packageModel.value.id,
      busTypeName: packageModel.value.busTypeName,
      busName: packageModel.value.busName,
      hotelName: packageModel.value.hotelName,
      durationName: packageModel.value.durationName,
      transportFee: Number(packageModel.value.transportFee || 0),
      hotelFee: Number(packageModel.value.hotelFee || 0),
      serviceFee: Number(packageModel.value.serviceFee || 0),
      budgetAmount: Number(packageModel.value.budgetAmount || 0),
      departureDate: packageModel.value.departureDate,
     
    });

    await router.push({name: 'BookingSummary'})
  } catch (error) {
    alert(t('Something wrong.'));
    console.error(error);
  }

};

onMounted(() => {
  const targetId = route.params.id || route.query.id || route.params.packageId;
  if (targetId) {
    getPackageDetail(targetId);
  }
});
</script>

<style scoped>
.text-light-green-darken-3 {
  color: #689F38 !important;
}

.text-light-green-darken-4 {
  color: #558B2F !important;
}

ul {
  list-style-type: disc;
}
</style>