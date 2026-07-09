<template>
  <div class="px-2 px-sm-4 px-md-6">
    <!-- title -->
    <Teleport to="#appbar-title"> {{ props.title || 'List' }} </Teleport>
    <!-- filter  -->
    <Teleport to="#appbar-filter">
      <!-- filter activator -->
      <template v-if="!!props.filters">
        <div id="filter-menu-activator">
          <div v-if="breakpointXs">
            <v-btn :icon="mdiMagnify"></v-btn>
          </div>
          <div
            v-else
            class="bg-white rounded-lg px-3 py-2 d-flex justify-space-between align-center"
            style="width: 200px; height: 40px; cursor: pointer;"
          
          >
            <div>
              <v-icon :icon="mdiMagnify" class="mr-2" start></v-icon>
              {{ t('Search') }}
            </div>
            <div>
              <span class="search-shortcut"> {{t('press/')}}/ </span>
            </div>
          </div>
        </div>

        <!-- filter menu -->
        <v-menu
          activator="#filter-menu-activator"
          :close-on-content-click="false"
          content-class="rounded-lg"
          :width="breakpointXs ? '100%' : '500px'"
          v-model="filterMenu"
          eager
        >
          <v-card>
            <v-container class="pa-3">
              <component
                ref="filterRef"
                :is="props.filters.component"
                @search="onFilterSearch"
                @close="onFilterClose"
              ></component>
            </v-container>
          </v-card>
        </v-menu>
      </template>
    </Teleport>

    <!-- breadcrumbs and actions -->
    <div
      class="d-flex justify-space-between align-center py-4"
      style="height: 68px; font-size: 14px"
    >
      <!-- breadcrumbs -->
      <div>
        <v-breadcrumbs
          v-if="!breakpointXs"
          :items="breadcrumbs"
          class="text-capitalize"
        >
        </v-breadcrumbs>
        <v-spacer v-else></v-spacer>
      </div>

      <!-- actions -->
      <div class="d-flex" style="gap: 1em">
        <slot name="actions">
          <template v-if="props.actions" v-for="action in props.actions">
            <v-btn
              :color="action.color"
              :to="action.to"
              @click="action.onClick"
            >
              <v-icon :icon="action.icon" start v-if="action.icon"></v-icon>
              {{ t(action.label) }}
            </v-btn>
          </template>
        </slot>
      </div>
    </div>

    <!-- table -->
    <div>
      <slot name="summary"></slot>
      <slot name="datatable" :clearFilter="onFilterClear"></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { mdiMagnify, mdiFilter } from '@mdi/js';
import { useDisplay } from 'vuetify';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const props = defineProps(['title', 'breadcrumbs', 'actions', 'filters']);

const route = useRoute();
const { xs: breakpointXs } = useDisplay();
const filterMenu = ref(false);
const filterRef = ref<any>(null);

const breadcrumbs = computed(() => {
  return (
    props.breadcrumbs ??
    route.fullPath
      .split('/')
      .filter((i) => i)
      .map((j) => ({
        title: j,
      }))
  );
});

const filterShortcutListener = (event: any) => {
  const keyName = event.key;
  if (keyName == '/') filterMenu.value = true;
};

const onFilterSearch = (e: any) => {
  props.filters.onSearch(e);
};

const onFilterClose = (e: any) => {
  filterMenu.value = false;
};

const onFilterClear = (e: any) => {
  const { onReset } = filterRef.value;
  if (onReset && typeof onReset == 'function') onReset();
};

onMounted(() => {
  document.addEventListener('keyup', filterShortcutListener, false);
});

onUnmounted(() => {
  document.removeEventListener('keyup', filterShortcutListener, false);
});
</script>

<style>
.search-shortcut {
  color: grey;
  border: 1px solid gray;
  padding: 2px 6px;
  border-radius: 4px;
  margin-left: 8px;
  font-size: 14px;
}
</style>
