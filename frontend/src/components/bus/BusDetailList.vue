<template>
  <div class="d-flex my-4 align-center">
    <h3>{{ t('Bus Informations') }}</h3>
    <v-btn
      class="mx-3"
      :icon="mdiPlus"
      variant="tonal"
      color="primary"
      @click="busDialog= true"
    ></v-btn>
    <span class="text-red">
      <!-- <template v-if="!props.streetId">{{ t('SELECT STREET') }}</template>
      <template v-else-if="TownshipList.length <= 0">{{
        t('* Detail list is required')
      }}</template> -->
    </span>
  </div>
  <v-card variant="outlined" style="border-color: grey !important">

    <v-form v-model="detailFormValid" ref="detailFormRef">
      <v-table density="comfortable" fixed-header height="550">
        <thead>
          <tr>
            <th class="text-left" style="min-width: 200px">
              {{ t('Bus Image') }}
            </th>
            <th class="text-left" style="min-width: 200px">
              {{ t('Bus Name') }}
            </th>
            <th class="text-left" style="min-width: 200px">
              {{ t('Status') }}
            </th>
            <th class="text-left" style="min-width: 200px">
              {{ t('Created Date') }}
            </th>
            <th class="text-left" style="min-width: 200px">
              {{ t('Created By') }}
            </th>
            <th class="text-left" style="min-width: 180px">
              {{ t('Updated By') }}
            </th>
             <th class="text-left" style="min-width: 180px">
              {{ t('Updated Date') }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(busDetail, index) in busList"
            :key="`poDetail${index}`"
          >
          <td >
            <v-img :src="busDetail.imageUrl || 'http://via.placeholder.com/40'">

            </v-img>
          </td>
            <td class="text-left">{{ busDetail.name }}</td>
            <td class="text-left">{{ busDetail.status }}</td>
            <td class="text-left">{{ formatDate(busDetail.createdDateInMilliSeconds as number) }}</td>
            <td class="text-left">{{ busDetail.createdBy }}</td>
            <td class="text-left">{{ busDetail.updatedBy }}</td>
            <td class="text-left">{{ formatDate(busDetail.updatedDateInMilliSeconds as number) }}</td>
            <td class="text-left">
              <v-btn
                :icon="mdiClose"
                color="error"
                @click="removeBusDetail(index , busDetail)"
                variant="text"
                density="compact"
                :hide-details="props.formValid === false ? false : true"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-form>
  </v-card>

  <BusPickerDialog
    v-model:dialog="busDialog"
    :selected="busList"
    :params="{has:false,status: Status.ACTIVE}"
    @apply:item="onApplyResourceListPicker"
  ></BusPickerDialog>
</template>

<script lang="ts" setup>
import { mdiClose, mdiPlus } from '@mdi/js';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import ImageInput from '../../components/common/ImageInput.vue';
import { required } from '../../utils/validations.js';
import {Bus, BusDetail } from '../../models/BusModel.js';
import BusPickerDialog from '../../components/bus/BusPickerDialog.vue';
import BusList from '../../views/bus/BusList.vue';
import ListDataTable from '../common/ListDataTable.vue';
import { formatDate } from '../../utils/index.js';
import { Status } from '../../constants/Status.js';
const { t } = useI18n({ useScope: 'global' });

const props = defineProps([
  'id',
  'header',
  'modelValue',
  'isEdit',
  'formValid',
]);
const emits = defineEmits([
  'update:delete',
  'update:modelValue',
  'update:formValid',
]);
const deleteBusDetailIds = ref<number[]>([]);
const busDialog = ref(false)

const rules = {
  required,
};
const detailFormValid = computed({
  get() {
    return props.formValid;
  },
  set(v) {
    emits('update:formValid', v);
  },
});
const removeBusDetail = (index, item: BusDetail) => {
  if (item.id) {
    deleteBusDetailIds.value.push(item.id);
    emits('update:delete', deleteBusDetailIds.value);
  }
  busList.value.splice(index, 1);
};
const busList = computed<BusDetail[]>({
  get() {
    return props.modelValue;
  },
  set(v) {
    emits('update:modelValue', v);
  },
});
interface  BusListPicker {
  dialog: boolean;
  selected: Partial<Bus>[];
}

// const removeStreetDetail = (index: number, item: Street) => {
//   if (item.id) {
//     deleteStreetDetailIds.value.push(item.id);
//     emits('update:delete', deleteStreetDetailIds.value);
//   }
//   streetList.value.splice(index, 1);
// };
const busListPicker = ref<BusListPicker>({
  dialog: false,
  selected: [],
});

const onApplyResourceListPicker = (resources: Bus[]) => {
  const newItems = resources.map((rs) => ({
    imageUrl: rs.imageUrl,
    id: rs.id,
    name: rs.name,
    status: rs.status,
    createdDateInMilliSeconds: rs.createdDateInMilliSeconds,
    createdBy: rs.createdBy,
    updatedBy: rs.updatedBy,
    updatedDateInMilliSeconds: rs.updatedDateInMilliSeconds,
  }));

   const uniqueNewItems = newItems.filter(
    (newItem) => !(props.modelValue || []).some((existingItem: any) => existingItem.id === newItem.id)
  );

  const updatedList = [...(props.modelValue || []), ...uniqueNewItems];
  
  emits('update:modelValue', updatedList);
  
  busDialog.value = false;
};
</script>
