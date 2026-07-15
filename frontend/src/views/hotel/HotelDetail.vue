<template>
  <Detail
    v-bind="{
      title: t('Hotel'),
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
            <!-- Left Side: Image Upload Block -->
            <v-col cols="12" md="4" class="d-flex justify-center align-start">
              <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed; background-color: #f8f9fa;">
                <ImageInput
                  :image-url="hotelModel.imageUrl"
                  v-model="hotelModel.imageUrl"
                  @delete="hotelModel.imageUrl = null"
                  image-height="180px"
                  image-width="100%"
                  width="100%"
                  class="mx-auto"
                  :label="t('Hotel Image')"
                  :disabled="isUpdate && authStore.user.role !== Role.SYSADMIN"
                ></ImageInput>
              </v-card>
            </v-col>

            <!-- Right Side: Hotel Input Fields -->
            <v-col cols="12" md="8">
              <v-row dense>
                
                <!-- Hotel Name -->
                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="name"
                    v-model="hotelModel.name"
                    :label="t('Hotel Name') + ' *'"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                  ></v-text-field>
                </v-col>

                <!-- Active Destination Dropdown -->
                <v-col cols="12" sm="6" class="py-1">
                  <v-select
                    v-model="hotelModel.destinationId"
                    :items="activeDestinationList"
                    item-title="name"
                    item-value="id"
                    :label="t('Destination') + ' *'"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                    :loading="destinationApi.status.value === ApiStatus.LOADING"
                    :no-data-text="t('No data available')"
                  ></v-select>
                </v-col>

                <!-- Price Per Night -->
                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="pricePerNight"
                    v-model.number="hotelModel.pricePerNight"
                    type="number"
                    :label="t('Price per Night') + ' *'"
                    :rules="[rules.required, rules.positiveNumber]"
                    variant="outlined"
                    density="comfortable"
                  ></v-text-field>
                </v-col>

                <!-- Status Select -->
                <v-col cols="12" sm="6" class="py-1 d-flex align-center">
                  <v-switch
                    name="status"
                    :label="hotelModel.status === 'ACTIVE' ? t('ACTIVE') : t('INACTIVE')"
                    v-model="hotelModel.status"
                    :true-value="Status.ACTIVE"
                    :false-value="Status.INACTIVE"
                    color="green"
                    hide-details
                  ></v-switch>
                </v-col>

                <!-- Address Box -->
                <v-col cols="12" class="py-1">
                  <v-textarea
                    name="address"
                    v-model="hotelModel.address"
                    :label="t('Address') + ' *'"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                    rows="2"
                  ></v-textarea>
                </v-col>

                <!-- Description Box -->
                <v-col cols="12" class="py-1">
                  <v-textarea
                    name="description"
                    v-model="hotelModel.description"
                    :label="t('Description') + ' *'"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                    rows="3"
                  ></v-textarea>
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
import { ref, onMounted, computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import { required } from '../../utils/validations';
import useApi, { ApiStatus } from '../../api/index.js';
import { useRoute, useRouter } from 'vue-router';
import { mdiContentSave, mdiArrowLeft, mdiUndo } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ImageInput from '../../components/common/ImageInput.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth';
import { hotelApiResource } from '../../api/resources/hotelResource.js';
import { destinationApiResource } from '../../api/resources/destinationResource.js';
import { Hotel, HotelModel } from '../../models/HotelModel.js';
import { Role } from '../../constants/Role.js';
import { Status } from '../../constants/Status.js';

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const isUpdate = ref(false);

const hotelModel = ref<any>(HotelModel());
const roles = ref([
  { id: 1, name: 'Admin' },
  { id: 2, name: 'Customer' },
 
]);
const originalModelString = ref('');
const activeDestinationList = ref([]);
const { t } = useI18n();
const authStore = useAuthStore();

const { call, response, error, status } = useApi();
const destinationApi = useApi();

status.value = ApiStatus.IDLE; 
const isEditMode = ref(false);

const rules = {
  required,
  positiveNumber: (v: number) => v > 0 || t('Price must be greater than 0'),
};

const getDetail = async (id: any) => {
  await call(hotelApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    hotelModel.value = response.value?.data as Hotel;
  }
};

const loadActiveDestinations = async () => {
  await destinationApi.call(destinationApiResource.getByStatus, null, { status: 'ACTIVE' });
  if (destinationApi.status.value === ApiStatus.SUCCESS) {
    activeDestinationList.value = destinationApi.response.value?.data || [];
  } else {
    activeDestinationList.value = [];
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value?.validate() || { valid: false };
  if (!valid) return;

  let apiUrl = hotelApiResource.save;
  if (isEditMode.value) apiUrl = hotelApiResource.update;
  hotelModel.value.name = (hotelModel.value as any).name;
  hotelModel.value.destinationId = (hotelModel.value as any).destinationId;
  hotelModel.value.address = (hotelModel.value as any).address;
  hotelModel.value.imageUrl = (hotelModel.value as any).imageUrl;
  hotelModel.value.pricePerNight = (hotelModel.value as any).pricePerNight;
  hotelModel.value.description = (hotelModel.value as any).description;
  hotelModel.value.status = (hotelModel.value as any).status;

  const payload = { ...hotelModel.value };

  await call(apiUrl, { data: payload });

  if (status.value == ApiStatus.SUCCESS) {
    router.go(-1);
  }
};

const handleReset = () => {
  if (isEditMode.value) {
    hotelModel.value = JSON.parse(originalModelString.value);
  } else {
    detailFormRef.value?.reset();
    hotelModel.value.status = 'ACTIVE';
    hotelModel.value.imageUrl = null;
  }
};

const breadcrumbs = ref([
  { title: t('General') },
  { title: t('Hotel', 2), to: { name: routeNames.hotelList } },
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
    icon: mdiUndo,
    label: 'Reset',
    onClick: handleReset,
    color: 'warning',
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

onMounted(async () => {
  await loadActiveDestinations();
  let { id } = route.params;
  if (id !== 'new') {
    isEditMode.value = true;
    await getDetail(id);
  } else {
    originalModelString.value = JSON.stringify(hotelModel.value);
  }
});
</script>