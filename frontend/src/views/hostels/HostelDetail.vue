<template>
  <Detail
    v-bind="{
      title: t('Register Form'),
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
          <v-tabs v-model="tab" density="comfortable">
            <v-tab value="info">
              <v-badge
                :model-value="formValid === false"
                dot
                floating
                color="error"
              >
                {{ t('Register Form') }}
              </v-badge>
            </v-tab>
            <v-tab value="roomDetail">
              {{ t('Choose Rooms') }}
            </v-tab>
          </v-tabs>

          <v-window v-model="tab" :touch="false">
            <v-window-item value="info">
              <v-container fluid class="pa-0">
                <v-row>
                  <v-col
                    cols="12"
                    md="4"
                    class="d-flex justify-center align-start mt-2"
                  >
                    <v-card
                      variant="outlined"
                      class="pa-4 w-100 pt-3"
                      rounded="lg"
                      style="border-style: dashed; background-color: #f8f9fa"
                    >
                      <ImageInput
                        :image-url="hostelModel.hostelImage"
                        v-model="hostelModel.hostelImageUrl"
                        @delete="hostelModel.hostelImageUrl = null"
                        image-height="180px"
                        image-width="100%"
                        width="100%"
                        class="mx-auto"
                        :label="t('Hostel Image')"
                        
                      ></ImageInput>
                    </v-card>
                  </v-col>

                  <v-col cols="12" md="8">
                    <v-row dense>
                      <v-col cols="12" sm="6" class="pt-3">
                        <v-text-field
                          name="hostelName"
                          v-model="hostelModel.hostelName"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Hostel Name')"
                          
                          density="comfortable"
                         
                        ></v-text-field>
                      </v-col>

                      <v-col
                        cols="12"
                        sm="6"
                        class="py-1"
                        v-if="authStore.userRole != Role.OWNER"
                      >
                        <owner-picker
                          v-model:owner-id="hostelModel.userId"
                          v-model:owner-name="hostelModel.userName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Owner')"
                          
                        >
                        </owner-picker>
                      </v-col>

                      <v-col cols="12" sm="6" class="pt-3">
                        <v-select
                          v-model="hostelModel.gender"
                          :items="['MALE', 'FEMALE']"
                          :label="t('Gender')"
                          density="comfortable"
                         
                        ></v-select>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="description"
                          v-model="hostelModel.description"
                          :rules="[rules.required, rules.maxLength(100)]"
                          :label="t('Description')"
                         
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="description"
                          v-model="hostelModel.latitude"
                          :rules="[rules.required]"
                          :label="t('Latitude')"
                         
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <v-text-field
                          name="description"
                          v-model="hostelModel.longitude"
                          :rules="[rules.required]"
                          :label="t('Longitude')"
                         
                          density="comfortable"
                          
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <v-select
                          v-model="hostelModel.hostelStatus"
                          :items="[
                            { title: t('AVAILABLE'), value: 'AVAILABLE' },
                            { title: t('UNAVAILABLE'), value: 'UNAVAILABLE' },
                          ]"
                          :label="t('Hostel Status')"
                          
                          density="comfortable"
                          
                        ></v-select>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <v-select
                          v-model="hostelModel.status"
                          :items="[
                            { title: t('ACTIVE'), value: 'ACTIVE' },
                            { title: t('INACTIVE'), value: 'INACTIVE' },
                          ]"
                          :label="t('Status')"
                          
                          density="comfortable"
                         
                        ></v-select>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <city-picker
                          v-model:city-id="hostelModel.cityId"
                          v-model:city-name="hostelModel.cityName"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('City')"
                         
                        >
                        </city-picker>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <township-picker
                          v-model:township-id="hostelModel.townshipId"
                          v-model:township-name="hostelModel.townshipName"
                          :city-id="hostelModel.cityId"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Township')"
                          
                        >
                        </township-picker>
                      </v-col>

                      <v-col cols="12" sm="6" class="py-1">
                        <street-picker
                          v-model:street-id="hostelModel.streetId"
                          v-model:street-name="hostelModel.streetName"
                          :township-id="hostelModel.townshipId"
                          :params="{ status: Status.ACTIVE }"
                          :label="t('Street')"
                         
                        >
                        </street-picker>
                      </v-col>
                    </v-row>
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <v-window-item value="roomDetail">
              <v-container fluid>
                <v-row>
                  <v-col cols="12">
                    <RoomDetailList
                      v-model="hostelModel.roomList"
                      v-model:formValid="detailListForm"
                      :id="hostelModel.id"
                      :title="t('Room')"
                      @update:delete="hostelModel.deleteRoomDetailIds = $event"
                    ></RoomDetailList>
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>
          </v-window>
        </v-container>
      </v-form>
    </template>
  </Detail>
