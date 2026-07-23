<template>
  <v-container class="d-flex justify-center align-center fill-height">
    <v-card width="500" class="pa-6" elevation="4">
      <v-card-title class="text-center font-weight-bold mb-4">
        {{t('Change Password')}}
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="handleUpdatePassword" ref="loginFormRef">
          <!-- <v-text-field
            label="User Name"
            v-model="userName"
            disabled
            variant="outlined"
             :rules="[rules.required, rules.maxLength(100)]"
            density="compact"
            class="mb-2"
          ></v-text-field> -->

          <v-text-field
            name="oldPassword"
            v-model="form.oldPassword"
            :rules="[rules.required, rules.maxLength(255), rules.password]"
            :label="t('Current Password')"
            :type="showOldPassword ? 'text' : 'password'"
            :append-inner-icon="showOldPassword ? mdiEye : mdiEyeOff"
            @click:append-inner="showOldPassword = !showOldPassword"
          ></v-text-field>

          <v-text-field
            name="newPassword"
            v-model="form.newPassword"
            :rules="[rules.required, rules.maxLength(255), rules.password]"
            :label="t('New Password')"
            :type="showNewPassword ? 'text' : 'password'"
            :append-inner-icon="showNewPassword ? mdiEye : mdiEyeOff"
            @click:append-inner="showNewPassword = !showNewPassword"
          ></v-text-field>

          <v-text-field
            name="confirmNewPassword"
            v-model="form.confirmNewPassword"
            :rules="[
              rules.required,
              (v) => v == form.newPassword || t('passwordNotMatch'),
            ]"
            :label="t('Confirm New Password')"
            :type="showConfirmNewPassword ? 'text' : 'password'"
            :append-inner-icon="showConfirmNewPassword ? mdiEye : mdiEyeOff"
            @click:append-inner="
              showConfirmNewPassword = !showConfirmNewPassword
            "
          ></v-text-field>
        </v-form>
      </v-card-text>

      <v-card-actions class="px-4 pb-4 d-flex justify-space-between">
        <v-btn
          color="#06402B"
          variant="elevated"
          @click="handleCancel"
          class="px-8"
        >
          {{t('Cancel')}}
        </v-btn>

        <v-btn
          color="#06402Bcu"
          variant="elevated"
          @click="handleUpdatePassword"
          :loading="status == ApiStatus.LOADING"
          class="px-6"
        >
          {{t('Change Password')}}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import axios from 'axios';
import { Customer, CustomerChangePassword, CustomerChangePasswordModel, CustomerModel } from '../models/CustomerModel.js';

import {
  required,
  maxLength,
  minLength,
  password,
} from '../utils/validations.js';
import { useI18n } from 'vue-i18n';
import { mdiContentSave, mdiArrowLeft, mdiEye, mdiEyeOff } from '@mdi/js';
import useApi, { ApiStatus } from '../api';
import { userApiResource } from '../api/resources/userResource';
const showOldPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmNewPassword = ref(false);
const { t } = useI18n({ useScope: 'global' });
const rules = {
  required,
  maxLength,
  minLength,
  password,
};
const customerModel = ref<Customer>(CustomerModel());
const isLoading = ref(false);
const form = reactive({
  id: '',
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: '',
});
const changePasswordModel = ref<CustomerChangePassword>(CustomerChangePasswordModel());
const loginFormRef = ref<null | any>(null);

const { call, response, error, status } = useApi();

const handleCancel = () => window.history.back();

const handleUpdatePassword = async () => {
  const { valid } = await loginFormRef.value.validate();
  if (!valid) return;

  const loginData = {
    oldPassword: form.oldPassword,
    newPassword: form.newPassword,
  };

  if (form.newPassword !== form.confirmNewPassword) {
    alert(t('Passwords do not match!'));
    return;
  }

  await call(userApiResource.changePassword, { data: loginData });

  if (status.value == ApiStatus.SUCCESS) {
    alert(t('Password Updated Successfully!'));
    handleCancel();
  }
  if (status.value == ApiStatus.ERROR) {
    alert(t('Something went wrong!'));
  }

  // try {
  //   isLoading.value = true;
  //   await axios.post('/api/auth/change-password-by-current-user', {
  //     id: form.id,
  //     oldPassword: form.oldPassword,
  //     newPassword: form.newPassword,
  //   });
  //   alert('Password Updated Successfully!');
  //   handleCancel();
  // } catch (error: unknown) {
  //   const err = error as any;
  //   alert(err.response?.data?.message || 'Something went wrong!');
  // } finally {
  //   isLoading.value = false;
  // }
};
</script>
