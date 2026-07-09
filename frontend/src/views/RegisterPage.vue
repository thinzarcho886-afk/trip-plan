<template>
  <v-container fluid class="fill-height bg-grey-lighten-4">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12 rounded-lg pa-6">
          <v-card-title class="text-h4 font-weight-bold text-center py-4">
            Create Account
          </v-card-title>

          <v-card-text>
            <v-form ref="formRef" @submit.prevent="submitForm">
              <div class="d-flex justify-center mb-6">
                <ImageInput
                  :image-url="form.imageUrl"
                  v-model="form.image"
                  @delete="form.imageUrl = ''"
                  image-height="180px"
                  image-width="200%"
                  width="200%"
                  class="mx-auto"
                  :label="t('Image')"
                  :disabled="isUpdate && authStore.user.role !== Role.SYSADMIN"
                ></ImageInput>
              </div>

              <v-text-field
                v-model="form.name"
                label="Full Name"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255)]"
              >
              </v-text-field>

              <v-text-field
                v-model="form.email"
                label="Email"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255), rules.email]"
              >
              </v-text-field>

              <v-text-field
                v-model="form.phone"
                label="Phone"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255), rules.phone]"
              >
              </v-text-field>

              <v-text-field
                v-model="form.address"
                label="Address"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255)]"
              >
              </v-text-field>

              <v-select
                v-model="form.role"
                :items="['OWNER', 'STUDENT']"
                label="Role"
                density="compact"
                variant="outlined"
              >
              </v-select>

              <h3 class="text-h6 mt-4">Account Information</h3>
              <v-text-field
                v-model="form.username"
                label="Username"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255)]"
              >
              </v-text-field>

              <v-text-field
                v-model="form.password"
                label="Password"
                type="password"
                density="compact"
                variant="outlined"
                :rules="[rules.required, rules.maxLength(255), rules.password]"
              >
              </v-text-field>

              <v-btn
                type="submit"
                color="primary"
                block
                size="large"
                class="mt-4 font-weight-bold"
              >
                CREATE ACCOUNT
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { userApiResource } from '../api/resources/userResource';
import useApi, { ApiStatus } from '../api';
import { User, UserModel } from '../models/UserModel';
import { UserLoginResponse } from '../models/UserLoginModel';
import {
  required,
  maxLength,
  email,
  password,
  phone,
} from '../utils/validations.js';
import { useAuthStore } from '../store/auth';
import { Role } from '../constants/Role.js';
import ImageInput from '../components/common/ImageInput.vue';
const isUpdate = ref(false);
const { t } = useI18n();
const authStore = useAuthStore();
const route = useRoute();
const rules = {
  required,
  maxLength,
  email,
  password,
  phone,
};
const router = useRouter();
const formRef = ref();
// const imageFile = ref<File | null>(null);
// const imageUrl = ref<string | null>(null);

const form = ref({
  image: '',
  imageUrl: '',
  name: '',
  email: '',
  phone: '',
  address: '',
  status: 'ACTIVE',
  role: 'OWNER',
  username: '',
  password: '',
});

// const onFileChange = () => {
//   if (imageFile.value) {
//     imageUrl.value = URL.createObjectURL(imageFile.value);
//   }
// };
const { response, call, status } = useApi();
const submitForm = async () => {
  const formData = new FormData();
  // if (imageFile.value) formData.append('image', imageFile.value);

  for (const key in form.value) {
    formData.append(key, form.value[key]);
  }

  if (form.value.role == 'OWNER') {
    formData.append('ownerImage', form.value.imageUrl);
    formData.append('ownerImageUrl', form.value.image);
    formData.append('ownerName', form.value.name);
    formData.append('ownerEmail', form.value.email);
    formData.append('ownerPhone', form.value.phone);
    formData.append('ownerAddress', form.value.address);
    // formData.append("status","INACTIVE")
    formData.append('ownerStatus', 'ACTIVE');
  } else {
    formData.append('studentImage', form.value.imageUrl);
    formData.append('studentImageUrl', form.value.image);
    formData.append('studentName', form.value.name);
    formData.append('studentEmail', form.value.email);
    formData.append('studentPhone', form.value.phone);
    formData.append('studentAddress', form.value.address);
    // formData.append("status","ACTIVE")
    formData.append('studentStatus', 'ACTIVE');
  }

  await call(userApiResource.register, { data: formData });

  if (status.value == ApiStatus.SUCCESS) {
    alert('Account created!');
    const { user, token } = response.value?.data as UserLoginResponse;

    if (!!user && !!token) {
      authStore.store(user, token);

      if (route.query.redirect)
        router.push({ path: route.query.redirect as string });
      else router.push('/');
    }
  }
};
</script>
