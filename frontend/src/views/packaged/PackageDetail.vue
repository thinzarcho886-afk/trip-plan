<template>
  <Detail
    v-bind="{
      title: t('Package'),
      loading: status == ApiStatus.LOADING,
      error: status == ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error.message,
      formValid,
      breadcrumbs,
      actions,
    }"
  >
    <template #form>
      <v-form ref="detailFormRef" v-model="formValid" @submit.prevent="() => {}">
        <v-container fluid class="pa-0">
          <!-- 🌟 Bus Types အတိုင်း Tab ပြောင်းလဲခြင်း -->
          <v-tabs v-model="tab" density="comfortable" color="primary">
              <v-tab value="info">
                <v-badge :model-value="formValid === false" dot floating color="error">
                  {{ t('Register Form') }}
                </v-badge>
              </v-tab>
              
              <!-- 🛠️ @click="openDialog" ကို ပြန်ထည့်ပေးလိုက်ပါတယ် -->
              <v-tab value="packageDetail" :disabled="!packageModel.id" @click="openDialog">
                {{ t('Add Place To Visits') }}
              </v-tab>
            </v-tabs>

          <v-window v-model="tab" :touch="false" class="mt-4">
            <!-- Tab 1: Info (Form ဖြည့်သည့်နေရာ) -->
            <v-window-item value="info">
              <v-container fluid class="pa-0">
                <v-row>
                 <v-col cols="12" md="4" class="d-flex justify-center align-start">
              <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed;">
                <ImageInput
                  :image-url="packageModel.packageImageUrl"
                  v-model="packageModel.packageImage"
                  @delete="packageModel.packageImageUrl = ''; packageModel.packageImage = '';"                  image-height="180px"
                  image-width="100%"
                  width="100%"
                  class="mx-auto"
                  :label="t('Image')"
                ></ImageInput>
              </v-card>
            </v-col>
            </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1 mt-4">
                    <v-text-field
                      name="name"
                      v-model="packageModel.name"
                      :rules="[rules.required, rules.maxLength(100)]"
                      :label="t('Package Name')"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>
                
                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <busType-picker
                      v-model:bus-type-id="packageModel.busTypeId"
                      v-model:bus-type-name="packageModel.busTypeName"
                      :params="{ status: Status.ACTIVE }"
                      :label="t('Bus Type')"
                      density="comfortable"
                      variant="outlined"
                    ></busType-picker>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <bus-transport-picker
                      v-model:bus-id="packageModel.transportId"
                      v-model:bus-name="packageModel.busName"
                      :bus-type-id="(packageModel.busTypeId as any)"
                      :params="{ status: Status.ACTIVE }"
                      :label="t('Bus')"
                      density="comfortable"
                      variant="outlined"
                    ></bus-transport-picker>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <destination-picker
                      v-model:destination-id="packageModel.destinationId"
                      v-model:destination-name="packageModel.destinationName"
                      :params="{ status: Status.ACTIVE }"
                      :label="t('Destination')"
                      density="comfortable"
                      variant="outlined"
                    ></destination-picker>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <hotel-picker
                      v-model:hotel-id="packageModel.hotelId"
                      v-model:hotel-name="packageModel.hotelName"
                      :params="{ status: Status.ACTIVE }"
                      :label="t('Hotel')"
                      density="comfortable"
                      variant="outlined"
                    ></hotel-picker>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <duration-picker
                      v-model:duration-id="packageModel.durationId"
                      v-model:duration-name="packageModel.durationName"
                      :params="{ status: Status.ACTIVE }"
                      :label="t('Duration')"
                      density="comfortable"
                      variant="outlined"
                    ></duration-picker>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="departureDate"
                      v-model="departureDateInput" 
                      :label="t('Departure Date')"
                      type="date"
                      density="comfortable"
                      variant="outlined"
                      :rules="[rules.required]"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="transportFee"
                      v-model="packageModel.transportFee"
                      :rules="[rules.required]"
                      :label="t('Transport Fee')"
                      type="number"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="hotelFee"
                      v-model="packageModel.hotelFee"
                      :rules="[rules.required]"
                      :label="t('Hotel Fee')"
                      type="number"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="serviceFee"
                      v-model="packageModel.serviceFee"
                      :rules="[rules.required]"
                      :label="t('Service Fee')"
                      type="number"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="budgetAmount"
                      v-model="packageModel.budgetAmount"
                      :rules="[rules.required]"
                      :label="t('Budget Amount')"
                      type="number"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="extraService"
                      v-model="packageModel.extraService"
                      :rules="[rules.required]"
                      :label="t('Extra Service')"
                      type="number"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-textarea
                      name="description"
                      v-model="packageModel.description"
                      :label="t('Description')"
                      rows="4"
                      variant="outlined"
                    ></v-textarea>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1 d-flex align-center">
                    <v-switch
                      name="status"
                      :label="packageModel.status === 'ACTIVE' ? t('ACTIVE') : t('INACTIVE')"
                      v-model="packageModel.status"
                      :true-value="Status.ACTIVE"
                      :false-value="Status.INACTIVE"
                      color="green"
                      hide-details
                    ></v-switch>
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <!-- Tab 2: Package Detail (Place List အပိုင်း) -->
            <v-window-item value="packageDetail">
              <v-container fluid class="pa-0">
                <v-row>
                  <v-col cols="12">
                   <PackageDetailList 
                    v-model:items="packageModel.packageDetails" 
                    :package-id="packageModel.id"
                    :title="t('Add Place To Visits')"
                    @open-dialog="openDialog"
                    @update:delete="onPlaceDelete"
                  />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>
          </v-window>
        </v-container>
      </v-form>

      <!-- Add Place Dialog -->
      <v-dialog v-model="dialog" max-width="500">
        <v-card class="pa-4">
          <v-card-title>Add New Place</v-card-title>
          <v-card-text>
            <v-text-field v-model="tempItem.placeToVisit" label="Place Name" variant="outlined"></v-text-field>
            
            <ImageInput
              :image-url="tempItem.imageUrl"
              v-model="tempItem.imageUrl"
              @delete="tempItem.imageUrl = ''; tempItem.image = '';"
              image-height="180px"
              image-width="100%"
              width="100%"
              class="mx-auto"
              :label="t('Image')"
            ></ImageInput>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="grey" variant="text" @click="dialog = false">Cancel</v-btn>
            <v-btn color="primary" variant="flat" @click="savePlace">Confirm</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import { required, maxLength } from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { useRoute, useRouter } from 'vue-router';
