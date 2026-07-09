[7/8/2026 1:34 PM] Yoon Nge:
<template>
  <v-container fluid class="fill-height bg-grey-lighten-4 py-10">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="5">
        <v-card class="elevation-4 rounded-xl overflow-hidden">
          <div
            class="bg-#2C5E82 pt-6 pb-12 text-center"
            style="border-radius: 0 0 50% 50%"
          >
            <h2 class="text-white text-h5 mb-2">Edit Profile</h2>
            <v-chip
              color="white"
              text-color="#2C5E82-darken-4"
              class="font-weight-black text-uppercase px-4"
              density="comfortable"
            >
              Role: {{ userModel.role }}
            </v-chip>
          </div>

          <v-card-text class="mt-4 px-8">
            <v-form ref="formRef" @submit.prevent="onSave">
              <div class="d-flex justify-center mb-6">
                <ImageInput
                  v-if="userModel.role === 'STUDENT'"
                  :image-url="userModel.studentImage"
                  v-model="userModel.studentImageUrl"
                  @delete="userModel.studentImageUrl = ''"
                  image-height="180px"
                  image-width="200%"
                  width="200%"
                  class="mx-auto"
                  :label="t('Student Image')"
                ></ImageInput>

                <ImageInput
                  v-else-if="userModel.role === 'OWNER'"
                  :image-url="userModel.ownerImage"
                  v-model="userModel.ownerImage"
                  @delete="userModel.ownerImageUrl = ''"
                  image-height="180px"
                  image-width="200%"
                  width="200%"
                  class="mx-auto"
                  :label="t('Owner Image')"
                ></ImageInput>
              </div>

              <v-text-field
                v-model="userModel.username"
                label="Username"
                density="compact"
                variant="outlined"
                color="#2C5E82"
                :rules="[rules.required, rules.maxLength(255)]"
              ></v-text-field>

              <template v-if="userModel.role === 'STUDENT'">
                <v-text-field
                  v-model="userModel.studentName"
                  label="Student Name"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255)]"
                ></v-text-field>

                <v-text-field
                  v-model="userModel.studentEmail"
                  label="Student Email"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.email]"
                ></v-text-field>

                <v-text-field
                  v-model="userModel.studentPhone"
                  label="Student Phone"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.phone]"
                ></v-text-field>

                <v-text-field
                  v-model="userModel.studentAddress"
                  label="Student Address"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255)]"
                ></v-text-field>
              </template>

              <template v-else-if="userModel.role === 'OWNER'">
                <v-text-field
                  v-model="userModel.ownerName"
                  label="Owner Name"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255)]"
                ></v-text-field>
                [7/8/2026 1:34 PM] Yoon Nge:
                <v-text-field
                  v-model="userModel.ownerEmail"
                  label="Owner Email"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.email]"
                ></v-text-field>

                <v-text-field
                  v-model="userModel.ownerPhone"
                  label="Owner Phone"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255), rules.phone]"
                ></v-text-field>

                <v-text-field
                  v-model="userModel.ownerAddress"
                  label="Owner Address"
                  density="compact"
                  variant="outlined"
                  color="#2C5E82"
                  :rules="[rules.required, rules.maxLength(255)]"
                ></v-text-field>
              </template>

              <div class="text-center my-4">
                <v-btn
                  variant="text"
                  color="#2C5E82-darken-2"
                  @click="goToChangePassword"
                  class="text-none font-weight-bold"
                >
                  Change Password?
                </v-btn>
              </div>

              <v-row>
                <v-col cols="6">
                  <v-btn
                    variant="outlined"
                    color="grey"
                    block
                    rounded="lg"
                    @click="router.back()"
                    class="text-none"
                  >
                    Cancel
                  </v-btn>
                </v-col>
                <v-col cols="6">
                  <v-btn
                    type="submit"
                    color="#2C5E82"
                    block
                    rounded="lg"
                    :loading="loading"
                    class="text-none text-white font-weight-bold"
                  >
                    Save Changes
                  </v-btn>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { userApiResource } from '../api/resources/userResource';
import { useAuthStore } from '../store/auth';
import useApi from '../api/index';
import ImageInput from '../components/common/ImageInput.vue';
import {
  required,
  maxLength,
  email,
  password,
  phone,
} from '../utils/validations.js';
import { User, UserModel } from '../models/UserModel';

const { t } = useI18n();
const formRef = ref<any>(null);
const router = useRouter();
const authStore = useAuthStore();
const loading = ref(false);

const rules = {
  required,
  maxLength,
  email,
  password,
  phone,
};

const { call, response, status } = useApi<any>();

const userModel = ref<User>(UserModel());

onMounted(async () => {
  const userId = authStore.user?.id;
  if (!userId) return;

  await call(userApiResource.getById, null, { id: userId });

  if (status.value === 'success' && response.value) {
    const data = response.value.data;

    userModel.value = data as User;
  }
});

const onSave = async () => {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  loading.value = true;
  userModel.value.password = '';

  try {
    let apiUrl = userApiResource.update;
    await call(apiUrl, { data: userModel.value });
    if (status.value === 'success') {
      alert('Successfully Saved!');

      authStore.user = { ...authStore.user, ...userModel.value };

      if (userModel.value.role === 'OWNER') {
        router.push('/admin');
      } else {
        router.push('/');
      }
    }
  } catch (error) {
    console.error('Save error:', error);
    alert('Failed to save profile. Please try again.');
  } finally {
    loading.value = false;
  }
};

const goToChangePassword = () => {
  router.push('/changePassword');
};
</script>
