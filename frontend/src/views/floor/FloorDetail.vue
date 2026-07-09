<template>
  <Detail
    v-bind="{
      title: t('Floor'),
      loading: status == ApiStatus.LOADING,
      error: status == ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error.message,
      formValid,
      breadcrumbs,
      actions,
    }"
  >
    <template #form>
      <v-form
        v-model="formValid"
        ref="detailFormRef"
        @submit.prevent="
          () => {
            // disable enter triggers on inputs
          }
        "
      >
        <v-container>
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field
                name="name"
                v-model="floorModel.name"
                :rules="[rules.required, rules.maxLength(100)]"
                :label="t('Name')"
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="6" v-if="authStore.userRole == Role.SYSADMIN">
              <CompanyPicker
                v-model:id="floorModel.companyId"
                v-model:name="floorModel.companyName"
                :label="t('Company')"
                :params="{ status: Status.ACTIVE }"
                :rules="[() => !!floorModel.companyId || 'required']"
              ></CompanyPicker>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12">
              <v-textarea
                name="description"
                v-model="floorModel.description"
                :rules="[rules.maxLength(300)]"
                :label="t('Description')"
                rows="5"
              ></v-textarea>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" md="6" v-if="floorModel.id">
              <v-switch
                name="status"
                :label="t(floorModel.status)"
                v-model="floorModel.status"
                :true-value="Status.ACTIVE"
                :false-value="Status.INACTIVE"
              ></v-switch>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import { required, minLength, maxLength } from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { useRoute, useRouter } from 'vue-router';
import { Status } from '../../constants/Status.js';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Floor, FloorModel } from '../../models/FloorModel.js';
import CompanyPicker from '../../components/company/CompanyPicker.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import { floorApiResource } from '../../api/resources/floorResource.js';

const { t } = useI18n({ useScope: 'global' });

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const floorModel = ref<Floor>(FloorModel());
const rules = {
  required,
  minLength,
  maxLength,
};

const { call, response, error, status } = useApi();
const authStore = useAuthStore();

const getDetail = async (id: any) => {
  await call(floorApiResource.getById, null, { id });

  if (status.value == ApiStatus.SUCCESS) {
    floorModel.value = response.value?.data as Floor;
  }
};

const onSave = async () => {
  if (authStore.userRole != Role.SYSADMIN) {
    floorModel.value.companyId = authStore.user.companyId;
    floorModel.value.companyName = authStore.user.companyName;
  }

  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = floorApiResource.save;
  if (floorModel.value.id) apiUrl = floorApiResource.update;

  await call(apiUrl, { data: floorModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    // back?
    router.go(-1);
  }
};

type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

// custom breadcrumbs
const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('General') },
  { title: t('Floor'), to: { name: routeNames.floorList } },
  { title: t('Detail') },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => {
      router.push({ name: routeNames.floorList }).catch(() => {});
    },
    color: '',
    useLoading: false,
    useDisabled: false,
  },
  {
    icon: mdiContentSave,
    label: t('Save'),
    onClick: onSave,
    color: 'primary',
    useLoading: true,
    useDisabled: true,
  },
]);

onMounted(() => {
  let { id } = route.params;
  if (id != 'new') getDetail(id);
});
</script>
