<template>
  <v-app-bar :order="0" density="comfortable" elevation="1" color="purple">
    <template #prepend>
      <v-app-bar-nav-icon @click="emits('nav-toggle')"></v-app-bar-nav-icon>
    </template>
    <template #title>
      <div id="appbar-title"></div>
    </template>
    <template #append>
      <div id="appbar-filter" class="pr-1 pr-sm-2 pr-md-4"></div>

      <div v-if="!smAndDown">
        <LocaleSelect></LocaleSelect>
      </div>

      

      <v-btn
        :icon="mdiAccount"
        variant="tonal"
        id="account-menu-activator"
      ></v-btn>

      <v-menu
        activator="#account-menu-activator"
        width="250"
        v-model="menuOpen"
        :close-on-content-click="false"
      >
        <v-list rounded="xl">
          <template v-for="item in userAccountMenus" :key="item.name">
            <v-list-item
              v-if="item.name != 'divider' && item.name != 'Language'"
              :to="item.to"
              :prepend-icon="item.icon"
              @click="item.onClick"
            >
              <v-list-item-title>{{ t(item.name) }}</v-list-item-title>
            </v-list-item>
            <LocaleSelectMobile
              v-else-if="item.name == 'Language'"
              :item="item"
              v-model:menu="menuOpen"
            ></LocaleSelectMobile>
            <v-divider v-else></v-divider>
          </template>
        </v-list>
      </v-menu>
    </template>
  </v-app-bar>

  <v-dialog v-model="logoutDialog" max-width="380" persistent>
    <v-card class="pa-4" rounded="xl" elevation="10">
      <v-card-title
        class="text-h5 font-weight-bold text-center justify-center pt-4"
        style="color: #5f5f5f"
      >
        Are you sure?
      </v-card-title>

      <v-card-actions class="justify-center pb-4 pt-4">
        <v-btn
          color="warning"
          variant="flat"
          class="text-subtitle-1 font-weight-bold px-8 py-2 mx-2 text-white"
          style="font-size: 16px !important; letter-spacing: normal"
          @click="confirmLogout"
          >Ok</v-btn
        >

        <v-btn
          color="warning"
          variant="flat"
          class="text-subtitle-1 font-weight-bold px-7 py-2 mx-2 text-white"
          style="font-size: 16px !important; letter-spacing: normal"
          @click="logoutDialog = false"
          >Cancel</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { mdiAccount, mdiAccountLock, mdiEarth, mdiLogout } from '@mdi/js';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import LocaleSelect from '../../locales/LocaleSelect.vue';
import LocaleSelectMobile from '../../locales/LocaleSelectMobile.vue';
import { routeNames } from '../../router/routes';
import { useAuthStore } from '../../store/auth';

const { t } = useI18n({ useScope: 'global' });

const emits = defineEmits(['nav-toggle']);
const authStore = useAuthStore();
const router = useRouter();
const { smAndDown } = useDisplay();

const menuOpen = ref<boolean>(false);

// const onLogout = () => {
//   authStore.clear();
//   router.push({ name: routeNames.login }).catch(() => {});
// };
const logoutDialog = ref<boolean>(false);

const onLogout = () => {
  menuOpen.value = false;
  logoutDialog.value = true;
};

const confirmLogout = () => {
  logoutDialog.value = false;
  authStore.clear();
  router.push({ name: routeNames.loginPage }).catch(() => {});
};
const userAccountMenus = computed(() => {
  if (smAndDown.value === true) {
    return [
      {
        icon: mdiAccount,
        name: 'Account',
        to: { name: routeNames.userDetail, params: { id: authStore.user.id } },
        onClick: () => {
          menuOpen.value = false;
        },
      },
      {
        icon: mdiAccountLock,
        name: 'Change Password',
        to: { name: routeNames.userChangePassword, params: { id: 'me' } },
        onClick: () => {
          menuOpen.value = false;
        },
      },
      { name: 'divider' },
      {
        icon: mdiEarth,
        name: 'Language',
        to: undefined,
        onClick: undefined,
      },
      { name: 'divider' },
      { icon: mdiLogout, name: 'Logout', to: undefined, onClick: onLogout },
    ];
  } else {
    return [
      {
        icon: mdiAccount,
        name: 'Account',
        to: {
          name: routeNames.userDetail,
          params: { id: authStore.user?.id ?? 'new' },
        },
        onClick: undefined,
      },
      {
        icon: mdiAccountLock,
        name: 'Change Password',
        to: { name: routeNames.userChangePassword, params: { id: 'me' } },
        onClick: undefined,
      },
      { name: 'divider' },
      { icon: mdiLogout, name: 'Logout', to: undefined, onClick: onLogout },
    ];
  }
});
</script>
