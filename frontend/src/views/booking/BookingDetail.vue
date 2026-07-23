 <template>
  <Detail
    v-bind="{
      title: t('Booking'),
      loading: status === ApiStatus.LOADING,
      error: status === ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error?.message,
      formValid: formValid,
      breadcrumbs,
      actions,
    }"
  >
  
    <template #form>
      <v-form
        v-model="formValid"
        ref="detailFormRef"
        @submit.prevent="onSave"
        class="pa-6"
      >
        <v-container fluid class="pa-0">
  <v-row>
    <!-- ဘယ်ဘက်ခြမ်း - Image Card -->
    <v-col cols="12" md="4" class="d-flex justify-center align-start">
      <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed;">
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
      </v-card>
    </v-col>

    <!-- ညာဘက်ခြမ်း - Form Fields -->
    <v-col cols="12" md="8">
      <v-row>
        <!-- Row 1 -->
        <v-col cols="12" sm="6" class="py-1">
          <package-picker
            name="name"
            v-model:package-id="bookingModel.packageId"
            v-model:package-name="bookingModel.packageName"
            :rules="[rules.required]"
            :label="t('Package Name')"
            @change="onPackagePickerChange"
            density="comfortable"
            variant="outlined"
          ></package-picker>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.busTypeName"
            name="busTypeName"
            :label="t('Bus Type Name')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <!-- Row 2 -->
        <v-col cols="12" sm="6" class="py-1">
          <customer-picker
            v-model:id="bookingModel.customerId"
            v-model:name="bookingModel.customerName"
            :rules="[rules.required]"
            :label="t('Customer Name')"
            density="comfortable"
            variant="outlined"
          ></customer-picker>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.busName"
            name="busName"
            :label="t('Bus Name')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <!-- Row 3 -->
        <v-col cols="12" sm="6" class="py-1">
          <payment-picker
            v-model:payment-method-id="bookingModel.paymentMethodId"
            v-model:payment-method-name="bookingModel.paymentMethodName"
            :rules="[rules.required]"
            :label="t('Payment Method')"
            density="comfortable"
            variant="outlined"
          ></payment-picker>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.destinationName"
            name="destination"
            :label="t('Destination')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <!-- Row 4 -->
        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            name="travelersQty"
            v-model="bookingModel.travelersQty"
            :rules="[rules.required, rules.numberOnly]"                  
            :label="t('Travelers Qty')"
            density="comfortable"
            variant="outlined"
            @input="calculateTotalAmount"
          ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.hotelName"
            name="hotel"
            :label="t('Hotel')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <!-- Row 5 -->
        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            name="totalAmount"
            v-model="bookingModel.totalAmount"
            :readonly="true"
            :label="t('Total Amount')"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.durationName"
            name="duration"
            :label="t('Duration')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>
        
        <!-- Row 6 -->
        <v-col cols="12" sm="6" class="py-1">
          <v-select
            v-model="bookingModel.status"
            :items="[{ title: t('PENDING'), value: 'PENDING' },
      { title: t('CONFIRM'), value: 'CONFIRM' },
      { title: t('CANCEL'), value: 'CANCEL' }]"
            :label="t('Status')"
            density="comfortable"
            variant="outlined"
            :disabled="isStatusDisabled"
            @update:model-value="isDirty = true"
          ></v-select>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formattedDepartureDate"
            name="departureDate"
            :label="t('Departure Date')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <v-col cols="12" class="py-1">
          <v-textarea
            name="note"
            v-model="bookingModel.note"
            :rules="[rules.required, rules.maxLength(255)]"
            :label="t('Note')"
            density="comfortable"
            variant="outlined"
            rows="3"
          ></v-textarea>
        </v-col>

        <!-- 🌟 Fees & Financial Section ကို ၂ ကော်လံ ညီညာစွာ ခွဲထုတ်လိုက်ခြင်း (အကွက်လွတ်တွေ မလိုတော့ပါ) -->
        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.transportFee"
            name="transportFee"
            :label="t('Transport Fee')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.hotelFee"
            name="hotelFee"
            :label="t('Hotel Fee')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.serviceFee"
            name="serviceFee"
            :label="t('Service Fee')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>

        <v-col cols="12" sm="6" class="py-1">
          <v-text-field
            v-model="formModel.budgetAmount"
            name="budgetAmount"
            :label="t('Budget Amount')"
            :readonly="true"
            density="comfortable"
            variant="outlined"
          ></v-text-field>
        </v-col>
        
      </v-row>
    </v-col>
  </v-row>
</v-container>
      </v-form>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, onMounted,computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import Detail from '../../layouts/default/Detail.vue';
