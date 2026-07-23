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
          <!-- Image Overlay -->
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

    <v-card class="pa-6 mt-6" elevation="2" rounded="md">
      <v-row>
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

        <v-col cols="12" sm="12" md="4" class="d-flex justify-md-end">
          <v-btn
            color="primary"
            width="160"
            height="44"
            elevation="2"
            class="rounded-lg text-capitalize font-weight-bold text-white"
            @click="scrollToBooking"
          >
            {{ t('Start Planning') }}
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>

  <!-- Popular Packages Section -->
  <v-container>
    <div class="d-flex justify-space-between align-center my-4">
      <h3 class="text-h6 font-weight-bold">{{ t('Popular Packages') }}</h3>
    
    </div>

    <!-- Loading State -->
    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular indeterminate color="#2C5E82"></v-progress-circular>
    </v-row>

    <!-- Packages List -->
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
                  :src="item.raw.packageImageUrl || 'https://via.placeholder.com/400'"
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

    <v-card ref="bookingSection" class="mt-5 position-relative overflow-hidden" elevation="2" rounded="md">
      <v-row no-gutters align="stretch">
        <v-col cols="12" md="8" class="pa-6">
          <div class="mb-4">
            <div class="text-black text-subtitle-1 d-flex align-center font-weight-bold">
              <v-icon
                :icon="mdiBookOpenOutline"
                size="small"
                class="mr-1"
                color="#06402B"
              ></v-icon>
              {{ t('Booking Information') }}
            </div>

            <div class="text-black text-subtitle-2 text-grey-darken-1 mt-1 ms-4">
              {{ t('Fill in your trip details and traveler information') }}
            </div>
          </div>

          <v-row class="mt-2">
            <!-- Left Form Column -->
            <v-col cols="12" sm="6">
              <destination-picker
                v-model:destination-id="packageModel.destinationId"
                v-model:destination-name="packageModel.destinationName"
                variant="outlined"
                density="comfortable"
                class="mb-3"
                :label="t('Choose Destination')"
              ></destination-picker>

              <package-picker
                v-model:package-id="bookingModel.packageId"
                v-model:package-name="bookingModel.packageName"
                v-model:destination-id="packageModel.destinationId"
                variant="outlined"
                density="comfortable"
                class="mb-3"
                @change="onPackagePickerChange"
                :label="t('Choose Package Name')"
              ></package-picker>

              <v-text-field
                v-model="contactForm.name"
                variant="outlined"
                density="comfortable"
                class="mb-3 bg-grey-lighten-3"
                readonly
                disabled
                placeholder="Not Logged In"
                hide-details="auto"
                :label="t('Customer Name')"
              ></v-text-field>

              <v-text-field
                v-model="contactForm.email"
                variant="outlined"
                density="comfortable"
                class="mb-3 bg-grey-lighten-3"
                readonly
                disabled
                placeholder="Not Logged In"
                hide-details="auto"
                :label="t('Customer Email')"
              ></v-text-field>

              <v-text-field
                v-model="contactForm.phone"
                variant="outlined"
                density="comfortable"
                class="mb-3 bg-grey-lighten-3"
                readonly
                disabled
                placeholder="Not Logged In"
                hide-details="auto"
                :label="t('Customer Phone No')"
              ></v-text-field>

              <v-textarea
                v-model="bookingModel.note"
                :label="t('Enter Note')"
                rows="8"
                density="comfortable"
                variant="outlined"
                class="mb-4"
              ></v-textarea>

              <v-btn
                color="#8EB872"
                width="150"
                height="44"
                elevation="1"
                class="rounded-lg text-capitalize font-weight-bold text-black"
                :loading="status == ApiStatus.LOADING"
                @click="handleBooking"
              >
                {{ t('Booking') }}
              </v-btn>
            </v-col>

            <!-- Right Readonly Form Column -->
            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formModel.busTypeName"
                name="busTypeName"
                :label="t('Bus Type Name')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.busName"
                name="busName"
                :label="t('Bus Name')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.destinationName"
                name="destinationName"
                :label="t('Destination Name')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.hotelName"
                name="hotelName"
                :label="t('Hotel Name')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.durationName"
                name="durationName"
                :label="t('Duration Name')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.transportFee"
                name="transportFee"
                :label="t('Transport Fee')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.hotelFee"
                name="hotelFee"
                :label="t('Hotel Fee')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.serviceFee"
                name="serviceFee"
                :label="t('Service Fee')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <!-- Used formatted computed property here -->
              <v-text-field
                :model-value="formattedDepartureDate"
                name="departureDate"
                :label="t('Departure Date')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                v-model="formModel.budgetAmount"
                name="budgetAmount"
                :label="t('Budget Amount')"
                readonly
                density="comfortable"
                variant="outlined"
                class="mb-3"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row>
            <v-sheet border rounded="lg" class="pa-2 bg-grey-lighten-5 mb-4 w-100">
              <v-row dense class="text-caption text-center" align="center" justify="space-around">
                <v-col cols="auto">
                  <v-icon :icon="mdiRibbon" size="x-small" color="primary"></v-icon> {{t('Best Price Guarantee')}}
                </v-col>
                <v-col cols="auto">
                  <v-icon :icon="mdiCheckCircle" size="x-small" color="primary"></v-icon>{{t('Easy Booking')}}
                </v-col>
                <v-col cols="auto">
                  <v-icon :icon="mdiCheckCircleOutline" size="x-small" color="primary"></v-icon> {{t('Secure Payment')}}
                </v-col>
                <v-col cols="auto">
                  <v-icon :icon="mdiHeadset" size="x-small" color="primary"></v-icon> {{t('24/7 Support')}}
                </v-col>
              </v-row>
            </v-sheet>
          </v-row>
        </v-col>

        <v-col cols="12" md="4" class="d-none d-md-block position-relative">
          <v-img
            src="../assets/images/op.jpg"
            alt="Travel Illustration"
            cover
            height="100%"
            width="100%"
          ></v-img>
        </v-col>
      </v-row>
    </v-card>
  </v-container>

  <v-container>
    <!-- Payment Methods Section -->
    <v-card class="mt-5 position-relative overflow-hidden pa-4" elevation="2" rounded="md">
      <div class="d-flex align-center mb-3">
        <v-icon :icon="mdiShieldCheckOutline" class="mr-2" color="primary"></v-icon>
        <span class="text-subtitle-1 font-weight-bold">{{t('Secure Payment')}}</span>
      </div>

      <template v-if="paymentList.length > 0">
        <v-data-iterator :items="paymentList" :items-per-page="12">
          <template v-slot:default="{ items }">
            <v-row dense>
              <v-col
                v-for="item in items"
                :key="item.raw.id"
                cols="4"
                sm="3"
                md="2"
              >
                <v-card
                  class="pa-2 d-flex flex-column align-center justify-center border text-center h-100"
                  elevation="0"
                  rounded="lg"
                  hover
                >
                  <v-img
                    :src="item.raw.imageUrl || 'https://via.placeholder.com/150'"
                    width="100"
                    height="100"
                    contain
                    class="mb-2"
                  ></v-img>

                  <div class="text-caption font-weight-medium text-truncate w-100">
                    {{ item.raw.name }}
                  </div>
                </v-card>
              </v-col>
            </v-row>

            <v-row>
              <v-sheet border rounded="lg" class="pa-2 bg-grey-lighten-5 w-100 mb-6">
                <v-row dense class="text-caption" align="center" justify="space-around">
                  <v-col cols="auto" class="d-flex align-center">
                    <v-icon :icon="mdiCheckCircle" color="green" class="mr-1"></v-icon>
                    <div>
                      <strong>{{t('100% Secure')}}</strong>
                      <div class="text-grey text-caption" style="font-size: 10px !important;">{{t('Your payment is safe')}}</div>
                    </div>
                  </v-col>
                  <v-col cols="auto" class="d-flex align-center">
                    <v-icon :icon="mdiContactlessPayment" color="amber" class="mr-1"></v-icon>
                    <div>
                      <strong>{{t('Fast Payment')}}</strong>
                      <div class="text-grey text-caption" style="font-size: 10px !important;">{{t('Instant confirmation')}}</div>
                    </div>
                  </v-col>
                  <v-col cols="auto" class="d-flex align-center">
                    <v-icon :icon="mdiTagMultiple" color="blue" class="mr-1"></v-icon>
                    <div>
                      <strong>{{t('Multiple Options')}}</strong>
                      <div class="text-grey text-caption" style="font-size: 10px !important;">{{t('Choose your way')}}</div>
                    </div>
                  </v-col>
                  <v-col cols="auto" class="d-flex align-center">
                    <v-icon :icon="mdiHeadset" color="teal" class="mr-1"></v-icon>
                    <div>
                      <strong>{{t('Trusted')}}</strong>
                      <div class="text-grey text-caption" style="font-size: 10px !important;">{{t('Trusted by thousands')}}</div>
                    </div>
                  </v-col>
                </v-row>
              </v-sheet>
            </v-row>
          </template>
        </v-data-iterator>
      </template>
    </v-card>

    <!-- Why Choose Us Section -->
    <v-card class="mt-5 position-relative overflow-hidden pa-4" elevation="2" rounded="md">
      <div class="mb-4">
        <div class="text-black text-subtitle-1 d-flex align-center font-weight-bold">
          {{ t('Why Choose Us?') }}
        </div>
      </div>
      <v-sheet border rounded="lg" class="pa-2 bg-grey-lighten-5 w-100 mb-6">
        <v-row dense class="text-caption" align="center" justify="space-around">
          <v-col cols="auto" class="d-flex align-center">
            <v-icon :icon="mdiMonitorEye" color="green" class="mr-1"></v-icon>
            <div>
              <strong>{{ t('Budget Friendly') }}</strong>
              <div class="text-grey text-caption" style="font-size: 10px !important;">
                {{ t('Best options within your budget') }}
              </div>
            </div>
          </v-col>
          <v-col cols="auto" class="d-flex align-center">
            <v-icon :icon="mdiSelectPlace" color="amber" class="mr-1"></v-icon>
            <div>
              <strong>{{ t('Handpicked Places') }}</strong>
              <div class="text-grey text-caption" style="font-size: 10px !important;">
                {{ t('Top destinations for you') }}
              </div>
            </div>
          </v-col>
          <v-col cols="auto" class="d-flex align-center">
            <v-icon :icon="mdiCardsPlayingDiamond" color="blue" class="mr-1"></v-icon>
            <div>
              <strong>{{ t('Customizable Trips') }}</strong>
              <div class="text-grey text-caption" style="font-size: 10px !important;">
                {{ t('Plan as you like') }}
              </div>
            </div>
          </v-col>
          <v-col cols="auto" class="d-flex align-center">
            <v-icon :icon="mdiHeadset" color="teal" class="mr-1"></v-icon>
            <div>
              <strong>{{ t('Trusted and Safe') }}</strong>
              <div class="text-grey text-caption" style="font-size: 10px !important;">
                {{ t('Your Safety is our priority') }}
              </div>
            </div>
          </v-col>
        </v-row>
      </v-sheet>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useAuthStore } from '../store/auth';
