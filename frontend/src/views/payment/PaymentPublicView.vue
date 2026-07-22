<template>
  <v-container class="my-6">
    <v-card class="pa-6" elevation="2" rounded="lg">
      <div 
  class="d-flex align-center cursor-pointer text-grey-darken-2 hover-underline mb-7" 
  @click="router.back()"
>
  <v-icon :icon="mdiArrowLeft" size="small" class="mr-1"></v-icon>
  <span>Go Back</span>
</div>
      <div class="text-h5 font-weight-bold mb-1">Payment Form</div>
      
      <div class="text-subtitle-2 text-teal-darken-3 mb-6">
        Please select a payment method and complete your booking.
      </div>

      <v-row>
        <!-- ဘယ်ဘက်ခြမ်း: Payment Steps -->
        <v-col cols="12" md="7">
          <!-- 1. Select Payment Method -->
          <v-card variant="outlined" class="pa-4 mb-4 rounded-lg">
            <div v-if="paymentList.length > 0" class="text-subtitle-2 font-weight-bold text-teal-darken-3 mb-3">
              1. Select Payment Method
            </div>

            <!-- Loading State -->
            <v-progress-linear v-if="status === ApiStatus.LOADING" indeterminate color="teal"></v-progress-linear>

            <v-item-group v-else v-model="selectedPaymentIndex" mandatory>
              <v-row dense>
                <v-col
                  v-for="method in paymentList"
                  :key="method.id"
                  cols="6"
                  sm="4"
                  md="3"
                >
                  <v-item v-slot="{ isSelected, toggle }">
                    <v-card
                      :color="isSelected ? 'primary' : 'grey-lighten-4'"
                      class="d-flex flex-column align-center justify-center pa-3 text-center cursor-pointer h-100"
                      variant="outlined"
                      @click="toggle"
                    >
                      <v-img
                        :src="method.imageUrl || 'https://via.placeholder.com/150'"
                        height="40"
                        width="40"
                        contain
                        class="mb-2"
                      ></v-img>
                      
                      <div class="text-caption font-weight-bold">{{ method.name }}</div>
                      
                      <div class="text-caption text-grey" style="font-size: 10px !important;">
                        {{ t('Pay With') }} {{ method.name }}
                      </div>
                    </v-card>
                  </v-item>
                </v-col>
              </v-row>
            </v-item-group>
          </v-card>

          <v-card variant="outlined" class="pa-4 mb-4 rounded-lg">
            <div class="text-subtitle-2 font-weight-bold text-teal-darken-3 mb-2">
              Transfer To
            </div>
            <div class="text-caption text-grey-darken-1 mb-3">Please Transfer To the following account</div>
            <v-row dense>
              <v-col cols="12" sm="6">
                <v-text-field
                  label="Account Name"
                  :model-value="activePaymentMethod?.accountName"
                  variant="outlined"
                  density="compact"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  label="Account Number"
                  :model-value="activePaymentMethod?.accountNumber"
                  variant="outlined"
                  density="compact"
                  readonly
                ></v-text-field>
              </v-col>
            </v-row>
          </v-card>

          <!-- 3. Sender Information -->
          <v-card variant="outlined" class="pa-4 mb-4 rounded-lg">
            <div class="text-subtitle-2 font-weight-bold text-teal-darken-3 mb-2">
              Sender Information
            </div>
            <div class="text-caption text-grey-darken-1 mb-3">Enter your payment information</div>
            <v-row dense>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="contactForm.name"
                  label="Sender Name"
                  variant="outlined"
                  density="compact"
                  readonly
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                   v-model="contactForm.phone"
                  label="Sender Phone Number"
                  variant="outlined"
                  density="compact"
                  readonly
                ></v-text-field>
              </v-col>
            </v-row>
          </v-card>

          <!-- 4. Upload Payment Screenshot -->
          <v-card variant="outlined" class="pa-4 rounded-lg text-center">
            <div class="text-subtitle-2 font-weight-bold text-teal-darken-3 mb-1">
              Upload Payment Screenshot
            </div>
            <div class="text-caption text-grey-darken-1 mb-4">Upload your payment proof screenshot</div>

        <ImageInput
          :image-url="bookingModel.paymentReceiveImageUrl"
          v-model="bookingModel.paymentReceiveImageUrl"
          @delete="bookingModel.paymentReceiveImageUrl = '';"    
          image-height="180px"
          image-width="100%"
          width="100%"
          class="mx-auto"
          :label="t('Payment Receive Image')"
        ></ImageInput>

            <div class="text-caption text-grey mt-2 mb-4">
              * Please make sure the screenshot is clear and shows the transaction details.
            </div>

            <v-btn color="primary" block size="large" @click="submitPayment">
              <v-icon left class="mr-1">mdi-check-circle</v-icon>
              Confirm Payment
            </v-btn>
            <div class="text-caption text-grey-darken-1 mt-2">
              🔒 Your payment information is secure and encrypted.
            </div>
          </v-card>
        </v-col>

        <!-- ညာဘက်ခြမ်း: Trip Summary -->
        <v-col cols="12" md="5">
          <v-card class="pa-4" variant="outlined" rounded="lg">
            <div class="text-subtitle-1 font-weight-bold text-teal-darken-3 mb-3">
              Trip Summary
            </div>

            <v-img
              :src="bookingData.imageUrl || 'https://via.placeholder.com/40'"
              height="120"
              cover
              class="rounded mb-3"
            ></v-img>

            <div class="text-h6 font-weight-bold">{{ bookingData.destinationName || 'Pyin Oo Lwin' }}</div>
            <div class="text-caption text-grey-darken-1 mb-3">Nature • Waterfalls • Cool weather</div>

            <v-divider class="mb-3"></v-divider>

            <div class="text-body-2 space-y-2">
              <div class="d-flex justify-space-between py-1">
                <span>Departure Date</span>
                <span>{{ bookingData.departureDate || '27 May 2026' }}</span>
              </div>
              <div class="d-flex justify-space-between py-1">
                <span>Travelers Qty</span>
                <span>{{ bookingData.travellerQty || 2 }} people</span>
              </div>
              <div class="d-flex justify-space-between py-1">
                <span>Transport</span>
                <span>{{ bookingData.busName || 'Standard Bus' }}</span>
              </div>
              <div class="d-flex justify-space-between py-1">
                <span>Hotel</span>
                <span>{{ bookingData.hotelName || 'Winter Fall Hotel' }}</span>
              </div>
              <div class="d-flex justify-space-between py-1">
                <span>Duration</span>
                <span>{{ bookingData.durationName || '2days 3nights' }}</span>
              </div>
            </div>

            <v-divider class="my-3"></v-divider>

            <div class="text-subtitle-2 font-weight-bold text-teal-darken-3 mb-2">Price Breakdown</div>
            <div class="text-caption space-y-1">
              <div class="d-flex justify-space-between">
                <span>Transport Fee</span>
                <span>{{ bookingData.transportFee || 0 }} MMK</span>
              </div>
              <div class="d-flex justify-space-between">
                <span>Hotel Fee</span>
                <span>{{ bookingData.hotelFee || 0 }} MMK</span>
              </div>
              <div class="d-flex justify-space-between">
                <span>Service Fee</span>
                <span>{{ bookingData.serviceFee || 0 }} MMK</span>
              </div>
            </div>

            <v-divider class="my-3"></v-divider>

            <div class="d-flex justify-space-between align-center text-subtitle-1 font-weight-bold">
              <span>Total Amount</span>
              <span class="text-teal-darken-3 text-h6">{{ bookingData.totalAmount || 0 }} MMK</span>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script setup lang="ts">

