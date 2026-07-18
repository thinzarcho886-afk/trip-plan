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
      <v-form v-model="formValid" ref="detailFormRef" @submit.prevent>
         <v-container fluid class="pa-0">
          <v-tabs v-model="tab" density="comfortable">
            <v-tab value="info">
                {{ t('Register Form') }}
            </v-tab>
             <v-tab value="packageDetail" @click="openDialog">
              {{ t('Add Place To Visits') }}
            </v-tab>
          </v-tabs>
           <v-window v-model="tab" :touch="false">
            <v-window-item value="info">
              <v-container fluid class="pa-0">
          <v-row>
            <v-col cols="12" md="6" sm="4">
              <v-text-field
                name="name"
                v-model="packageModel.name"
                :rules="[rules.required, rules.maxLength(100)]"
                :label="t('Package Name')"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
                      <v-col
                        cols="12"
                        sm="6"
                        class="py-1"
                      >
                        <busType-picker
                          v-model:bus-type-id="packageModel.busTypeId"
                          v-model:bus-type-name="packageModel.busName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Bus Type')"
                          
                        >
                        </busType-picker>
                      </v-col></v-row>
                      <v-row>
                       <v-col
                        cols="12"
                        sm="6"
                        class="py-1"
                      >
                        <bus-picker
                          v-model:bus-id="packageModel.transportId"
                          v-model:bus-name="packageModel.transportName"
                          :bus-type-id="packageModel.busTypeId"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Bus')"
                        >
                        </bus-picker>
                      </v-col></v-row>
                      <v-row>
                       <v-col cols="12" sm="6" class="py-1">
                        <destination-picker
                          v-model:destination-id="packageModel.destinationId"
                          v-model:destination-name="packageModel.destinationName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Destionation')"
                         
                        >
                        </destination-picker>
                      </v-col></v-row>
                      <v-row>
                       <v-col cols="12" sm="6" class="py-1">
                        <hotel-picker
                          v-model:hotel-id="packageModel.hotelId"
                          v-model:hotel-name="packageModel.hotelName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Hotel')"
                         
                        >
                        </hotel-picker>
                      </v-col></v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <duration-picker
                          v-model:duration-id="packageModel.durationId"
                          v-model:duration-name="packageModel.durationName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Duration')"
                         
                        >
                      </duration-picker></v-col></v-row>
                      <v-row>
                        <v-col cols="12" sm="6" class="py-1">
                          <v-text-field
                            name="departureDate"
                            v-model="departureDateInput" 
                            :label="t('Departure Date')"
                            type="date"
                            density="comfortable"
                            :rules="[rules.required]"
                          ></v-text-field>
                        </v-col>
                      </v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="transportFee"
                          v-model="packageModel.transportFee"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Transport Fee')"
                          type="number"
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col></v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="hotelFee"
                          v-model="packageModel.hotelFee"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Hotel Fee')"
                          type="number"
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col></v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="serviceFee"
                          v-model="packageModel.serviceFee"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Service Fee')"
                          type="number"
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col></v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="budgetAmount"
                          v-model="packageModel.budgetAmount"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Budget Amount')"
                         type="number"
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col></v-row>
                      <v-row>
                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="extraService"
                          v-model="packageModel.extraService"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Extra Service')"
                         type="number"
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col></v-row>
                       <v-row>
            <v-col cols="12" sm="6">
              <v-textarea
                name="description"
                v-model="packageModel.description"
                :rules="[rules.maxLength(300)]"
                :label="t('Description')"
                rows="5"
              ></v-textarea>
            </v-col></v-row>
             <v-row>
            <v-col cols="12" sm="6" class="py-1 d-flex align-center">
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
        <v-window-item value="packageDetail">
          <PackageDetailList :items="(packageModel.packageDetails || [])" />
        </v-window-item>
        </v-window>
        </v-container>
      </v-form>
       <!-- Add Place Dialog -->
      <v-dialog v-model="dialog" max-width="500">
        <v-card class="pa-4">
          <v-card-title>Add New Place</v-card-title>
          <v-card-text>
            <v-text-field v-model="tempItem.placeToVisit" label="Place Name"></v-text-field>
         <v-file-input 
        v-model="tempItem.imageFile" 
        label="Upload Photo" 
        prepend-icon="mdi-camera"
        variant="outlined"
        density="comfortable"
      ></v-file-input>
    </v-card-text>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="grey" @click="dialog = false">Cancel</v-btn>
      <v-btn color="primary" @click="savePlace">Save</v-btn>
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
import { mdiContentSave, mdiArrowLeft, mdiRefresh } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Package, PackageModel } from '../../models/PackageModel.js';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import { packageApiResource } from '../../api/resources/packageResource.js';
import BusTypePicker from '../../components/busType/BusTypePicker.vue';
import DurationPicker from '../../components/duration/DurationPicker.vue';
import DestinationPicker from '../../components/destination/DestinationPicker.vue';
import BusPicker from '../../components/bus/BusPicker.vue';
import PackageDetailList from '../../components/packaged/PackageDetailList.vue';
import HotelPicker from '../../components/hotel/HotelPicker.vue';
const tab = ref<string | null>(null);
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
const dialog = ref(true);
const tempItem = ref({ 
  placeToVisit: '', 
  id: undefined, 
  imageUrl: '', 
  imageFile:null,
  imageFullUrl: '' 
});
const departureDateInput = computed({
  get() {
    // API ကလာတဲ့ ISO String ကို 'YYYY-MM-DD' format ပြောင်းပေးခြင်း
    if (!packageModel.value.departureDate) return '';
    return new Date(packageModel.value.departureDate).toISOString().split('T')[0];
  },
  set(val: string) {
    // User ရွေးလိုက်တဲ့ရက်ကို 'YYYY-MM-DDT00:00:00Z' အဖြစ်ပြောင်းပြီး သိမ်းခြင်း
    if (val) {
      packageModel.value.departureDate = new Date(val).toISOString();
    }
  }
});

