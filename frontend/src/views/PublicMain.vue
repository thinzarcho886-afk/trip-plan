<template>
  <v-container class="mt-5">
    <!-- Carousel / Slide Show -->
    <v-card elevation="6" rounded="lg" class="overflow-hidden">
      <v-carousel
        cycle
        height="260"
        hide-delimiter-background
        show-arrows="hover"
        interval="4000"
      >
        <v-carousel-item
          v-for="(slide, i) in bannerSlides"
          :key="i"
          :src="slide.image"
          cover
        >
          <div
            class="d-flex align-center justify-center fill-height"
            style="background: rgba(0, 0, 0, 0.4);"
          >
            <h2 class="text-h4 font-weight-bold text-white text-center px-4">
              {{ t(slide.title) }}
            </h2>
          </div>
        </v-carousel-item>
      </v-carousel>
    </v-card>

    <!-- Search Section -->
    <v-card class="pa-6 mt-6" elevation="2" rounded="md">
      <v-row align="center">
        <v-col cols="12" sm="6" md="4">
          <destination-picker
            v-model:destination-id="searchParams.destinationId"
            v-model:destination-name="searchParams.destinationName"
            variant="outlined"
            density="comfortable"
            :label="t('Choose Destination Name')"
          >
          </destination-picker>
        </v-col>

        <v-col cols="12" sm="6" md="4" class="d-flex align-center">
          <v-btn
            variant="outlined"
            color="grey-darken-2"
            width="110"
            height="44"
            class="rounded-lg text-capitalize font-weight-bold mr-2"
            @click="handleClear"
          >
            {{ t('Clear') }}
          </v-btn>

          <v-btn
            color="primary"
            width="150"
            height="44"
            elevation="2"
            class="rounded-lg text-capitalize font-weight-bold text-white"
            :loading="status == ApiStatus.LOADING"
            @click="handleSearch"
          >
            {{ t('Search') }}
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>

  <!-- Popular Packages Section -->
  <v-container>
    <div class="d-flex justify-space-between align-center my-4">
      <h3 class="text-h6 font-weight-bold">{{ t('Popular Packages') }}</h3>
      <v-btn
        variant="flat"
        color="#06402B"
        rounded="lg"
        class="text-white"
        @click="goToViewAll"
      >
        {{ t('View All') }}
      </v-btn>
    </div>

    <!-- Loading State -->
    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="#2C5E82"></v-progress-circular>
    </v-row>

    <!-- Packages Grid -->
    <template v-else-if="packageList.length > 0">
      <v-data-iterator :items="packageList" :items-per-page="6">
        <template v-slot:default="{ items }">
          <v-row>
            <v-col
              v-for="item in items"
              :key="item.raw.id"
              cols="12"
              sm="6"
              md="4"
              lg="2.4"
            >
              <v-card class="rounded-lg border elevation-1 h-100 d-flex flex-column" flat>
                <v-img
                  :src="
                    item.raw.packageImageUrl || item.raw.packageImage || 'https://via.placeholder.com/400'
                  "
                  height="180"
                  cover
                ></v-img>
                
                <v-card-item>
                  <div
                    class="text-subtitle-1 font-weight-bold mb-2 text-truncate"
                    style="color: #2C5E82; text-align: center;"
                  >
                    {{ item.raw.name }}
                  </div>
                </v-card-item>

                <v-spacer></v-spacer>

                <!-- 🌟 View Details Button Section -->
                <div class="pa-4 pt-0">
                  <v-btn
                    variant="flat"
                    color="#82B139"
                    rounded="lg"
                    block
                    class="text-white font-weight-bold text-capitalize"
                    @click="goToPackageDetail(item.raw.id)"
                  >
                    {{ t('View Details') }}
                  </v-btn>
                </div>
              </v-card>
            </v-col>
          </v-row>
        </template>
      </v-data-iterator>
    </template>

    <!-- Empty State -->
    <v-row v-else class="justify-center py-10">
      <div class="text-grey text-subtitle-1">
        {{ t('No package data found') }}
      </div>
    </v-row>

    <!-- Booking Information Form Section -->
    <v-card class="pa-6 mt-8" elevation="2" rounded="md">
      <v-row class="flex-column mb-2">
        <div class="text-black text-subtitle-1 d-flex align-center font-weight-bold">
          <v-icon
            :icon="mdiBookOpenOutline"
            size="small"
            class="mr-1"
            color="#06402B"
          ></v-icon>  
          {{ t('Booking Information') }}
        </div>

        <div class="text-subtitle-2 text-grey-darken-1 ms-5">
          {{ t('Fill in your trip details and traveler information') }}
        </div>
      </v-row>

      <v-row>
        <v-col cols="12" sm="6" md="4">
          <destination-picker
            v-model:destination-id="packageModel.destinationId"
            v-model:destination-name="packageModel.destinationName"
            variant="outlined"
            density="comfortable"
            class="mb-3"
            :label="t('Choose Destination')"
          >
          </destination-picker>
          
          <package-picker
            v-model:package-id="bookingModel.packageId"
            v-model:package-name="bookingModel.packageName"
            v-model:destination-id="packageModel.destinationId"
            variant="outlined"
            density="comfortable"
            :label="t('Choose Package Name')"
            @change="onPackagePickerChange"
          >
          </package-picker>
        </v-col>

        <v-col cols="12" sm="6" md="4">
          <v-text-field
            v-model="formModel.busTypeName"
            name="busTypeName"
            :label="t('Bus Type Name')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
            class="mb-3"
          ></v-text-field>

          <v-text-field
            v-model="formModel.busName"
            name="busName"
            :label="t('Bus Name')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>
      </v-row>

      <v-row dense justify="start" class="mt-4">
        <v-col cols="12" sm="6" md="4">
          <v-btn
            variant="outlined"
            color="grey-darken-2"
            width="110"
            height="44"
            class="rounded-lg text-capitalize font-weight-bold mr-2"
            @click="handleClear"
          >
            {{ t('Clear') }}
          </v-btn>

          <v-btn
            color="primary"
            width="150"
            height="44"
            elevation="2"
            class="rounded-lg text-capitalize font-weight-bold text-white"
            :loading="status == ApiStatus.LOADING"
            @click="handleSearch"
          >
            {{ t('Search') }}
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import useApi, { ApiStatus } from '../api';
import { Package, PackageListParams, PackageListParamsModel, PackageModel } from '../models/PackageModel';
import { packagePublicApiResource } from '../api/resources/packagePublicResource';
import DestinationPicker from '../components/destination/DestinationPicker.vue';
import PackagePicker from '../components/packaged/PackagePicker.vue';
import { mdiBookOpenOutline } from '@mdi/js';
import { Booking, BookingModel } from '../models/BookingModel';
import { packageApiResource } from '../api/resources/packageResource';