import { Status } from '../../constants/Status.js';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Package, PackageModel } from '../../models/PackageModel.js';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import { packageApiResource } from '../../api/resources/packageResource.js';
import BusTypePicker from '../../components/busType/BusTypePicker.vue';
import DurationPicker from '../../components/duration/DurationPicker.vue';
import DestinationPicker from '../../components/destination/DestinationPicker.vue';
import BusTransportPicker from '../../components/bus/BusTransportPicker.vue';
import PackageDetailList from '../../components/packaged/PackageDetailList.vue';
import HotelPicker from '../../components/hotel/HotelPicker.vue';
import ImageInput from '../../components/common/ImageInput.vue';

// 🌟 Default အဖြစ် 'info' tab ကိုပေးထားပါတယ်
const tab = ref<string | null>('info');
const { t } = useI18n({ useScope: 'global' });
const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const initialData = ref<Package | null>(null);
const packageModel = ref<Package>(PackageModel());

const rules = { required, maxLength };
const { call, response, error, status } = useApi();
const authStore = useAuthStore();
const dialog = ref(false);

const tempItem = ref({ 
  placeToVisit: '', 
  id: undefined, 
  image: '', 
  imageFile: null,
  imageUrl: '' 
});

// ဖြတ်လိုက်တဲ့ Place id တွေကို သိမ်းထားမယ့် array
const deletePlaceIds = ref<any[]>([]);

// Component ကနေ delete လုပ်လိုက်တဲ့ id list ကို လှမ်းယူမယ့် function
const onPlaceDelete = (event: any) => {
  deletePlaceIds.value = event;
};

const departureDateInput = computed({
  get() {
    if (!packageModel.value.departureDate) return '';
    return new Date(packageModel.value.departureDate).toISOString().split('T')[0];
  },
  set(val: string) {
    if (val) {
      packageModel.value.departureDate = new Date(val).toISOString();
    }
  }
});

const getDetail = async (id: any) => {
  await call(packageApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    packageModel.value = response.value?.data as Package;
    if (!packageModel.value.packageDetails) {
      packageModel.value.packageDetails = [];
    }
    initialData.value = JSON.parse(JSON.stringify(packageModel.value));
  }
};

const savePlace = () => {
  if (!packageModel.value.packageDetails) {
    packageModel.value.packageDetails = [];
  }
  
  if (tempItem.value.image && !tempItem.value.imageUrl) {
    tempItem.value.imageUrl = tempItem.value.image; 
  }
  
  const newItem = {
    ...tempItem.value,
    id: tempItem.value.id || null
  };
  
  packageModel.value.packageDetails.push(newItem as any);
  packageModel.value.packageDetails = [...packageModel.value.packageDetails];
  
  dialog.value = false;
};
const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  const isUpdate = !!packageModel.value.id; 
  let apiUrl = isUpdate ? packageApiResource.update : packageApiResource.create;
 const payload = {
    ...packageModel.value,
    deletePackageDetailIds: deletePlaceIds.value 
  };

  await call(apiUrl, { data: payload }, { params: { isUpdate } });

  if (status.value == ApiStatus.SUCCESS) { 
    router.push({ name: routeNames.packageList });
  }
};

const openDialog = () => {
  tempItem.value = { 
    placeToVisit: '', 
    imageFile: null, 
    id: undefined, 
    image: '', 
    imageUrl: '' 
  };
  dialog.value = true;
};

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => router.push({ name: routeNames.packageList }),
    color: '',
    useLoading: false,
    useDisabled: false,
  },
  {
    icon: mdiContentSave,
    label: t('Save'),
    onClick: onSave,
    color: 'primary',
    useLoading: true,
    useDisabled: true,
  },
]);

const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Package'), to: { name: routeNames.packageList } },
  { title: t('Detail') },
]);

onMounted(() => {
  let { id } = route.params;
  if (id != 'new') getDetail(id);
  if (!packageModel.value.packageDetails) {
    packageModel.value.packageDetails = [];
  }
});
</script>