const getDetail = async (id: any) => {
  await call(packageApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    packageModel.value = response.value?.data as Package;
    initialData.value = JSON.parse(JSON.stringify(packageModel.value));
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;


  let apiUrl =packageApiResource.create;
  if (packageModel.value.id) apiUrl = packageApiResource.update;

  await call(apiUrl, { data: packageModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    router.push({ name: routeNames.packageList });
  }
};
const openDialog = () => {
    dialog.value = true;
  } ;

const savePlace = () => {
  if (!packageModel.value.packageDetails) {
    packageModel.value.packageDetails = [];
  }
  
  // Data အသစ်ကို တွန်းထည့်မယ်
  // ဒီနေရာမှာ tempItem ထဲက data တွေအကုန် ပါသွားပါမယ်
  packageModel.value.packageDetails.push({ ...tempItem.value });

  // Data အသစ်ထည့်ပြီးတာနဲ့ UI ပြန်လည်ပတ်အောင် 
  packageModel.value.packageDetails = [...packageModel.value.packageDetails];

  // Reset လုပ်ပါ
  tempItem.value = { 
    placeToVisit: '', 
    imageFile: null, 
    id: undefined, 
    imageUrl: '', 
    imageFullUrl: '' 
  };
  
  dialog.value = false;
};
// const onSave = async () => {
//   const { valid } = await detailFormRef.value.validate();
//   if (!valid) return;

//   let apiUrl = packageModel.value.id ? packageApiResource.update : packageApiResource.create;
//   await call(apiUrl, { data: packageModel.value });

//   if (status.value == ApiStatus.SUCCESS) {
//     if (!packageModel.value.id) {
//        packageModel.value.id = response.value.data.id;
//        tab.value = 'packageDetail'; 
//     }
//   }
// };

const addPlace=()=>{
if (!packageModel.value.packageDetails) {
  packageModel.value.packageDetails = [];
}
packageModel.value.packageDetails.push({
  placeToVisit: '',
  imageUrl: '',
  imageFullUrl: '',
  id:undefined,
});
};

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => router.push({ name: routeNames.packageList }),
    color: '',
  },
 
  {
    icon: mdiContentSave,
    label: t('Save'),
    onClick: onSave,
    color: 'primary',
    useLoading: true,
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

