<template>
  <Detail
    v-bind="{
      title: t('Register Form'),
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

            <v-col cols="12" md="4" class="d-flex justify-center align-start">
              <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed; background-color: #f8f9fa;">
                <ImageInput
                  :image-url="customerModel.profileImageUrl"
                  v-model="customerModel.profileImage"
                  @delete="customerModel.profileImage = null"
                  image-height="180px"
                  image-width="100%"
                  width="100%"
                  class="mx-auto"
                  :label="t('Customer Image')"
                ></ImageInput>
              </v-card>
            </v-col>


            <v-col cols="12" md="8">
              <v-row dense>

                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="name"
                    v-model="customerModel.name"
                    :label="t('Name')"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                  ></v-text-field>
                </v-col>

               <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="email"
                    v-model="customerModel.email"
                    :rules="[rules.required,rules.maxLength(255), rules.email]"
                    :label="t('Email')"
                    
                    density="comfortable"
                    ></v-text-field>
                </v-col>

                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="phoneNumber"
                    v-model="customerModel.phoneNumber"
                    :label="t('Phone')"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                    type="number"
                  ></v-text-field>
                </v-col>

                

                <v-col cols="12" sm="6" class="py-1">
                  <v-select
                    v-model="customerModel.status"
                    :items="[
                              { title: t('ACTIVE'), value: 'ACTIVE' },
                              { title: t('INACTIVE'), value: 'INACTIVE' }
                            ]"
                    :label="t('Status')"
                    :rules="[rules.required]"
                    variant="outlined"
                    density="comfortable"
                  ></v-select>
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
import { ref, onMounted , computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import { required, minLength, maxLength, email} from '../../utils/validations';
import useApi, { ApiStatus } from '../../api/index.js';
import { useRoute, useRouter } from 'vue-router';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ImageInput from '../../components/common/ImageInput.vue';
import { useI18n } from 'vue-i18n';
import { Room, RoomModel } from '../../models/RoomModel.js';
import { useAuthStore } from '../../store/auth';
import { customerApiResource } from '../../api/resources/customerResource.js';
import { Role } from '../../constants/Role.js';
import { Customer, CustomerModel } from '../../models/CustomerModel.js';

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const customerModel = ref<any>(CustomerModel());
const { t } = useI18n();

const rules = {
  required,
  minLength,
  maxLength,
  email,
};
const roomTypeList = ref([]);

const { call, response, error, status } = useApi();
const authStore = useAuthStore();

const getDetail = async (id: any) => {
  await call(customerApiResource.getById, null, { id });

  if (status.value == ApiStatus.SUCCESS) {
    customerModel.value = response.value?.data as Customer;
  }
};



const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;


  if (authStore.userRole == Role.STUDENT) {
    customerModel.value.name = authStore.user.name;
  }

  let apiUrl = customerApiResource.register;
  if (customerModel.value.id) apiUrl = customerApiResource.update;

 await call(apiUrl, { data: customerModel.value });


  if (status.value == ApiStatus.SUCCESS) {
    router.go(-1);
  }
};



const breadcrumbs = ref([
  { title: t('General') },
  { title: t('Customer', 2), to: { name: routeNames.customerList } },
  { title: t('Detail') },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: 'Back',
    onClick: () => { router.go(-1); },
    color: '',
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
  

  let { id } = route.params;
  if (authStore.userRole == Role.STUDENT) {
    customerModel.value.id = authStore.user.id;
  }
  if (id != 'new') {
    getDetail(id)
  }
});
</script>
