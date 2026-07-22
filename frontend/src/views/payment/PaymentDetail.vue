 <template>
  <Detail
    v-bind="{
      title: t('Payment Method'),
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
              <v-card variant="outlined" class="pa-4 w-100" rounded="lg" style="border-style: dashed;">
                <ImageInput
                  :image-url="paymentModel.imageUrl"
                  v-model="paymentModel.image"
                  @delete="paymentModel.imageUrl = ''; paymentModel.image = '';"             
                   image-height="180px"
                  image-width="100%"
                  width="100%"
                  class="mx-auto"
                  :label="t('Image')"
                ></ImageInput>
              </v-card>
            </v-col>

            <v-col cols="12" md="8">
              <v-row>
                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="name"
                    v-model="paymentModel.name"
                    :rules="[rules.required, rules.maxLength(100)]"
                    :label="t('Payment Name')"
                    density="comfortable"
                   ></v-text-field>
                </v-col>


                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="accountNumber"
                    v-model="paymentModel.accountNumber"
                    :rules="[rules.required, rules.maxLength(255), rules.numberOnly]"                   
                    :label="t('Account Number')"
                    density="comfortable"
                     ></v-text-field>
                </v-col>


                <v-col v-if="!isUpdate" cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="accountName"
                    v-model="paymentModel.accountName"
                    :rules="[rules.required]"
                    :label="t('Account Name')"
                    density="comfortable"
                     ></v-text-field>
                </v-col>


                <v-col cols="12" sm="6" class="py-1">
                  <v-text-field
                    name="description"
                    v-model="paymentModel.description"
                    :rules="[rules.required,rules.maxLength(255)]"
                    :label="t('Description')"
                    
                    density="comfortable"
                    ></v-text-field>
                </v-col>

                
                <v-col cols="12" sm="6" class="py-1 d-flex align-center">
                  <v-switch
                    name="status"
                    :label="paymentModel.status === 'ACTIVE' ? t('ACTIVE') : t('INACTIVE')"
                    v-model="paymentModel.status"
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
import { PaymentMethod, PaymentMethodModel } from '../../models/PaymentMethodModel.js';
import { useAuthStore } from '../../store/auth.js';
import { paymentMethodApiResource } from '../../api/resources/paymentMethodResource.js';
const route = useRoute();
const router = useRouter();
const { t } = useI18n();

const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const paymentModel = ref<PaymentMethod>(PaymentMethodModel());
const isUpdate = ref(false);
const authStore=useAuthStore();



const rules = {
  required,
  maxLength,
  email,
  password,
   numberOnly: (value: string) => {
    const pattern = /^[0-9]+$/;
    return pattern.test(value) || 'ဂဏန်းသီးသန့်သာ ရိုက်ထည့်ပေးပါ';
  }
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
  await call(paymentMethodApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    paymentModel.value = response.value?.data as PaymentMethod;
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value?.validate() || { valid: false };
  if (!valid) return;

  let apiUrl = paymentMethodApiResource.register;
  if (isUpdate.value) apiUrl = paymentMethodApiResource.update;

  paymentModel.value.name = (paymentModel.value as any).name;
  paymentModel.value.accountNumber = (paymentModel.value as any).accountNumber;
  paymentModel.value.accountName = (paymentModel.value as any).accountName;
  paymentModel.value.description = (paymentModel.value as any).description;

  paymentModel.value.status = (paymentModel.value as any).status;
 paymentModel.value.imageUrl = (paymentModel.value as any).imageUrl;
  const payload = { ...paymentModel.value };
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
  { title: t('Payment Method', 2), to: { name: routeNames.paymentList } },
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
