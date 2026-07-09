<template>
  <v-hover>
    <template v-slot:default="{ isHovering, props: hoverProps }">
      <v-navigation-drawer
        :width="isHovering ? 96 : 58"
        color="primary"
        v-bind="hoverProps"
        v-model="drawer"
        :permanent="drawer"
      >
        <div
          style="
            height: 64px;
            cursor: pointer;
            position: relative;
            z-index: 9999;
            pointer-events: auto !important;
          "
          class="d-flex justify-center align-center"
          @click="goToUser"
        >
          <v-avatar :image="appLogoUrl" rounded="lg" size="51"></v-avatar>
        </div>

        <v-divider></v-divider>

        <v-list density="comfortable" class="py-0">
          <v-list-item
            v-for="(menu, index) in menus"
            :key="`menu-${index}-${menu.name}`"
            :to="menu.to"
            @click="onMenuClick(menu)"
            :active="selectedMenus?.name == menu.name"
            class="px-2 mb-1"
            active-class="bg-white"
            color="white"
          >
            <template v-slot:default="{ isActive }">
              <div class="d-flex flex-column align-center">
                <div>
                  <v-icon
                    :icon="menu.icon"
                    :color="isActive ? 'primary' : ''"
                  ></v-icon>
                </div>
                <div
                  v-if="isHovering"
                  class="menu-name text-center pt-1 text-truncate-two-line"
                  :class="{ 'text-primary': isActive }"
                >
                  {{ t(menu.name) }}
                </div>
              </div>
            </template>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>

      <v-navigation-drawer v-model="childDrawer" temporary v-bind="hoverProps">
        <div
          style="height: 64px"
          class="py-1 px-4 d-flex flex-column justify-center"
        >
          <div class="text-body-1 d-flex justify-space-between align-center">
            <div>
              {{ selectedMenus ? t(selectedMenus.name) : '' }}
            </div>

            <div>
              <v-btn
                variant="text"
                :icon="mdiChevronLeft"
                @click="childDrawer = false"
                size="small"
              >
              </v-btn>
            </div>
          </div>
        </div>
        <v-divider></v-divider>
        <v-list density="compact" nav>
          <v-list-item
            v-for="(childMenu, cIndex) in selectedMenus?.children"
            :to="childMenu.to"
            :key="`child-menu-${cIndex}-${childMenu.name}`"
            class="text-body-2"
            active-class="text-primary"
          >
            {{ t(childMenu.name, 2) }}
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </template>
  </v-hover>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { getMenus, Menu, getCurrentMenu } from '../../constants/Menus';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '../../store/auth';
import appLogoUrl from '../../assets/logoHome.png';
import { mdiChevronLeft } from '@mdi/js';
import { useI18n } from 'vue-i18n';
import { routeNames } from '../../router/routes';

const { t } = useI18n({ useScope: 'global' });

const props = defineProps(['drawer']);
const emits = defineEmits(['update:drawer']);

const router = useRouter();
const goToUser = () => {
  router.push({ name: routeNames.publicMain });
};

const route = useRoute();
const authStore = useAuthStore();
const childDrawer = ref<boolean>(false);
const drawer = computed({
  get() {
    return props.drawer;
  },
  set(v) {
    emits('update:drawer', v);
  },
});

const menus = computed<Menu[]>(() => getMenus(authStore.userRole));
const selectedMenus = ref<Menu>();

const onMenuClick = (menu: Menu): void => {
  selectedMenus.value = menu;

  if (menu.children && menu.children.length) {
    childDrawer.value = true;
  } else if (!!menu.to) {
    childDrawer.value = false;
    router.push(menu.to).catch(() => {});
  }
};

// set selected menu on create or route change
watch(
  () => route.name,
  (v) => {
    selectedMenus.value = getCurrentMenu(v, menus.value);
  },
  { immediate: true },
);
</script>

<style>
.menu-name {
  font-size: 0.75rem;
  line-height: 1rem;
  user-select: none;
}
</style>
