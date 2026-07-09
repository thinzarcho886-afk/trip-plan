<template>
  <div class="pa-4" style="min-width: 350px; background-color: white;">
    <v-row dense>
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="searchParams.hostelName"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Hostel Name')"
        ></v-text-field>
      </v-col>

      <!-- <v-col cols="12" md="6">
        <v-text-field
          v-model="searchParams.studentPhone"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Phone')"
        ></v-text-field>
      </v-col>

      <v-col cols="12" md="6">
        <v-text-field
          v-model="searchParams.studentAddress"
          density="compact"
          variant="outlined"
          hide-details
          :label="t('Address')"
        ></v-text-field>
      </v-col> -->

      <v-col cols="12" sm="6">
        <EnumPicker
          v-model:value="searchParams.status"
          :label="t('Status')"
         :enum="{ [t('ACTIVE')]: Status.ACTIVE, [t('INACTIVE')]: Status.INACTIVE }"
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
import { ref, onMounted } from 'vue';
import { Status } from '../../constants/Status';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes';
import { useRouteFilter } from '../../utils/useRouteFilter';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth';
import { Role } from '../../constants/Role';
import EnumPicker from '../common/EnumPicker.vue';
import { HostelListParams, HostelListParamsModel } from '../../models/HostelModel';

const { t } = useI18n({ useScope: 'global' });

const authStore = useAuthStore();
const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();
const searchParams = ref<HostelListParams>(HostelListParamsModel());

const onSearch = async () => {

  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.hostelList,
      query: searchParams.value,
    })
    .catch(() => {});

  // close search menu
  onClose();
};

const onReset = () => {
  searchParams.value = HostelListParamsModel();
  onSearch();
};

const onClose = () => {
  emits('close');
};

defineExpose({
  onReset,
});

onMounted(() => {
  const { routeFilter, isFiltered } = useRouteFilter<HostelListParams>(
    searchParams.value,
    route.query
  );

  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>
