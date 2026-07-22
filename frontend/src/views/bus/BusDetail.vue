 <template>
  <Detail
    v-bind="{
      title: t('Bus'),
      loading: status === ApiStatus.LOADING,
      error: status === ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error?.message,
      formValid: formValid,
      breadcrumbs,
      actions,
    }"
  >
    <template #form>
      <v-form
        v-model="formValid"
        ref="detailFormRef"
        @submit.prevent="onSave"
        class="pa-6"
      >
        <v-container fluid class="pa-0">
          <v-row>
            <v-col cols="12" md="4" class="d-flex justify-center align-start">
              <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed; background-color: #f8f9fa;">
                <ImageInput
                  :image-url="busModel.imageUrl"
                  v-model="busModel.imageUrl"
                  @delete="busModel.imageUrl = null"
                  image-height="180px"
                  image-width="100%"
                  width="100%"
                  class="mx-auto"
                  :label="t('Bus Type Image')"
                ></ImageInput>
              </v-card></v-col>
            <v-col cols="12" md="8">
              <v-row>
                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="name"
                    v-model="busModel.name"
                    :rules="[rules.required, rules.maxLength(100)]"
                    :label="t('Bus Name')"
                    density="comfortable"
                    :readonly="usernameFieldState.readonly"
                    :disabled="usernameFieldState.disabled"
                    :variant="usernameFieldState.readonly ? 'outlined' : 'filled'"
                  ></v-text-field>
                </v-col>


                <v-col cols="12" sm="6" class="py-1 d-flex align-center">
                  <v-switch
                    name="status"
                    :label="busModel.status === 'ACTIVE' ? t('ACTIVE') : t('INACTIVE')"
                    v-model="busModel.status"
                    :true-value="Status.ACTIVE"
                    :false-value="Status.INACTIVE"
                    color="green"
                    hide-details
                  ></v-switch>
                </v-col>

                
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, onMounted,computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import Detail from '../../layouts/default/Detail.vue';
import ImageInput from '../../components/common/ImageInput.vue';
import { routeNames } from '../../router/routes.js';
import { required, maxLength, email, password,phone } from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { customerApiResource } from '../../api/resources/customerResource.js';
import { Status } from '../../constants/Status.js';
import { Role } from '../../constants/Role.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Bus, BusModel } from '../../models/BusModel.js';
import { useAuthStore } from '../../store/auth.js';
import { busApiResource } from '../../api/resources/busResource.js';
const route = useRoute();
const router = useRouter();
const { t } = useI18n();

const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const busModel = ref<Bus>(BusModel());
const isUpdate = ref(false);
const authStore=useAuthStore();

const rules = {
  required,
  maxLength,
  email,
  password,
  phone,
};
const roles = ref([
  { id: 1, name: 'Admin' },
  { id: 2, name: 'Customer' },
 
]);
const usernameFieldState = computed(() => {
  const isAdmin = authStore.user.role === 'SYSADMIN';
  if (isAdmin) {
    return { readonly: false, disabled: false };
  }
  if (isUpdate.value) {
    return { readonly: true, disabled: false };
  }
  return { readonly: false, disabled: false };
});
const { call, response, error, status } = useApi();
status.value = ApiStatus.IDLE;

const getDetail = async (id: any) => {
  await call(busApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    busModel.value = response.value?.data as Bus;
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value?.validate() || { valid: false };
  if (!valid) return;

  let apiUrl = busApiResource.register;
  if (isUpdate.value) apiUrl = busApiResource.update;

  busModel.value.name = (busModel.value as any).name;
  busModel.value.status = (busModel.value as any).status;

  const payload = { ...busModel.value };
  if (isUpdate.value) {
    delete (payload as any).password;
    delete (payload as any).confirmPassword;
  }

  await call(apiUrl, { data: payload });

  if (status.value == ApiStatus.SUCCESS) {
    router.go(-1);
  }
};

// Custom breadcrumbs
const breadcrumbs = ref([
  { title: t('General') },
  { title: t('Bus', 2), to: { name: routeNames.busList } },
  { title: t('Detail') },
]);
 const actions: ActionButton[] = [
  {
    icon: mdiArrowLeft,
    label: 'Back',
    onClick: () => {
      router.go(-1);
    },
    color: '',
    useLoading: false,
    useDisabled: false,
  },
  {
    icon: mdiContentSave,
    label: 'Save',
    onClick: onSave,
    color: 'green',
    useLoading: true,
    useDisabled: true,
  },
];

onMounted(() => {
  let { id } = route.params;
  if (id != 'new') {
    isUpdate.value = true;
    getDetail(id);
  }
});

//:readonly="authStore.user.status"

</script>