import { useAppStore } from '../store/app';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import useApi, { ApiStatus } from '../api';

import { Package, PackageListParams, PackageListParamsModel, PackageModel } from '../models/PackageModel';
import { packagePublicApiResource } from '../api/resources/packagePublicResource';
import DestinationPicker from '../components/destination/DestinationPicker.vue';
import PackagePicker from '../components/packaged/PackagePicker.vue';

import { 
  mdiBookOpenOutline, 
  mdiCardsPlayingDiamond, 
  mdiCheckCircle, 
  mdiCheckCircleOutline, 
  mdiContactlessPayment, 
  mdiHeadset, 
  mdiMonitorEye, 
  mdiRibbon, 
  mdiSelectPlace, 
  mdiShieldCheckOutline, 
  mdiTagMultiple 
} from '@mdi/js';

import { Booking, BookingModel } from '../models/BookingModel';
import { packageApiResource } from '../api/resources/packageResource';
import { PaymentMethod } from '../models/PaymentMethodModel';
import { paymentPublicApiResource } from '../api/resources/paymentPublicResource';

const contactForm = ref({ name: '', email: '', phone: '' });

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

const formattedDepartureDate = computed(() => {
  return formModel.value.departureDate 
    ? formModel.value.departureDate.replace('T', ' ').replace('Z', '').substring(0, 16) 
    : '';
});

