<template>
  <Detail
    v-bind="{
      title: t('Duration'),
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
                v-model="durationModel.name"
                :rules="[rules.required, rules.maxLength(100)]"
                :label="t('Name')"
              ></v-text-field>
            </v-col>
          </v-row>
           <v-row>
               <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="description"
                          v-model="durationModel.description"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Description')"
                         
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col>
                    </v-row>
            <v-row>
            <v-col cols="12" sm="3" md="6" v-if="durationModel.status">
              <v-switch
                name="status"
                :label="t(durationModel.status)"
                v-model="durationModel.status"
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
import { mdiContentSave, mdiArrowLeft, mdiRefresh } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Duration, DurationModel } from '../../models/DurationModel.js';
import DurationPicker from '../../components/duration/DurationPicker.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import { durationApiResource } from '../../api/resources/durationResource.js';
const { t } = useI18n({ useScope: 'global' });

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const durationModel = ref<Duration>(DurationModel());
const rules = {
  required,
  minLength,
  maxLength,
};
const { call, response, error, status } = useApi();
const authStore = useAuthStore();

const getDetail = async (id: any) => {
  await call(durationApiResource.getById, null, { id });

  if (status.value == ApiStatus.SUCCESS) {
    durationModel.value = response.value?.data as Duration;
  }
};

const onSave = async () => {
  // if (authStore.userRole != Role.SYSADMIN) {
  //   durationModel.value.name = authStore.user.name;
  // }

  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = durationApiResource.save;
  if (durationModel.value.id) apiUrl =durationApiResource.update;

  await call(apiUrl, { data: durationModel.value });

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
  { title: t('Duration'), to:{ name: routeNames.durationList } },
  { title: t('Detail') },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => {
      router.push({ name: routeNames.durationList }).catch(() => {});
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
