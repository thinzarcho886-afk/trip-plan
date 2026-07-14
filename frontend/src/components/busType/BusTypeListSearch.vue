<template>
  <v-row>
    <v-col cols="12" sm="6">
      <v-text-field
        v-model="searchParams.name"
        :label="t('Bus Types Name')"
        density="compact"
        variant="outlined"
        hide-details
      ></v-text-field>
    </v-col>

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
import { Status } from '../../constants/Status';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes';
import { useRouteFilter } from '../../utils/useRouteFilter';
import BusTypePicker from '../busType/BusTypePicker.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth';
import { Role } from '../../constants/Role';
import EnumPicker from '../common/EnumPicker.vue';
import { BusTypeListParams, BusTypeListParamsModel } from '../../models/BusTypeModel';

const { t } = useI18n({ useScope: 'global' });

const authStore = useAuthStore();
const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();
const searchParams = ref<BusTypeListParams>(BusTypeListParamsModel());

const onSearch = async () => {
  if (authStore.userRole != Role.SYSADMIN) {
    searchParams.value.name = authStore.user.name;
  }
  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.busTypeList,
      query: searchParams.value,
    })
    .catch(() => {});

  // close search menu
  onClose();
};

const onReset = () => {
  searchParams.value = BusTypeListParamsModel();
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
  const { routeFilter, isFiltered } = useRouteFilter<BusTypeListParams>(
    searchParams.value,
    route.query
  );

  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>
