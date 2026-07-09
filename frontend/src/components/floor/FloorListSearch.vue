<template>
  <v-row>
    <v-col cols="12" md="6">
      <v-text-field
        density="compact"
        variant="outlined"
        hide-details
        :label="t('Floor')"
        v-model="searchParams.name"
      ></v-text-field>
    </v-col>
    <v-col cols="12" md="6" v-if="authStore.userRole == Role.ADMIN">
      <company-picker
        v-model:id="searchParams.companyId"
        v-model:name="searchParams.company"
        :label="t('Company')"
        :params="{ status: Status.ACTIVE }"
        density="compact"
        variant="outlined"
        hide-details
      ></company-picker>
    </v-col>
    <v-col cols="12" md="6">
      <EnumPicker
        v-model:value="searchParams.status"
        :label="t('Status')"
        :enum="Status"
        :add-all="true"
        hide-details
        density="compact"
        variant="outlined"
      ></EnumPicker>
    </v-col>
    
  </v-row>

  <div class="pt-6 d-flex" style="gap: 0.5rem">
    <v-btn variant="text" @click="onClose"> {{ t('Close') }} </v-btn>
    <v-spacer></v-spacer>
    <v-btn color="primary" variant="text" @click="onReset">{{
      t('Reset')
    }}</v-btn>
    <v-btn color="primary" variant="flat" @click="onSearch"
      >{{ t('Search') }}
    </v-btn>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { Status } from '../../constants/Status.js';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes.js';
import { useRouteFilter } from '../../utils/useRouteFilter.js';
import CompanyPicker from '../company/CompanyPicker.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import EnumPicker from '../common/EnumPicker.vue';
import { FloorListParams, FloorListParamsModel } from '../../models/FloorModel.js';

const { t } = useI18n({ useScope: 'global' });

const authStore = useAuthStore();

const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();
const searchParams = ref<FloorListParams>(FloorListParamsModel());

const onSearch = async () => {
  if (authStore.userRole != Role.ADMIN) {
    searchParams.value.companyId = authStore.user.companyId;
    searchParams.value.company = authStore.user.companyName;
  }
  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.floorList,
      query: searchParams.value,
    })
    .catch(() => {});

  // close search menu
  onClose();
};

const onReset = () => {
  searchParams.value = FloorListParamsModel();
  onSearch();
};

const onClose = () => {
  emits('close');
};

// to use with Ref in List component
defineExpose({
  onReset,
});

onMounted(() => {
  const { routeFilter, isFiltered } = useRouteFilter<FloorListParams>(
    searchParams.value,
    route.query
  );

  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>
