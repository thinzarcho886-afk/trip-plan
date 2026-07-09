<template>
  <v-card max-width="480" class="mx-auto pa-4">
    <div class="mb-4 mt-2 text-center">
      <img
        src="../assets/images/Login_Logo.png"
        alt="Vue Material Admin"
        height="120"
      />
    </div>

    <v-card-text>
      <!-- error alert -->
      <div class="mb-6" v-if="status == ApiStatus.ERROR && error?.message">
        <v-alert type="error" variant="tonal"> {{ error.message }}</v-alert>
      </div>

      <!-- login form -->
      <v-form ref="loginFormRef" @submit.prevent="onLogin">
        <v-text-field
          v-model="loginData.username"
          :rules="[formRules.required]"
          label="Username"
          variant="outlined"
          class="my-2"
        ></v-text-field>

        <v-text-field
          v-model="loginData.password"
          :rules="[formRules.required]"
          label="Password"
          variant="outlined"
          class="my-2"
          :type="showPassword ? 'text' : 'password'"
          :append-inner-icon="showPassword ? mdiEye : mdiEyeOff"
          @click:append-inner="showPassword = !showPassword"
        ></v-text-field>

        <div class="mt-4">
          <v-btn
            color="primary"
            type="submit"
            block
            size="large"
            :loading="status == ApiStatus.LOADING"
          >
            Login
          </v-btn>
        </div>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import useApi, { ApiStatus } from '../api';
import { userApiResource } from '../api/resources/userResource';
import { useAuthStore } from '../store/auth';
import { mdiEye, mdiEyeOff } from '@mdi/js';
import {
  UserLoginModel,
  UserLogin,
  UserLoginResponse,
} from '../models/UserLoginModel';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();
const loginFormRef = ref<null | any>(null);
const formRules = {
  required: (v: string | number) => !!v || 'cannot be empty',
};
const loginData = reactive<UserLogin>(UserLoginModel());
const showPassword = ref<boolean>(false);

const { call, response, error, status } = useApi();



const onLogin = async () => {
  const { valid } = await loginFormRef.value.validate();
  if (!valid) return;

  await call(userApiResource.login, { data: loginData });

  if (status.value == ApiStatus.SUCCESS) {
    const { user, token } = response.value?.data as UserLoginResponse;

    if (!!user && !!token) {
      authStore.store(user, token);

      if (route.query.redirect)
        router.push({ path: route.query.redirect as string });
      else router.push({ name: 'Home' });
    }
  }
};
</script>
