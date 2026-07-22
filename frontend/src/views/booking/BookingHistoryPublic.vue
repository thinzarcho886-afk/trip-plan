<template>
    
  <v-container class="my-6">
    <!-- Header Banner -->
    <v-card class="mb-6 elevation-2 text-center py-6" color="teal-lighten-5">
      <h2 class="text-h4 font-weight-bold text-teal-darken-3">Booking History List</h2>
    </v-card>

    <!-- Data Table -->
    <v-card class="elevation-1">
      <v-table density="compact" class="text-no-wrap">
        <thead>
          <tr>
            <th class="text-center">Payment Image</th>
            <th class="text-left">Customer Name</th>
            <th class="text-left">Package Name</th>
            <th class="text-left">Bus Types</th>
            <th class="text-left">Bus</th>
            <th class="text-left">Destination</th>
            <th class="text-left">Duration</th>
            <th class="text-left">Hotel</th>
            <th class="text-left">Departure Date</th>
            <th class="text-left">Transport Fee</th>
            <th class="text-left">Hotel Fee</th>
            <th class="text-left">Service Fee</th>
            <th class="text-left">Qty</th>
            <th class="text-left">Budget Amount</th>
            <th class="text-left">Payment Method Name</th>
            <th class="text-left">Note</th>


            <th class="text-left">Status</th>
            <th class="text-left">Update Date</th>
            <th class="text-left">Update By</th>
            
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td colspan="15" class="text-center py-4">Loading history...</td>
          </tr>
          <tr v-else-if="bookingList.length === 0">
            <td colspan="15" class="text-center py-4">No booking history found.</td>
          </tr>
          <tr v-for="item in bookingList" :key="item.id">
            <td >
              <v-img
              :src="item.paymentReceiveImageUrl || 'https://via.placeholder.com/40'"
              cover
            ></v-img>
            </td>
             <td>{{ item.customerName || '-' }}</td>
            <td>{{ item.packageName || '-' }}</td>
            <td>{{ item.busTypeName || '-' }}</td>
            <td>{{ item.busName || '-' }}</td>
            <td>{{ item.destinationName || '-' }}</td>
            <td>{{ item.durationName || '-' }}</td>
            <td>{{ item.hotelName || '-' }}</td>
            <td>{{ formatDate(item.departureDate) }}</td>
            <td>{{ item.transportFee || 0 }} MMK</td>
            <td>{{ item.hotelFee || 0 }} MMK</td>
            <td>{{ item.serviceFee || 0 }} MMK</td>
            <td>{{ item.travelersQty || 0 }}</td>
            <td>{{ item.budgetAmount * item.travelersQty }} MMK</td>

            <td class="font-weight-bold">{{ item.paymentMethodName }}</td>
             <td>{{ item.note || '' }}</td>
            <td>
              <v-chip :color="getStatusColor(item.status)" size="small">
                {{ item.status }}
              </v-chip>
            </td>
            <td>{{ formatDate(item.updatedDateInMilliSeconds) }}</td>
            <td>{{ item.updatedBy }}</td>
            
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAuthStore } from '../../store/auth';
import useApi, { ApiStatus } from '../../api';
import { bookingApiResource } from '../../api/resources/bookingResource';

const authStore = useAuthStore();
const { call, response, status } = useApi();

const bookingList = ref<any[]>([]);
const loading = ref(false);

const fetchUserBookingHistory = async () => {
  const customerId = authStore.user?.customerId;
  if (!customerId) return;

  loading.value = true;
  try {
    await call(bookingApiResource.getByCustomerId,null,{ customerId:customerId } );


    if (status.value === ApiStatus.SUCCESS) {
      const res = response.value?.data;
      bookingList.value = res?.list || res?.data || res || [];
    }
  } catch (error) {
    console.error('Fetch Booking Error:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchUserBookingHistory();
});

// Helper Functions
const formatDate = (dateStr: string) => {
  if (!dateStr) return '-';
  return new Date(dateStr).toLocaleDateString();
};

const getStatusColor = (statusStr: string) => {
  switch (statusStr?.toUpperCase()) {
   
    case 'CONFIRM':
      return 'green';
    case 'PENDING':
      return 'blue';
    case 'CANCEL':
      return 'red';
    default:
      return 'grey';
  }
};

const downloadPayment = (url: string) => {
  window.open(url, '_blank');
};
</script>