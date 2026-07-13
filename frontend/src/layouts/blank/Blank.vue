<template>
  <v-app>
    <v-theme-provider :theme="appStore.theme">
      <v-layout class="d-flex flex-column">
        <AppBar></AppBar>
        <BlankView />
      </v-layout>
    </v-theme-provider>
     <Footer v-if="route.name == routeNames.loginPage" />
      <PublicFooter v-else />
  </v-app>
</template>

<script lang="ts" setup>
import { useAppStore } from '../../store/app';
import BlankView from './View.vue';
import Footer from './Footer.vue';
import { useRoute } from 'vue-router';
import { routeNames } from '../../router/routes';
import AppBar from './AppBar.vue';
import PublicFooter from './PublicFooter.vue';

const appStore = useAppStore();
const route = useRoute();
</script>

