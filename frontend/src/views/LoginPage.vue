<template>
  <v-container class="fill-height welcome-bg" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="5" lg="4">
        <v-card
          class="pa-10 rounded-xl"
          elevation="2"
          color="rgba(255, 255, 255, 0.95)"
        >
          <div class="text-center mb-8">
            <v-icon
              size="30"
              color="#06402B"
              class="mr-2"
              :icon="mdiBeach"
            ></v-icon>
            <div class="text-h6 font-weight-bold text-green">
             Budget Friendly Trip Planner Management System
            </div>
          </div>

          <v-form ref="loginFormRef" @submit.prevent="onLogin">
            <v-text-field
              v-model="loginData.username"
              label="Enter your name"
              variant="outlined"
              density="comfortable"
              :rules="[(v) => !!v || 'Username is required']"
            ></v-text-field>

            <v-text-field
              v-model="loginData.password"
              label="Password"
              type="password"
              variant="outlined"
              density="comfortable"
              :rules="[(v) => !!v || 'Password is required']"
            ></v-text-field>

            <div class="d-flex justify-center mt-2">
              <v-btn
                type="submit"
                color="#06402B"
                size="x-large"
                rounded="pill"
                width="280"
                class="text-none font-weight-bold"
                :loading="status === ApiStatus.LOADING"
              >
                Login
              </v-btn>
            </div>

            <div class="text-center mt-6">
              <span class="text-grey-darken-1">Don't have an account? </span>
              <router-link
                to="/registerPage"
                class="text-green font-weight-bold text-decoration-none"
              >
                Create Account
              </router-link>
            </div>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import useApi, { ApiStatus } from '../api';
import { userApiResource } from '../api/resources/userResource';
import { useAuthStore } from '../store/auth';
import { routeNames } from '../router/routes';

import { mdiBeach, mdiCity } from '@mdi/js';
import {
  UserLoginModel,
  UserLogin,
  UserLoginResponse,
} from '../models/UserLoginModel';
const { t } = useI18n();

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

      if (route.query.redirect) {
        router.push({ path: route.query.redirect as string });
        return;
      }

      if (user.role === 'OWNER' || user.role === 'SYSADMIN') {
        router.push({ name: routeNames.home });
      } else  if (user.role === 'CUSTOMER'){
        router.push({ name: routeNames.publicMain });
      }
      else{
        alert(t('Username Or Password is incorrect.'));
      }
    }
  }
};
</script>

<style scoped>
.welcome-bg {
  background: linear-gradient(135deg, #f3e5f5 0%, #e0f7fa 100%);
  min-height: 100vh;
}
</style>