import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';

import { useAppStore } from '../../store/app';
import { useAuthStore } from '../../store/auth';
import useApi, { ApiStatus } from '../../api';
import ImageInput from '../../components/common/ImageInput.vue';


import { PaymentMethod } from '../../models/PaymentMethodModel';
import { paymentPublicApiResource } from '../../api/resources/paymentPublicResource';
import { bookingApiResource } from '../../api/resources/bookingResource';
import { Booking, BookingModel } from '../../models/BookingModel';
import { mdiArrowLeft } from '@mdi/js';

const router = useRouter();
const { t } = useI18n();
const authStore = useAuthStore();
const appStore = useAppStore();
const bookingModel = ref<Booking>(BookingModel());


const { call, response, status } = useApi();

const contactForm = ref({ name: '', email: '', phone: '' });
const bookingData = computed(() => appStore.bookingData || {});

const paymentList = ref<PaymentMethod[]>([]);
const selectedPaymentIndex = ref<number>(0);


const activePaymentMethod = computed(() => {
  if (paymentList.value.length > 0 && selectedPaymentIndex.value !== undefined) {
    return paymentList.value[selectedPaymentIndex.value];
  }
  return null;
});

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
};

onMounted(() => {
  onApiCall();

  // Auth User Information Binding
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

// Submit Payment Logic
const submitPayment = async () => {
  if (!bookingModel.value.paymentReceiveImageUrl) {
    alert(t('Please upload the payment receipt screenshot.'));
    return;
  }

  bookingModel.value.packageId = bookingData.value.packageId || null;
  bookingModel.value.customerId = authStore.user?.customerId || null;
  bookingModel.value.paymentMethodId = activePaymentMethod.value?.id || null;
  bookingModel.value.travelersQty = Number(bookingData.value.travellerQty || 1);
  bookingModel.value.note=bookingData.value.note;
  bookingModel.value.status = 'PENDING';
  bookingModel.value.departureDate = bookingData.value.departureDate;
  bookingModel.value.note = bookingData.value.note;
  bookingModel.value.budgetAmount = bookingData.value.totalAmount;

  try {
    let apiUrl = bookingApiResource.registerPublic;
   

    await call(apiUrl, { data: bookingModel.value });

    if (status.value === ApiStatus.SUCCESS) {
      alert(t('Booking created successfully'));
      router.push({name: 'BookingHistoryPublic'});
    }
  } catch (error) {
    alert(t('Sorry, Booking cannot be sent.'))
    console.error(t('Booking Error:'), error);
  }
};
</script>