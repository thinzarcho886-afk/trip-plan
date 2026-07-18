<template>
  <v-container fluid class="fill-height bg-grey-lighten-4 py-10">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="5">
        <v-card class="elevation-4 rounded-xl overflow-hidden">
          <div
            class="bg-#2C5E82 pt-6 pb-12 text-center"
            style="border-radius: 0 0 50% 50%"
          >
            <h2 class="text-black text-h5">Edit Profile</h2>
            
          </div>

          <v-card-text class="px-8">
            <v-form ref="formRef" @submit.prevent="onSave">
              <div class="d-flex justify-center mb-6">
                
                <ImageInput
                  :image-url="customerModel.profileImageUrl"
                  v-model="customerModel.profileImage"
                  @delete="customerModel.profileImageUrl = ''"
                  image-height="180px"
                  image-width="200%"
                  width="200%"
                  class="mx-auto"
                  :label="t('Image')"
                ></ImageInput>
              </div>


              
                <v-text-field
                  v-model="customerModel.name"
                  label="Name"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255)]"
                ></v-text-field>

                <v-text-field
                  v-model="customerModel.email"
                  label="Email"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.email]"
                ></v-text-field>

                <v-text-field
                  v-model="customerModel.phoneNumber"
                  label="Phone"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.phone]"
                ></v-text-field>

               
              

              

              <div class="text-center my-4">
                <v-btn
                  variant="text"
                  color="#2C5E82-darken-2"
                  @click="goToChangePassword"
                  class="text-none font-weight-bold"
                >
                  Change Password?
                </v-btn>
              </div>

              <v-row>
                <v-col cols="6">
                  <v-btn
                    variant="outlined"
                    color="grey"
                    block
                    rounded="lg"
                    @click="router.back()"
                    class="text-none"
                  >
                    Cancel
                  </v-btn>
                </v-col>
                <v-col cols="6">
                  <v-btn
                    type="submit"
                    color="#06402B"
                    block
                    rounded="lg"
                    :loading="loading"
                    class="text-none text-white font-weight-bold"
                  >
                    Save Changes
                  </v-btn>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { customerApiResource } from '../api/resources/customerResource';
import { useAuthStore } from '../store/auth';
import useApi from '../api/index';
import ImageInput from '../components/common/ImageInput.vue';
import {
  required,
  maxLength,
  email,
  password,
  phone,
} from '../utils/validations.js';
import { Customer, CustomerModel } from '../models/CustomerModel';

const { t } = useI18n();
const formRef = ref<any>(null);
const router = useRouter();
const authStore = useAuthStore();
const loading = ref(false);

const rules = {
  required,
  maxLength,
  email,
  password,
  phone,
};

const { call, response, status } = useApi<any>();

const customerModel = ref<Customer>(CustomerModel());

onMounted(async () => {
  const customerId = authStore.user?.customerId;
  if (!customerId) return;

  await call(customerApiResource.getByCustomerId, null, { id: customerId });

  if (status.value === 'success' && response.value) {
    const data = response.value.data;

    customerModel.value = data as Customer;
  }
});

const onSave = async () => {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  loading.value = true;
  customerModel.value.password = '';

  try {
    let apiUrl = customerApiResource.updateCustomer;
    await call(apiUrl, { data: customerModel.value });
    if (status.value === 'success') {
      alert('Successfully Saved!');

      authStore.user = { ...authStore.user, ...customerModel.value };

     
        router.push('/');
      
    }
  } catch (error) {
    console.error('Save error:', error);
    alert('Failed to save profile. Please try again.');
  } finally {
    loading.value = false;
  }
};

const goToChangePassword = () => {
  router.push('/changePassword');
};
</script>
