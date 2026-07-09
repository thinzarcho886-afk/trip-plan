<template>
  <Detail
    v-bind="{
      title: pageTitle,
      loading: status == ApiStatus.LOADING,
      error: status == ApiStatus.ERROR,
      message:
        (status == ApiStatus.ERROR && error.message) ||
        (status == ApiStatus.SUCCESS && response.data?.message),
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
            <v-col cols="12" md="4" v-if="userId == 'me'">
              <v-text-field
                name="oldPassword"
                v-model="changePasswordModel.oldPassword"
                :rules="[rules.required, rules.minLength(8)]"
                :label="t('Current Password')"
                type="password"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" md="4">
              <v-text-field
                name="newPassword"
                v-model="changePasswordModel.newPassword"
                :rules="[rules.required, rules.minLength(8)]"
                :label="t('New Password')"
                :type="showPassword ? 'text' : 'password'"
                :append-inner-icon="showPassword ? mdiEye : mdiEyeOff"
                @click:append-inner="showPassword = !showPassword"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" md="4">
              <v-text-field
                name="confirmNewPassword"
                v-model="changePasswordModel.confirmNewPassword"
                :rules="[
                  rules.required,
                  (v) =>
                    v == changePasswordModel.newPassword ||
                    t('passwordNotMatch'),
                ]"
                :label="t('Confirm New Password')"
                type="password"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes';
import { required, minLength, maxLength } from '../../utils/validations';
import useApi, { ApiStatus } from '../../api';
import { userApiResource } from '../../api/resources/userResource';
import { useRoute, useRouter } from 'vue-router';
import { mdiContentSave, mdiArrowLeft, mdiEye, mdiEyeOff } from '@mdi/js';
import {
  UserChangePassword,
  UserChangePasswordModel,
} from '../../models/UserModel';
import { ActionButton } from '../../interfaces/ActionButton';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const changePasswordModel = ref<UserChangePassword>(UserChangePasswordModel());
const rules = {
  required,
  minLength,
  maxLength,
};
const showPassword = ref(false);
const pageTitle = ref('User Password Change');
const userId = ref<string | number>();

// custom breadcrumbs
const breadcrumbs = ref<{ [key: string]: any }[] | string[]>();
const actions = ref<ActionButton[]>([]);

// api
const { call, response, error, status } = useApi();

const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = userApiResource.changePasswordForUser;
  if (userId.value == 'me') apiUrl = userApiResource.changePassword;

  await call(apiUrl, { data: changePasswordModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    detailFormRef.value.reset();
    // back?
    if (userId.value == 'me') router.go(-1);
    else
      router.replace({
        name: routeNames.userList,
      });
  }
};

watch(
  () => route.params,
  (v) => {
    const { id } = v;
    if (!id) return;

    // get params
    userId.value = id as string;

    if (userId.value == 'me') {
      if (detailFormRef.value) detailFormRef.value.reset();
      pageTitle.value = 'Change My Password';

      // custom breadcrumbs
      breadcrumbs.value = [];

      actions.value = [
        {
          icon: mdiContentSave,
          label: 'Change',
          onClick: onSave,
          color: 'primary',
          useLoading: true,
          useDisabled: true,
        },
      ];
    } else {
      breadcrumbs.value = [
        { title: 'System' },
        { title: 'Users', to: { name: routeNames.userList } },
        {
          title: 'Detail',
          to: { name: routeNames.userDetail, params: { id: userId.value } },
        },
        { title: 'Change Password' },
      ];

      actions.value = [
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
          label: 'Change',
          onClick: onSave,
          color: 'primary',
          useLoading: true,
          useDisabled: true,
        },
      ];

      changePasswordModel.value.id = parseInt(userId.value);
    }
  },
  { immediate: true }
);
</script>
