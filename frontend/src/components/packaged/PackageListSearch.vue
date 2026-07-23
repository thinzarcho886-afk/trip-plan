<template>
  <div class="pa-4" style="min-width: 350px; background-color: white;">
    <v-row dense>
      <!-- Package Name -->
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="searchParams.name"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Package Name')"
          @keyup.enter="onSearch"
        ></v-text-field>
      </v-col>

      <!-- Destination Name -->
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="searchParams.destinationName"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Destination Name')"
          @keyup.enter="onSearch"
        ></v-text-field>
      </v-col>

      <!-- Duration Name -->
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="searchParams.durationName"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Duration Name')"
          @keyup.enter="onSearch"
        ></v-text-field>
      </v-col>

      <!-- Status Enum Picker -->
      <v-col cols="12" sm="6">
        <EnumPicker
          v-model:value="searchParams.status"
          :label="t('Status')"
          :enum="statusOptions"
          :add-all="true"
          hide-details
          density="compact"
          variant="outlined"
        ></EnumPicker>
      </v-col>
    </v-row>

    <!-- Action Buttons -->
    <div class="pt-6 d-flex" style="gap: 0.5rem">
      <v-btn variant="text" @click="onClose">
        {{ t('Close') }}
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn color="primary" variant="text" @click="onReset">
        {{ t('Reset') }}
      </v-btn>
      <v-btn color="primary" variant="flat" @click="onSearch">
        {{ t('Search') }}
      </v-btn>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { Status } from '../../constants/Status';
import { routeNames } from '../../router/routes';
import { useRouteFilter } from '../../utils/useRouteFilter';
import EnumPicker from '../common/EnumPicker.vue';
import { PackageListParams, PackageListParamsModel } from '../../models/PackageModel';

const { t } = useI18n({ useScope: 'global' });
const router = useRouter();
const route = useRoute();

const emits = defineEmits(['search', 'close']);

const searchParams = ref<PackageListParams>(PackageListParamsModel());

// Computed object for Status enum options to support dynamic language changes
const statusOptions = computed(() => ({
  [t('ACTIVE')]: Status.ACTIVE,
  [t('INACTIVE')]: Status.INACTIVE,
}));

const onSearch = async () => {
  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.packageList,
      query: { ...searchParams.value },
    })
    .catch(() => {});

  // Close search popup
  onClose();
};

const onReset = () => {
  searchParams.value = PackageListParamsModel();
  onSearch();
};

const onClose = () => {
  emits('close');
};

defineExpose({
  onReset,
});

onMounted(() => {
  const { routeFilter, isFiltered } = useRouteFilter<PackageListParams>(
    searchParams.value,
    route.query
  );

  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>