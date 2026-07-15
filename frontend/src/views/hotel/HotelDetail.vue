<template>
  <Detail
    v-bind="{
      title: isEditMode ? t('Hotel Edit Form') : t('Hotel Register Form'),
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
        @submit.prevent="() => {}"
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
                  accept="image/png, image/jpeg, image/jpg"
                ></ImageInput>
              </v-card>
            </v-col>

            <!-- Right Side: Hotel Input Fields -->
            <v-col cols="12" md="8">
              <v-row dense>
                
                <!-- Hotel Name (Required & Async Duplicate Validation Rule) -->
                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="hotelName"
                    v-model="hotelModel.hotelName"
                    :label="t('Hotel Name') + ' *'"
                    :rules="[rules.required, rules.duplicateCheck]"
                    variant="outlined"
                    density="comfortable"
                  ></v-text-field>
                </v-col>

                <!-- Active Destination Selection List Dropdown -->
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
                  ></v-select>
                </v-col>

                <!-- Price Per Night Input Field -->
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

                <!-- Status View Segment (Visible on creation/modification context) -->
                <v-col cols="12" sm="6" class="py-1">
                  <v-select
                    v-model="hotelModel.status"
                    :items="[
                              { title: t('ACTIVE'), value: 'ACTIVE' },
                              { title: t('INACTIVE'), value: 'INACTIVE' }
                            ]"
                    :label="t('Status') + ' *'"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                    :disabled="!isEditMode"
                  ></v-select>
                </v-col>

                <!-- Address Input Box -->
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

                <!-- Description Input Box -->
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

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const hotelModel = ref<any>({
  id: null,
  hotelName: '',
  destinationId: null,
  address: '',
  imageUrl: null,
  pricePerNight: null,
  description: '',
  status: 'ACTIVE' 
});

const originalModelString = ref('');
const activeDestinationList = ref([]);
const { t } = useI18n();
const authStore = useAuthStore();
const { call, response, error, status } = useApi();

const isEditMode = computed(() => route.params.id !== 'new');

const rules = {
  required,
  positiveNumber: (v: number) => v > 0 || t('Price must be greater than 0'),
  duplicateCheck: async (value: string) => {
    if (!value) return true;
    if (isEditMode.value && value === JSON.parse(originalModelString.value).hotelName) return true;
    
    const isDuplicate = await hotelApiResource.checkDuplicateName({ name: value });
    return !isDuplicate || t('Hotel Name already exists');
  }
};

const getDetail = async (id: any) => {
  await call(hotelApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    hotelModel.value = response.value?.data;
    originalModelString.value = JSON.stringify(hotelModel.value);
  }
};

const loadActiveDestinations = async () => {
  // Status ACTIVE ဖြစ်သော Destination များကိုသာ ဆွဲထုတ်ယူရန်
  const res = await destinationApiResource.getActiveDestinations();
  activeDestinationList.value = res?.data || [];
};

const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  const currentAdmin = authStore.user?.username || 'Admin';
  let apiUrl = hotelApiResource.register;

  if (isEditMode.value) {
    apiUrl = hotelApiResource.update;
    hotelModel.value.updatedBy = currentAdmin;
  } else {
    hotelModel.value.createdBy = currentAdmin;
    hotelModel.value.updatedBy = currentAdmin;
  }

  await call(apiUrl, { data: hotelModel.value });
  if (status.value == ApiStatus.SUCCESS) {
    router.push({ name: routeNames.hotelList });
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

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: 'Go Back',
    onClick: () => { router.push({ name: routeNames.hotelList }); },
    color: 'secondary',
    useLoading: false,
    useDisabled: status.value === ApiStatus.LOADING,
  },
  {
    icon: mdiUndo,
    label: 'Reset',
    onClick: handleReset,
    color: 'warning',
    useLoading: false,
    useDisabled: status.value === ApiStatus.LOADING,
  },
  {
    icon: mdiContentSave,
    label: 'Save',
    onClick: onSave,
    color: 'primary',
    useLoading: status.value === ApiStatus.LOADING,
    useDisabled: status.value === ApiStatus.LOADING,
  },
]);

onMounted(async () => {
  await loadActiveDestinations();
  let { id } = route.params;
  if (id !== 'new') {
    await getDetail(id);
  } else {
    originalModelString.value = JSON.stringify(hotelModel.value);
  }
});
</script>