</template>
<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import {
  required,
  maxLength,
  email,
  password,
} from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { hostelApiResource } from '../../api/resources/hostelResource.js';
import { roomApiResource } from '../../api/resources/roomResource.js';
import { useRoute, useRouter } from 'vue-router';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Hostel, HostelModel } from '../../models/HostelModel.js';
import ImageInput from '../../components/common/ImageInput.vue';
import { useI18n } from 'vue-i18n';
import RoomDetailList from '../../components/room/roomDetailList.vue';
import RoomList from '../room/RoomList.vue';
import CityPicker from '../../components/city/CityPicker.vue';
import TownshipPicker from '../../components/township/TownshipPicker.vue';
import StreetPicker from '../../components/street/StreetPicker.vue';
import OwnerPicker from '../../components/owner/OwnerPicker.vue';
import { Status } from '../../constants/Status.js';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
const isAdmin = computed(() => authStore.user?.role === 'SYSADMIN');
const isReadOnlyForOwner = computed(() => {
  return isEditMode.value && !isAdmin.value;
});
const isEditMode = computed(
  () =>
    !!route.params.id &&
    route.params.id !== 'new' &&
    route.params.id !== undefined,
);
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const tab = ref<string | null>(null);
const detailListForm = ref(true);
const detailFormRef = ref<null | any>(null);
const hostelModel = ref<Hostel>(HostelModel());
const { t } = useI18n();
const isUpdate = ref(false);
const rules = {
  required,
  maxLength,
  email,
  password,
};
const roomList = ref([]);
const cityList = ref([]);
const townshipList = ref([]);
const streetList = ref<any[]>([]);

const { call, response, error, status } = useApi();
status.value = ApiStatus.IDLE;

const getRoomListFromDb = async () => {
  await call(roomApiResource.getRooms);

  if (status.value === ApiStatus.SUCCESS) {
    RoomList.value = response.value?.data?.list || [];
  }
};

const getDetail = async (id: any) => {
  await call(hostelApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    hostelModel.value = response.value?.data;
    if (!hostelModel.value.rooms) {
      hostelModel.value.rooms = [];
    }
  }
};

const onSave = async () => {
  const { valid } = (await detailFormRef.value?.validate()) || { valid: false };
  if (!valid) return;

  let apiUrl = hostelApiResource.save;
  if (hostelModel.value.id) apiUrl = hostelApiResource.update;

  hostelModel.value.status = 'INACTIVE';

  if (!hostelModel.value.hostelStatus)
    hostelModel.value.hostelStatus = 'AVAILABLE';

  const cleanData = {
    ...hostelModel.value,
    roomList:
      hostelModel.value.roomList === '' ? [] : hostelModel.value.roomList,
    deleteRoomDetailIds:
      hostelModel.value.deleteRoomDetailIds === ''
        ? []
        : hostelModel.value.deleteRoomDetailIds,
  };

  await call(apiUrl, { data: cleanData });

  if (status.value == ApiStatus.SUCCESS) {
    router.push({ name: routeNames.hostelList });
  }
};

const removeRoom = (index: number) => {
  hostelModel.value.rooms.splice(index, 1);
};

// const onReset = () => {
//   detailFormRef.value?.reset();
//   hostelModel.value.rooms = [];
//   hostelModel.value.hostelStatus = 'UNAVAILABLE';
// };

const breadcrumbs = ref([
  { title: t('General') },
  { title: t('Hostel', 2), to: { name: routeNames.hostelList } },
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

  // {
  //   icon: mdiRefresh,
  //   label: 'Reset',
  //   onClick: onReset,
  //   color: 'warning',
  //   useLoading: false,
  //   useDisabled: false,
  // },
  {
    icon: mdiContentSave,
    label: 'Save',
    onClick: onSave,
    color: 'primary',
    useLoading: true,
    useDisabled: true,
  },
];
watch(
  () => hostelModel.value.roomList,
  (newRoomList) => {
    if (Array.isArray(newRoomList) && newRoomList.length > 0) {
      const hasAvailableRoom = newRoomList.some(
        (room: any) =>
          room.roomStatus === 'AVAILABLE' || room.room_status === 'AVAILABLE',
      );

      hostelModel.value.hostelStatus = hasAvailableRoom
        ? 'AVAILABLE'
        : 'UNAVAILABLE';
    } else {
      hostelModel.value.hostelStatus = 'UNAVAILABLE';
    }
  },
  { deep: true },
);

onMounted(async () => {
  await getRoomListFromDb();
  if (authStore.userRole == Role.OWNER) {
    hostelModel.value.userId = authStore.user.id;
  }
  let { id } = route.params;
  if (id != 'new') {
    getDetail(id);
  } else {
    hostelModel.value.rooms = [];
  }
});
</script>
