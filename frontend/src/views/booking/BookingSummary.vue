<template>
  <v-container class="my-6">
    <!-- Top Banner Section -->
    <v-card elevation="2" class="mb-6 rounded-lg overflow-hidden">
      <v-img
        src="https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=1200"
        height="180"
        cover
      >
        <div class="d-flex align-center fill-height pa-6 bg-black-transparent">
          <div>
            <h1 class="text-h4 font-weight-bold text-white">Book Your Trip</h1>
          </div>
        </div>
      </v-img>
    </v-card>
<v-form ref="formRef">
    <v-row>
      <v-col cols="12" md="7">
        <v-card class="pa-6" elevation="2" rounded="lg">
          <div class="d-flex align-center mb-4">
            <v-icon :icon="mdiBookOpenOutline" color="#06402B" class="mr-2"></v-icon>
            <div>
              <h2 class="text-h6 font-weight-bold">Booking Information</h2>
              <div class="text-caption text-grey-darken-1">Fill in your trip details and traveler information</div>
            </div>
          </div>

          <v-row dense class="mt-2">
            <!-- Destination Picker -->
            <v-col cols="12" sm="4">
              <destination-picker
                v-model:destination-id="bookingData.destinationId"
                v-model:destination-name="bookingData.destinationName"
                variant="outlined"
                density="comfortable"
                label="Destination"
              ></destination-picker>
            </v-col>

            <!-- Package Picker -->
            <v-col cols="12" sm="4">
              <package-picker
                v-model:package-id="bookingData.packageId"
                v-model:package-name="bookingData.packageName"
                v-model:destination-id="bookingData.destinationId"
                variant="outlined"
                density="comfortable"
                label="Package Name"
                @change="onPackagePickerChange"
              ></package-picker>
            </v-col>

            <v-col cols="12" sm="4">
              <v-text-field
                :model-value="formattedDepartureDate"
                label="Departure Date"
                variant="outlined"
                density="comfortable"
                readonly
              ></v-text-field>
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="bookingData.durationName"
                label="Duration"
                variant="outlined"
                density="comfortable"
                readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-text-field
                v-model.number="travellerQty"
                label="Travellers Qty"
                type="number"
                min="1"
                variant="outlined"
                density="comfortable"
              ></v-text-field>
            </v-col>

            <v-col cols="12" sm="4">
              <v-text-field
                v-model="bookingData.hotelName"
                label="Hotels"
                variant="outlined"
                density="comfortable"
                readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="4">
              <v-text-field
                v-model="bookingData.busTypeName"
                label="Bus Types"
                variant="outlined"
                density="comfortable"
                readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="4">
              <v-text-field
                v-model="bookingData.busName"
                label="Bus"
                variant="outlined"
                density="comfortable"
                readonly
              ></v-text-field>
            </v-col>

            <v-col cols="12" sm="6">
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
              
            </v-col>
            <v-col cols="12" sm="6">
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
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="contactForm.phone"
                variant="outlined"
                density="comfortable"
                class="mb-3 bg-grey-lighten-3"
                readonly
                disabled
                placeholder="Not Logged In"
                hide-details="auto"
                :label="t('Customer Phone')"
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-textarea
                v-model="bookingData.note"
                label="Note"
                rows="2"
                variant="outlined"
                :rules="[rules.required]"
                density="comfortable"
                
              ></v-textarea>
            </v-col>
          </v-row>

          <div class="d-flex justify-space-between mt-6">
            <v-btn
              variant="outlined"
              color="grey-darken-2"
              class="text-capitalize px-6"
              @click="router.back()"
            >
              <v-icon :icon="mdiArrowLeft" class="mr-1"></v-icon>
              Go Back
            </v-btn>

            <v-btn
              color="primary"
              class="text-capitalize px-6 text-white"
              :loading="status === ApiStatus.LOADING"
              @click="confirmBooking"
            >
              Confirm Booking
              <v-icon :icon="mdiArrowRight" class="ml-1"></v-icon>
            </v-btn>
          </div>
        </v-card>
      </v-col>
  

      <!-- ညာဘက်ခြမ်း: Trip Summary Card & Price Breakdown -->
      <v-col cols="12" md="5">
        <v-card class="pa-5" elevation="2" rounded="lg">
          <div class="d-flex align-center text-teal-darken-3 font-weight-bold mb-3">
            <v-icon :icon="mdiMapMarker" class="mr-1"></v-icon>
            Trip Summary
          </div>
          <v-img
              :src="bookingData.imageUrl || 'https://via.placeholder.com/40'"
              height="140"
              cover
               class="rounded-md mb-3"
            ></v-img>
          

          <h3 class="text-h6 font-weight-bold text-teal-darken-4">
            {{ bookingData.destinationName || 'Destination Name' }}
          </h3>
          <p class="text-caption text-grey-darken-1 mb-4">Nature • Waterfalls • Cool weather</p>

          <v-divider class="mb-4"></v-divider>

          <!-- Trip Info Details List -->
          <div class="text-body-2 text-grey-darken-3 space-y-2">
            <div class="d-flex align-center justify-space-between py-1">
              <span><v-icon :icon="mdiCalendar" size="small" class="mr-2" color="teal"></v-icon>Departure Date</span>
              <span class="font-weight-medium">{{ formattedDepartureDate || '-' }}</span>
            </div>
            <div class="d-flex align-center justify-space-between py-1">
              <span><v-icon :icon="mdiAccountGroup" size="small" class="mr-2" color="teal"></v-icon>Travellers Qty</span>
              <span class="font-weight-medium">{{ travellerQty }} people</span>
            </div>
            <div class="d-flex align-center justify-space-between py-1">
              <span><v-icon :icon="mdiBus" size="small" class="mr-2" color="teal"></v-icon>Transport</span>
              <span class="font-weight-medium">{{ bookingData.busTypeName || '-' }} / {{ bookingData.busName || '-' }}</span>
            </div>
            <div class="d-flex align-center justify-space-between py-1">
              <span><v-icon :icon="mdiBed" size="small" class="mr-2" color="teal"></v-icon>Hotel</span>
              <span class="font-weight-medium">{{ bookingData.hotelName || '-' }}</span>
            </div>
            <div class="d-flex align-center justify-space-between py-1">
              <span><v-icon :icon="mdiClockOutline" size="small" class="mr-2" color="teal"></v-icon>Duration</span>
              <span class="font-weight-medium">{{ bookingData.durationName || '-' }}</span>
            </div>
          </div>

          <v-divider class="my-4"></v-divider>

          <!-- Price Breakdown Table -->
          <div class="text-subtitle-2 font-weight-bold text-teal-darken-4 mb-2">
            Price Breakdown
          </div>

          <v-table density="compact" class="text-caption">
            <thead>
              <tr>
                <th class="text-left pl-0">Fee Name</th>
                <th class="text-center">Qty</th>
                <th class="text-right">Amount</th>
                <th class="text-right pr-0">Total Amount</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="pl-0">Transport Fee</td>
                <td class="text-center">{{ travellerQty }}</td>
                <td class="text-right">{{ formatCurrency(bookingData.transportFee) }}</td>
                <td class="text-right pr-0">{{ formatCurrency((bookingData.transportFee || 0) * travellerQty) }}</td>
              </tr>
              <tr>
                <td class="pl-0">Hotel Fee</td>
                <td class="text-center">{{ travellerQty }}</td>
                <td class="text-right">{{ formatCurrency(bookingData.hotelFee) }}</td>
                <td class="text-right pr-0">{{ formatCurrency((bookingData.hotelFee || 0) * travellerQty) }}</td>
              </tr>
              <tr>
                <td class="pl-0">Service Fee</td>
                <td class="text-center">{{ travellerQty }}</td>
                <td class="text-right">{{ formatCurrency(bookingData.serviceFee) }}</td>
                <td class="text-right pr-0">{{ formatCurrency((bookingData.serviceFee || 0) * travellerQty) }}</td>
              </tr>
            </tbody>
          </v-table>

          <v-divider class="my-3"></v-divider>

          <div class="d-flex justify-space-between align-center font-weight-bold text-subtitle-1">
            <span>Total Amount</span>
            <span class="text-teal-darken-3 text-h6 font-weight-bold">
              {{ formatCurrency(totalCalculatedAmount) }} MMK
            </span>
          </div>
        </v-card>
      </v-col>
    </v-row>
