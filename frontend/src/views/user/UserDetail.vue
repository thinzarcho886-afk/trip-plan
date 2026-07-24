<template>
  <Detail
    v-bind="{
      title: t('Users'),
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
            <v-col cols="12" md="4">
              <v-text-field
                name="username"
                v-model="userModel.username"
                :rules="[
                  rules.required,
                  rules.minLength(5),
                  rules.maxLength(50),
                ]"
                :label="t('Username')"
                :readonly="!!userModel.id"
              ></v-text-field>
            </v-col>

             <!-- <v-col cols="12" md="4">
              <v-text-field
                name="ownerName"
                v-model="userModel.ownerName"
                :rules="[
                  rules.required,
                  rules.minLength(5),
                  rules.maxLength(50),
                ]"
                :label="t('Owner Name')"
                :readonly="!!userModel.id"
              ></v-text-field>
            </v-col>

             <v-col cols="12" md="4">
              <v-text-field
                name="studentName"
                v-model="userModel.studentName"
                :rules="[
                  rules.required,
                  rules.minLength(5),
                  rules.maxLength(50),
                ]"
                :label="t('Student Name')"
                :readonly="!!userModel.id"
              ></v-text-field>
            </v-col> -->
            <!-- <v-col cols="12" md="4">
              <v-text-field
                name="name"
                v-model="userModel.name"
                :rules="[
                  rules.required,
                  rules.minLength(5),
                  rules.maxLength(50),
                ]"
                :label="t('Customer Name')"
                :readonly="!!userModel.id"
              ></v-text-field>
            </v-col> -->
            <v-col cols="12" md="4">
              <EnumPicker
                v-model:value="userModel.role"
                :label="t('Role')"
                :enum="{ [t('SYSADMIN')]: Role.SYSADMIN, [t('ADMIN')]: Role.ADMIN, [t('CUSTOMER')]: Role.CUSTOMER, }"
                :rules="[rules.required]"
                :readonly="!!userModel.id"
              ></EnumPicker>
            </v-col>
             <v-col
                        cols="12"
                        md="6"
                        sm="4"
                        class="py-1"
                         v-if="userModel.role === Role.CUSTOMER"
                      >
                        <customer-picker
                          v-model:id="userModel.customerId"
                          v-model:name="userModel.customerName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Customer')"
                          :readonly="!!userModel.id"
                        >
                        </customer-picker>
                      </v-col>

           <!-- <v-col cols="12" md="4" v-if="userModel.role && userModel.role == Role.OWNER">
            <user-picker
              v-model:student-id="userModel.studentId"
              v-model:owner-id="userModel.ownerId"
              :label="t('Owner List')"
              :params="{ status: Status.ACTIVE, role: Role.OWNER }"
              :rules="[rules.required]"
              :readonly="!!userModel.id"
           ></user-picker>
          </v-col> -->

          <!-- <v-col cols="12" md="4" v-if="userModel.role && userModel.role == Role.STUDENT">
            <user-picker
              v-model:student-id="userModel.studentId"
              v-model:owner-id="userModel.ownerId"
              :label="t('Student List')"
              :params="{ status: Status.ACTIVE, roleName: Role.STUDENT}"
              :rules="[rules.required]"
              :readonly="!!userModel.id"
           ></user-picker>
          </v-col> -->

            <!-- <v-col cols="12" md="4">
              <v-row>
                <v-col
                  cols="12"
                  sm="6"
                  md="12"
                  v-if="
                    userModel.role && userModel.role != Role.ADMIN
                  "
                >
                  <user-picker
                    v-model:student-id="userModel.studentId"
                    v-model:owner-id="userModel.ownerId"
                    :label="t('User')"
                    :params="{ status: Status.ACTIVE }"
                    :rules="[rules.required]"
                    :readonly="!!userModel.id"
                  ></user-picker>
                </v-col>
              </v-row>
            </v-col> -->


            <v-col v-if="!isUpdate" cols="12" md="4">
              <v-text-field
                name="password"
                v-model="userModel.password"
                :rules="[rules.required]"
                :label="t('Password')"
                :type="showPassword ? 'text' : 'password'"
                :append-inner-icon="showPassword ? mdiEye : mdiEyeOff"
                @click:append-inner="showPassword = !showPassword"
                variant="outlined"
                density="comfortable"
              ></v-text-field>
            </v-col>

            <v-col v-if="!isUpdate" cols="12" md="4">
              <v-text-field
                name="confirmPassword"
                v-model="userModel.confirmPassword"
                :rules="[
                  rules.required,
                  (v) => v == userModel.password || 'Password are not match',
                ]"
                :label="t('Confirm Password')"
                type="password"
                variant="outlined"
                density="comfortable"
              ></v-text-field>
            </v-col>



            <v-col cols="12" md="4" v-if="isUpdate">
              <v-btn :to="goChangePassword()" color="primary" variant="tonal">
                {{ t('Change Password') }}
              </v-btn>
            </v-col>


           <v-col cols="12" sm="6" class="py-1 d-flex align-center">
                  <v-switch
                    name="status"
                    :label="userModel.status === 'ACTIVE' ? t('ACTIVE') : t('INACTIVE')"
                    v-model="userModel.status"
                    :true-value="Status.ACTIVE"
                    :false-value="Status.INACTIVE"
                    color="primary"
                    hide-details
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
import { userApiResource } from '../../api/resources/userResource.js';
import { useRoute, useRouter } from 'vue-router';
import { Role } from '../../constants/Role.js';
import { Status } from '../../constants/Status.js';
import { mdiContentSave, mdiArrowLeft, mdiEyeOff, mdiEye } from '@mdi/js';
import { User, UserModel } from '../../models/UserModel.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import UserPicker from '../../components/user/UserPicker.vue';
import { useI18n } from 'vue-i18n';
import EnumPicker from '../../components/common/EnumPicker.vue';
import CustomerPicker from '../../components/customer/CustomerPicker.vue';
import { useAuthStore } from '../../store/auth.js';
const { t } = useI18n({ useScope: 'global' });
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const isUpdate = ref(false);

const userModel = ref<any>({
  ...UserModel(),
  role: 'SYSADMIN',
  customerId: null,
 // ownerId: null,
});

const rules = {
  required,
  minLength,
  maxLength,
};
const showPassword = ref(false);

const { call, response, error, status } = useApi();

const getDetail = async (id: any) => {
  await call(userApiResource.getById, null, { id });

  if (status.value == ApiStatus.SUCCESS) {
    userModel.value = response.value?.data as User;
    userModel.value.role = userModel.value.role || '';
    userModel.value.name=userModel.value.name || '';
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = userApiResource.save;
  if (isUpdate.value) apiUrl = userApiResource.update;

  const payload = { ...userModel.value };
  if (isUpdate.value) {
    delete (payload as any).password;
    delete (payload as any).confirmPassword;
  }

  await call(apiUrl, { data: userModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    router.push({ name: routeNames.userList }).catch(() => {});
  }
};

type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('System') },
  { title: t('Users'), to: { name: routeNames.userList } },
  { title: t('Detail') },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => {
      router.push({ name: routeNames.userList }).catch(() => {});
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
  if (id != 'new'){
    isUpdate.value = true;
  getDetail(id);
  }
});

const goChangePassword = () => ({
  name: routeNames.userChangePassword,
  params: { id: route.params.id },
});
</script>
