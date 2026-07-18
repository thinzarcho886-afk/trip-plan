<template>
  <v-row>
    <v-col cols="12" sm="6">
      <v-text-field
        v-model="searchParams.username"
        density="compact"
        variant="outlined"
        hide-details
        :label="t('Username')"
      ></v-text-field>
    </v-col>
   <v-col cols="12" sm="6">
      <EnumPicker
        v-model:value="searchParams.role"
        :label="t('Role')"
        :enum="Role"
        :add-all="true"
        density="compact"
        variant="outlined"
        hide-details
      ></EnumPicker>
    </v-col>

    <v-col cols="12" sm="6">
      <EnumPicker
        v-model:value="searchParams.status"
        :label="t('Status')"
        :enum="Status"
        :add-all="true"
        density="compact"
        variant="outlined"
        hide-details
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
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { UserListParams, UserListParamsModel } from '../../models/UserModel';
import { Role } from '../../constants/Role';
import { Status } from '../../constants/Status';
import { useRoute, useRouter } from 'vue-router';
import { routeNames } from '../../router/routes';
import { useRouteFilter } from '../../utils/useRouteFilter';
import { useI18n } from 'vue-i18n';
import EnumPicker from '../common/EnumPicker.vue';
import { useAuthStore } from '../../store/auth';
const { t } = useI18n({ useScope: 'global' });
const emits = defineEmits(['search', 'close']);
const router = useRouter();
const route = useRoute();
const searchParams = ref<UserListParams>(UserListParamsModel());

const onSearch = async () => {
  emits('search', searchParams.value);

  await router
    .replace({
      name: routeNames.userList,
      query: searchParams.value,
    })
    .catch(() => {});

  // close search menu
  onClose();
};

const onReset = () => {
  searchParams.value = UserListParamsModel();
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
  const { routeFilter, isFiltered } = useRouteFilter<UserListParams>(
    searchParams.value,
    route.query
  );

  if (isFiltered.value) {
    searchParams.value = routeFilter.value;
  }
  onSearch();
});
</script>