</v-form>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '../../store/app';
import { useAuthStore } from '../../store/auth';
import DestinationPicker from '../../components/destination/DestinationPicker.vue';
import PackagePicker from '../../components/packaged/PackagePicker.vue';
import useApi, { ApiStatus } from '../../api';
import { packageApiResource } from '../../api/resources/packageResource';
import { required } from '../../utils/validations.js';


import {
  mdiBookOpenOutline,
  mdiArrowLeft,
  mdiArrowRight,
  mdiMapMarker,
  mdiCalendar,
  mdiAccountGroup,
  mdiBus,
  mdiBed,
  mdiClockOutline,
} from '@mdi/js';


const contactForm = ref({ name: '', email: '', phone: '' });


const router = useRouter();
const appStore = useAppStore();
const authStore = useAuthStore();
const { call, response, status } = useApi();



const rules = {
  required,
 
};

const travellerQty = ref(1);

const bookingData = ref<any>({
  destinationId: null,
  destinationName: '',
  packageId: null,
  packageName: '',
  departureDate: '',
  durationName: '',
  hotelName: '',
  busTypeName: '',
  busName: '',
  customerName: '',
  customerPhone: '',
  customerEmail: '',
  note: '',
  transportFee: 0,
  hotelFee: 0,
  serviceFee: 0,
});
import { useI18n } from 'vue-i18n';
import { packagePublicApiResource } from '../../api/resources/packagePublicResource';
const formRef = ref<any>(null);