const { t } = useI18n();
const router = useRouter();
const { call, response, status } = useApi();

const packageList = ref<Package[]>([]);
const searchParams = ref<PackageListParams>(PackageListParamsModel());
const isDirty = ref(false);

const bookingModel = ref<Booking>(BookingModel());
const packageModel = ref<Package>(PackageModel());

const formModel = ref<any>({
  busTypeName: '',
  busName: '',
  destinationName: '',
  hotelName: '',
  durationName: '',
  transportFee: 0,
  hotelFee: 0,
  serviceFee: 0,
  budgetAmount: 0,
  departureDate: '',
});

// 🌟 View Details နှိပ်ပါက Package Details Page သို့ ID ဖြင့် သွားရန် Function
// Function definition ကို အောက်ပါအတိုင်း ပြင်ပါ
const goToPackageDetail = (packageId?: number | string) => {
  if (!packageId) return; // ID မရှိပါက ထွက်မည်

  router.push({
    name: 'PackagePublicList', // သင့် Router ၏ Route Name အတိုင်း ပြင်ပါ
    params: { id: packageId },
    query: { id: packageId }
  });
};
const goToViewAll = () => {
  router.push({
    name: 'PackagePublicList', // Package အားလုံးကြည့်ရန် Route Name
  });
};

const onPackagePickerChange = async (selectedPackage: any) => {
  isDirty.value = true;

  if (!selectedPackage || !selectedPackage.id) {
    clearPackageDetails();
    return;
  }

  await call(packageApiResource.getById, null, { id: selectedPackage.id });

  if (status.value === ApiStatus.SUCCESS) {
    const packageData = response.value?.data;
    
    if (packageData) {
      formModel.value.busTypeName = packageData.busTypeName || '';
      formModel.value.busName = packageData.busName || '';
      formModel.value.destinationName = packageData.destinationName || '';
      formModel.value.hotelName = packageData.hotelName || '';
      formModel.value.durationName = packageData.durationName || '';
      formModel.value.transportFee = packageData.transportFee || 0;
      formModel.value.hotelFee = packageData.hotelFee || 0;
      formModel.value.serviceFee = packageData.serviceFee || 0;
      formModel.value.departureDate = packageData.departureDate || '';
      formModel.value.budgetAmount = packageData.budgetAmount || 0; 
    }
  }
};

const clearPackageDetails = () => {
  formModel.value.busTypeName = '';
  formModel.value.busName = '';
  formModel.value.destinationName = '';
  formModel.value.hotelName = '';
  formModel.value.durationName = '';
  formModel.value.transportFee = 0;
  formModel.value.hotelFee = 0;
  formModel.value.serviceFee = 0;
  formModel.value.budgetAmount = 0;
};

const onApiCall = async (params: any = {}) => {
  try {
    await call(packagePublicApiResource.getPackages, { params });
    if (status.value == ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      packageList.value = resData?.list || resData?.data?.list || resData || [];
    }
  } catch (error) {
    console.error('API Error:', error);
  }
};

const handleSearch = () => {
  const activeParams = {
    destinationId: searchParams.value.destinationId || null,
  };
  onApiCall(activeParams);
};

const handleClear = () => {
  searchParams.value = PackageListParamsModel();
  onApiCall();
};

const bannerSlides = ref([
  {
    image: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=1200',
    title: 'Find the perfect hotel for your stay',
  },
  {
    image: 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=1200',
    title: 'Plan Your Dream Trip With Budget',
  },
  {
    image: 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?q=80&w=1200',
    title: 'Comfortable & Affordable Stay for travellers',
  },
]);

onMounted(() => {
  onApiCall();
});
</script>