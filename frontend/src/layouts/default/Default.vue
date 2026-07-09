<template>
  <v-app>
    <v-theme-provider :theme="appStore.theme">
      <v-layout>
        <DefaultNavigation v-model:drawer="drawer"></DefaultNavigation>
        <DefaultAppBar @nav-toggle="onNavToggle"></DefaultAppBar>
        <DefaultView v-if="isLayoutMounted" />
      </v-layout>
    </v-theme-provider>
  </v-app>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useAppStore } from '../../store/app';
import DefaultAppBar from './AppBar.vue';
import DefaultNavigation from './Navigation.vue';
import DefaultView from './View.vue';

const appStore = useAppStore();
const drawer = ref(true);
const isLayoutMounted = ref(false);

const onNavToggle = () => {
  drawer.value = !drawer.value;
};

// make sure layout is mounted before child views are mounting
onMounted(() => {
  isLayoutMounted.value = true;
});
</script>