const { t } = useI18n();
onMounted(async () => {
  if (appStore.bookingData && Object.keys(appStore.bookingData).length > 0) {
    bookingData.value = { ...bookingData.value, ...appStore.bookingData };
    
    if (bookingData.value.packageId) {
      await fetchPackageDetails(bookingData.value.packageId);
    }
  } else {
    if (authStore.user) {
      bookingData.value.customerName = authStore.user.username || authStore.user.name || '';
      bookingData.value.customerEmail = authStore.user.email || '';
      bookingData.value.customerPhone = authStore.user.phone || '';
    }
  }
});

const formattedDepartureDate = computed(() => {
  return bookingData.value.departureDate 
    ? bookingData.value.departureDate.replace('T', ' ').replace('Z', '').substring(0, 16) 
    : '';
});

const totalCalculatedAmount = computed(() => {
  const transport = (bookingData.value.transportFee || 0) * travellerQty.value;
  const hotel = (bookingData.value.hotelFee || 0) * travellerQty.value;
  const service = (bookingData.value.serviceFee || 0) * travellerQty.value;
  return transport + hotel + service;
});

const onPackagePickerChange = async (selectedPackage: any) => {
  const pkgId = selectedPackage?.id || bookingData.value.packageId;

  if (!pkgId) {
    clearPackageDetails();
    return;
  }

  await fetchPackageDetails(pkgId);
};

const fetchPackageDetails = async (id: number | string) => {
  await call(packageApiResource.getById, null, { id });

  if (status.value === ApiStatus.SUCCESS && response.value?.data) {
    const pkg = response.value.data;
    bookingData.value.imageUrl= pkg.packageImageUrl;
    bookingData.value.busTypeName = pkg.busTypeName || '';
    bookingData.value.busName = pkg.busName || '';
    bookingData.value.destinationName = pkg.destinationName || '';
    bookingData.value.hotelName = pkg.hotelName || '';
    bookingData.value.durationName = pkg.durationName || '';
    
    bookingData.value.transportFee = pkg.transportFee || 0;
    bookingData.value.hotelFee = pkg.hotelFee || 0;
    bookingData.value.serviceFee = pkg.serviceFee || 0;
    bookingData.value.departureDate = pkg.departureDate || '';
  }
};

const clearPackageDetails = () => {
  bookingData.value.busTypeName = '';
  bookingData.value.busName = '';
  bookingData.value.hotelName = '';
  bookingData.value.durationName = '';
  bookingData.value.transportFee = 0;
  bookingData.value.hotelFee = 0;
  bookingData.value.serviceFee = 0;
  bookingData.value.departureDate = '';
};

const formatCurrency = (val: number) => {
  return (val || 0).toLocaleString();
};

const confirmBooking = async() => {
 const { valid } = await formRef.value.validate();

  if (!valid) {
    alert(t('Please fill the note.'));
    return;
  }

  if (!bookingData.value.packageId ) {
    alert(t('Please choose one package'));
    return;
  }

  appStore.setBookingData({
    ...bookingData.value,
    travellerQty: travellerQty.value,
    totalAmount: totalCalculatedAmount.value,
  });

  alert('Booking Confirmed Successfully!');
  await router.push({name: 'PaymentView'});
};

onMounted(() => {

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

<style scoped>
.bg-black-transparent {
  background: rgba(0, 0, 0, 0.45);
}
</style>