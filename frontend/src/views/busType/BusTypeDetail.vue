<template>
  <Detail
    v-bind="{
      title: t('Bus Types'),
      loading: status == ApiStatus.LOADING,
      error: status == ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error.message,
      formValid,
      breadcrumbs,
      actions,
    }"
  >
    <template #form>
      <v-form ref="detailFormRef" v-model="formValid" @submit.prevent="() => {}">
        <v-container>
          <v-tabs v-model="tab" density="comfortable" color="primary">
            <v-tab value="info">
              <v-badge :model-value="formValid === false" dot floating color="error">
                {{ t('Bus Type Informations') }}
              </v-badge>
            </v-tab>
            <v-tab value="BusesDetail" :disabled="!busTypeModel.id">
              {{ t('Buses Detail') }}
            </v-tab>
          </v-tabs>

          <v-window v-model="tab" :touch="false" class="mt-4">
            <v-window-item value="info">
              <v-container>
                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="name"
                      v-model="busTypeModel.name"
                      :rules="[rules.required, rules.maxLength(100)]"
                      :label="t('Name')"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="availableSeats"
                      v-model="busTypeModel.availableSeats"
                      :rules="[rules.required]"
                      :label="t('Available Seats')"
                      density="comfortable"
                      variant="outlined"
                      type="number"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <v-text-field
                      name="description"
                      v-model="busTypeModel.description"
                      :rules="[rules.required, rules.maxLength(100)]"
                      :label="t('Description')"
                      density="comfortable"
                      variant="outlined"
                    ></v-text-field>
                  </v-col>
                </v-row>

                <v-row>
                  <v-col cols="12" md="6" class="py-1">
                    <EnumPicker
                      v-model:value="busTypeModel.status"
                      :label="t('Status')"
                      :enum="{ [t('ACTIVE')]: Status.ACTIVE, [t('INACTIVE')]: Status.INACTIVE }"
                      :rules="[rules.required]"
                    />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <v-window-item value="BusesDetail">
              <v-container fluid>
                <v-row>
                  <v-col cols="12">
                    <BusDetailList
                      v-model="busTypeModel.buses"
                      v-model:formValid="detailListForm"
                      :bus-type-id="busTypeModel.id"
                      :title="t('Bus')"
                      @update:delete="onBusDelete"
                    ></BusDetailList>
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
import { ref, onMounted, computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes';
import { required, minLength, maxLength } from '../../utils/validations';
import useApi, { ApiStatus } from '../../api';
import { useRoute, useRouter } from 'vue-router';
import { Status } from '../../constants/Status';
import { mdiContentSave, mdiArrowLeft } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton';
import { BusType, BusTypeModel } from '../../models/BusTypeModel';
import BusDetailList from '../../components/bus/BusDetailList.vue'; 
import EnumPicker from '../../components/common/EnumPicker.vue';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth';
import { Role } from '../../constants/Role';
import { busTypeApiResource } from '../../api/resources/busTypeResource';

const { t } = useI18n({ useScope: 'global' });
const detailListForm = ref(true);
const tab = ref<string | null>('info');
const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);
const busTypeModel = ref<any>(BusTypeModel());

// ဖျက်ထုတ်လိုက်သော Bus IDs များကို dynamic ယာယီသိမ်းထားရန် Array
const deleteBusIds = ref<any[]>([]);

const rules = {
  required,
  minLength,
  maxLength,
};

const { call, response, error, status } = useApi();
const authStore = useAuthStore();

// Bus Type single data ဆွဲယူရန် လုပ်ဆောင်ချက်
const getDetail = async (id: any) => {
  await call(busTypeApiResource.getById, null, { id });

  if (status.value == ApiStatus.SUCCESS) {
    busTypeModel.value = response.value?.data;
    // အကယ်၍ busType အောက်တွင် buses list မပါခဲ့လျှင် empty string/array mapping အစားထိုးပေးရန်
    if (!busTypeModel.value.buses) {
      busTypeModel.value.buses = [];
    }
  }
};

// Table ထဲမှ dynamic ဖျက်လိုက်သည့်အခါ ID များသိမ်းထားခြင်း
const onBusDelete = (event: any) => {
  deleteBusIds.value = event;
};

// Form Save (ဆောက်ခြင်း နှင့် ပြင်ဆင်ခြင်း)
const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;

  let apiUrl = busTypeApiResource.save;
  if (busTypeModel.value.id) apiUrl = busTypeApiResource.update;

  // ၁။ ပထမဦးစွာ BusType Model အား database ထဲသိမ်းပါမည်
  await call(apiUrl, { data: busTypeModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    const savedBusType = response.value?.data;
    const busTypeId = savedBusType.id || busTypeModel.value.id;

    // ၂။ Table ထဲရှိ Bus အသစ်များကို Transport table တွဲဆက်ပေးရန် API လှမ်းခေါ်ခြင်း
    if (busTypeModel.value.buses && busTypeModel.value.buses.length > 0) {
      for (const bus of busTypeModel.value.buses) {
        // dynamic database creation အတွက် backend loop ပတ်ခေါ်ခြင်း
        await call(busTypeApiResource.addBusToTransport, null, { busTypeId, busId: bus.id }); //
      }
    }

    // ၃။ Mapping ဖြုတ်လိုက်သော Bus များကို Transport Table မှ ပယ်ဖျက်ရန် လုပ်ဆောင်ခြင်း
    if (deleteBusIds.value && deleteBusIds.value.length > 0) {
      for (const busId of deleteBusIds.value) {
        await call(busTypeApiResource.removeBusFromTransport, null, { busTypeId, busId }); //
      }
    }

    router.go(-1);
  }
};

type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('General') },
  { title: t('Bus Type'), to: { name: routeNames.busTypeList } },
  { title: t('Detail') },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => {
      router.push({ name: routeNames.busTypeList }).catch(() => {});
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
  if (authStore.userRole != Role.SYSADMIN) {
    busTypeModel.value.id = authStore.user.id;
  }
  if (id != 'new') {
    getDetail(id);
  }
});
</script>