import ImageInput from '../../components/common/ImageInput.vue';
import { routeNames } from '../../router/routes.js';
import { required, maxLength, email, password,phone } from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { customerApiResource } from '../../api/resources/customerResource.js';
import { Status } from '../../constants/Status.js';
import { Role } from '../../constants/Role.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { PaymentMethod, PaymentMethodModel } from '../../models/PaymentMethodModel.js';
import { useAuthStore } from '../../store/auth.js';
import { paymentMethodApiResource } from '../../api/resources/paymentMethodResource.js';
import { Booking, BookingModel } from '../../models/BookingModel.js';
import { bookingApiResource } from '../../api/resources/bookingResource.js';
import PackagePicker from '../../components/packaged/PackagePicker.vue';
import CustomerPicker from '../../components/customer/CustomerPicker.vue';
import PaymentPicker from '../../components/payment/PaymentPicker.vue';
import { packageApiResource } from '../../api/resources/packageResource.js';
const isDirty = ref(false);
const formModel = ref<any>({
  busTypeName: '',
  busName: '',
  destinationName: '',
  hotelName: '',
  durationName: '',
  transportFee: 0,
  hotelFee: 0,
  serviceFee: 0,
  budgetAmount: 0 ,
  departureDate:'',
});



const formattedDepartureDate = computed(() => {
  return formModel.value.departureDate ? formModel.value.departureDate.replace('T', ' ').replace('Z', '').substring(0, 16) : '';
});
const route = useRoute();
const router = useRouter();
const { t } = useI18n();

const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const bookingModel = ref<Booking>(BookingModel());
const isUpdate = ref(false);
const authStore=useAuthStore();

const calculateTotalAmount = () => {
  const qty = Number(bookingModel.value.travelersQty) || 0;
  const budget = Number(formModel.value.budgetAmount) || 0;
  bookingModel.value.totalAmount = budget * qty;
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

     
      calculateTotalAmount();
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
  formModel.value.departureDate = '';
  formModel.value.totalAmount = 0;
};
let initialSavedData = ''; // Reset အတွက် သိမ်းဆည်းမည့် string data

const isStatusDisabled = computed(() => {
  if (!isUpdate.value) {
    return true; 
  }

  const isAlreadyConfirmed = initialSavedData ? JSON.parse(initialSavedData).status === 'CONFIRM' : false;
  if (isAlreadyConfirmed) {
    return true;
  }

  const currentUserRole = authStore.user?.role;
  const isAdmin = currentUserRole === 'SYSADMIN' || currentUserRole === 'ADMIN';
  
  if (!isAdmin) {
    return true; 
  }

  return false; 
});











const rules = {
  required,
  maxLength,
  email,
  password,
   numberOnly: (value: string) => {
    const pattern = /^[0-9]+$/;
    return pattern.test(value) || 'ဂဏန်းသီးသန့်သာ ရိုက်ထည့်ပေးပါ';
  }
};
const roles = ref([
  { id: 1, name: 'Admin' },
  { id: 2, name: 'Customer' },
 
]);
const usernameFieldState = computed(() => {
  const isAdmin = authStore.user.role === 'SYSADMIN';
  if (isAdmin) {
    return { readonly: false, disabled: false };
  }
  if (isUpdate.value) {
    return { readonly: true, disabled: false };
  }
  return { readonly: false, disabled: false };
});
const { call, response, error, status } = useApi();
status.value = ApiStatus.IDLE;

const getDetail = async (id: any) => {
  await call(bookingApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    bookingModel.value = response.value?.data as Booking;
    initialSavedData = JSON.stringify(bookingModel.value);



    if (bookingModel.value.packageId) {
      await call(packageApiResource.getById, null, { id: bookingModel.value.packageId });
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
          
          formModel.value.budgetAmount = packageData.budgetAmount || 0; 
          formModel.value.departureDate = packageData.departureDate || '';

          calculateTotalAmount();
        }
      }
      status.value = ApiStatus.SUCCESS;
    }

  }
};

const onSave = async () => {
  if (authStore.userRole != Role.SYSADMIN) {
    bookingModel.value.packageName = authStore.user.name;
  }

  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = bookingApiResource.register;
  if (bookingModel.value.id) apiUrl = bookingApiResource.update;

  await call(apiUrl, { data: bookingModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    // back?
    router.go(-1);
  }
};


// Custom breadcrumbs
const breadcrumbs = ref([
  { title: t('General') },
  { title: t('Booking', 2), to: { name: routeNames.bookingList } },
  { title: t('Detail') },
]);
 const actions: ActionButton[] = [
  {
    icon: mdiArrowLeft,
    label: 'Back',
    onClick: () => {
      router.go(-1);
    },
    color: '',
    useLoading: false,
    useDisabled: false,
  },
  {
    icon: mdiContentSave,
    label: 'Save',
    onClick: onSave,
    color: 'green',
    useLoading: true,
    useDisabled: true,
  },
];

onMounted(() => {
  let { id } = route.params;
  if (id != 'new') {
    isUpdate.value = true;
    getDetail(id);
  }
 else {
    bookingModel.value.status = 'PENDING';
    initialSavedData = JSON.stringify(bookingModel.value);
  }

});

//:readonly="authStore.user.status"

</script>
