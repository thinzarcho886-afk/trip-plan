<template>
  <v-app-bar elevation="0" height="80" color="primary" class="px-6">
    <div
      class="d-flex align-center"
      @click="goToDashboard"
      style="cursor: pointer"
      title="Go to Dashboard"
    >
      <v-icon size="30" color="#06402B" class="mr-2" :icon="mdiBeach"></v-icon>
<div class="d-flex flex-column lh-tight">
      <span class="text-subtitle-1 font-weight-black text-white text-uppercase">
        {{t('Budget Friendly')}}
      </span>
      <span class="text-caption font-weight-bold text-green-darken-4" style="margin-top: -4px;">
        {{t('Trip Planner Management System')}}
      </span>
    </div>    </div>
    
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>
    <v-spacer></v-spacer>

    <div class="d-flex align-center" style="gap: 20px">
      <v-btn variant="text" to="/" :prepend-icon="mdiHome"  class="text-white" exact>{{ t('Home') }}</v-btn>
      <v-btn variant="text" to="/about" :prepend-icon="mdiInformation" class="text-white" exact>{{ t('About Us') }}</v-btn>
     <v-btn variant="text" to="/public-hotels" :prepend-icon="mdiDomain" class="text-white">{{ t('Hotels') }}</v-btn>
     <v-btn variant="text" to="/public-transports" :prepend-icon="mdiCar3Plus" class="text-white">{{ t('Transport') }}</v-btn>
    <v-btn variant="text" to="/bookingHistoryPublic" :prepend-icon="mdiClipboardList" class="text-white">{{ t('Booking List') }}</v-btn>
    </div>

    <v-spacer></v-spacer>

    <div class="navbar-actions d-flex align-center" style="gap: 16px;">
      
      <div class="locale-selector-wrapper">
        <LocaleSelect></LocaleSelect>
      </div>

      <v-menu v-if="isLoggedIn" transition="scale-transition">
        <template v-slot:activator="{ props }">
          <v-btn
            color="#06402B"
            class="rounded-pill text-none font-weight-bold px-4"
            style="
              background-color: white !important;
              color: #06402B !important;
            "
            v-bind="props"
            :append-icon="mdiChevronDown"
          >
            <v-icon start :icon="mdiAccountCircle"></v-icon>
            {{ currentUserName }}
          </v-btn>
        </template>

        <v-list class="mt-2 rounded-lg" min-width="150" elevation="3">
          <v-list-item to="/edit-profile" link>
            <template v-slot:prepend>
              <v-icon :icon="mdiAccountEdit" color="#06402B"></v-icon>
            </template>
            <v-list-item-title class="font-weight-medium">Edit Profile</v-list-item-title>
          </v-list-item>

          <v-divider></v-divider>

          <v-list-item @click="onLogout" link>
            <template v-slot:prepend>
              <v-icon :icon="mdiLogout" color="error"></v-icon>
            </template>
            <v-list-item-title class="font-weight-medium text-error">Logout</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <v-btn
        v-else
        color="white"
        variant="outlined"
        rounded="lg"
        class="font-weight-bold"
        to="/loginPage"
        :prepend-icon="mdiAccountCircle"
      >
        Login
      </v-btn>

    </div>
  </v-app-bar>
</template>

<script setup lang="ts">
import { computed } from 'vue';

import { useI18n } from 'vue-i18n';

const { t } = useI18n();


import { useRouter } from 'vue-router';
import { useAuthStore } from '../../store/auth';
import LocaleSelect from '../../locales/LocaleSelect.vue';
import {
  mdiCity,
  mdiAccountCircle,
  mdiHome,
  mdiDomain,
  mdiBed,
  mdiInformation,
  mdiChevronDown,
  mdiAccountEdit,
  mdiLogout,
  mdiBeach,
  mdiAccountDetails,
  mdiClipboardList,
  mdiCar3Plus,
} from '@mdi/js';

const authStore = useAuthStore();
const router = useRouter();

const goToDashboard = () => {
  const userRole = authStore.userRole;

  if (isLoggedIn.value && (userRole === 'SYSADMIN' || userRole === 'OWNER')) {
    router.push('/admin');
  } else {
    router.push('/');
  }
};

const isLoggedIn = computed(
  () => authStore.isAuth || !!localStorage.getItem('user_session'),
);

const currentUserName = computed(() => {
  if (isLoggedIn.value) {
    if (authStore.user) {
      return authStore.user?.name || authStore.user?.username || 'User';
    }

    const savedSession = localStorage.getItem('user_session');
    if (savedSession) {
      try {
        const userData = JSON.parse(savedSession);
        return (
          userData?.name || userData?.username || userData?.user?.name || 'User'
        );
      } catch (e) {
        return 'User';
      }
    }
    return 'User';
  }
  return '';
});

const onLogout = () => {
  authStore.isAuth = false;
  authStore.user = null;

  localStorage.removeItem('user_session');
  localStorage.removeItem('token');

  router.push('/loginPage');
};
</script>