const authStore = useAuthStore();
const appStore = useAppStore(); 

const { t } = useI18n();
const router = useRouter();

const packageList = ref<Package[]>([]);
const paymentList = ref<PaymentMethod[]>([]);

const goToDetails = () => {
  router.push({ name: 'HostelPublicList' });
};
const goToPackageDetail = (packageId?: number | string) => {
  if (!packageId) return; 

  router.push({
    name: 'PackagePublicList', 
    params: { id: packageId },
    query: { id: packageId }
  });
};

const isDirty = ref(false);
const bookingModel = ref<Booking>(BookingModel());
const packageModel = ref<Package>(PackageModel());

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
  formModel.value.departureDate = '';
  formModel.value.budgetAmount = 0;
};

const { call, response, status } = useApi();

const onApiCall = async (params: any = {}) => {
  try {
    await call(paymentPublicApiResource.getPaymentMethods, { params:{status: 'ACTIVE'} });
    if (status.value === ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      paymentList.value = resData?.list || resData?.data?.list || resData || [];
    }
  } catch (error) {
    console.error('Payment API Error:', error);
  }

  try {
    await call(packagePublicApiResource.getPackages, { params });
    if (status.value === ApiStatus.SUCCESS) {
      const resData = response.value?.data;
      packageList.value = resData?.list || resData?.data?.list || resData || [];
    }
  } catch (error) {
    console.error('API Error:', error);
  }
};

const searchParams = ref<PackageListParams>(PackageListParamsModel());

const handleSearch = () => {
  const activeParams = {
    destinationId: searchParams.value.destinationId || null,
  };
  onApiCall(activeParams);
};
const handleBooking = async () => {
  try {
    appStore.setBookingData({
      destinationId: packageModel.value.destinationId,
      destinationName:
        packageModel.value.destinationName || formModel.value.destinationName,
      packageId: bookingModel.value.packageId,
      busTypeName: formModel.value.busTypeName,
      busName: formModel.value.busName,
      hotelName: formModel.value.hotelName,
      durationName: formModel.value.durationName,
      transportFee: Number(formModel.value.transportFee || 0),
      hotelFee: Number(formModel.value.hotelFee || 0),
      serviceFee: Number(formModel.value.serviceFee || 0),
      budgetAmount: Number(formModel.value.budgetAmount || 0),
      departureDate: formModel.value.departureDate,
      note: bookingModel.value.note,
      customerName: contactForm.value.name,
      customerEmail: contactForm.value.email,
      customerPhone: contactForm.value.phone,
    });

    await router.push({ name: 'BookingSummary' });
  } catch (error) {
    alert(t('Something wrong.'));
    console.error(error);
  }
};

const bookingSection = ref<any>(null);

const scrollToBooking = () => {
  if (bookingSection.value) {
    const el = bookingSection.value.$el || bookingSection.value;
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
};

const handleClear = () => {
  searchParams.value = PackageListParamsModel();
  onApiCall();
};

const bannerSlides = ref([
  {
    image: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=1200',
    title: t('Find the perfect hotel for your stay'),
  },
  {
    image: 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=1200',
    title: t('Plan Your Dream Trip With Budget'),
  },
  {
    image: 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?q=80&w=1200',
    title: t('Comfortable & Affordable Stay for travellers'),
  },
]);

onMounted(async () => {
  onApiCall();
  
  const userId = authStore.user?.id;
  const username = authStore.user?.username;
  const userEmail = authStore.user?.email;
  const userPhone = authStore.user?.phone;
  
  if (userId) {
    contactForm.value.name = username || '';
    contactForm.value.email = userEmail || '';
    contactForm.value.phone = userPhone || '';
  }
});
</script>