<template>

  <v-dialog v-model="dialog" width="auto">
    <v-card>
      <v-card-title class="d-flex">
        <div>
          {{ t('Bus') }}
          <v-icon
            end
            :icon="mdiMagnify"
            @click="showFilters = !showFilters"
            size="24"
          ></v-icon>
        </div>
        <div></div>
        <v-spacer></v-spacer>
        <div class="d-flex" style="gap: 0.5rem">
          <v-btn @click="dialog = false"> {{ t('Close') }} </v-btn>
          <v-btn
            color="green"
            variant="elevated"
            @click="onConfirm"
            :disabled="dialogFormValid === false"
          >
            {{ t('Confirm') }}
          </v-btn>
        </div>
      </v-card-title>

      <div class="px-4">
        <v-slide-y-transition>
          <v-row v-if="showFilters" class="my-1">
            <v-col cols="12" sm="4" md="3">
              <v-text-field
                v-model="filterParams.name"
                density="compact"
                variant="outlined"
                hide-details
                :label="t('Bus Name')"
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="4" md="3">
      <EnumPicker
        v-model:value="filterParams.status"
        :label="t('Status')"
        :enum="{ [t('ACTIVE')]: Status.ACTIVE, [t('INACTIVE')]: Status.INACTIVE }"
        :add-all="true"
        hide-details
        density="compact"
        variant="outlined"
      ></EnumPicker>
    </v-col>
            <v-col cols="12" sm="4" md="3">
              <v-btn
                color="green"
                variant="flat"
                @click="onFilter"
                rounded="xl"
              >
                {{ t('Search') }}
                <v-icon :icon="mdiMagnify" end></v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-slide-y-transition>
      </div>

      <v-card-text style="min-height: 500px; overflow-y: auto" class="pa-0">
        <ListDataTable
          v-bind="busListMeta"
          :api-params="apiParams"
          use-select
          v-model:selected="selected"
          @clear-filter="onClearFilter"
        >
    <template v-slot:[`item.imageUrl`]="{ item }">
            <v-avatar size="40" rounded="lg" class="my-1">
            <v-img
              :src="item.imageUrl || 'https://via.placeholder.com/40'"
              cover
            ></v-img>
          </v-avatar>
        </template>
        <template v-slot:[`item.createdDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>
        <template v-slot:[`item.updatedDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>
        </ListDataTable>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { mdiMagnify } from '@mdi/js';
import { computed, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { busApiResource } from '../../api/resources/busResource';
import { ListMeta } from '../../interfaces/ListMeta';
import { Status } from '../../constants/Status';
import EnumPicker from '../common/EnumPicker.vue';
import {
  BusListParams,
  BusListParamsModel,
} from '../../models/BusModel';
import ListDataTable from '../common/ListDataTable.vue';
import ListDateTime from '../common/ListDateTime.vue';
const { t } = useI18n({ useScope: 'global' });
const selectedIds = computed(() =>
  (props.selected || []).map((i: any) => i.Id),
);

const props = defineProps([
  'id',
  'dialog',
  'selected',
  'params',
]);
const emits = defineEmits(['update:dialog', 'apply:item']);


const dialogFormValid = ref(true);
const selected = ref<any[]>([]);

// apiParams must be undefined
const apiParams = ref();

// search filter
const showFilters = ref(true);
const filterParams = ref<BusListParams>(
  BusListParamsModel(),
);

const busListMeta: ListMeta = {
  headers: [
     { title: t('Image'), key: 'imageUrl', width: 150 },
    {
      title: t('Bus Name'),
      key: 'name',
      width: 200,
      sortable: false,
    },
    { title: t('Status'), key: 'status', width: 150 },
     { title: t('Created Date'), key: 'createdDate', width: 200 },
      { title: t('Created By'), key: 'createdBy', width: 200 },
      { title: t('Updated Date'), key: 'updatedDate', width: 200 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
  ],
  apiResource: busApiResource.getBuses,
  responseKey: 'list',
  defaultSort: [{ key: 'createdBy', order: 'asc' }],
};

const dialog = computed({
  get() {
    return props.dialog;
  },
  set(v) {
    emits('update:dialog', v);
  },
});

watch(dialog, (v) => {
  if (v) {
    apiParams.value = {
      ...props.params,
      id: props.id,
      status:'',
    };
  } else {
    selected.value = [];
    filterParams.value = BusListParamsModel();
    status:'';
  }
});

const onConfirm = () => {
  const filtered = selected.value.filter(
    (i) => !selectedIds.value.includes(i.id),
  );

  emits('apply:item', filtered);
  dialog.value = false;
};

const onFilter = () => {
  apiParams.value = {
    ...filterParams.value,
    ...props.params,
    id: props.id ?? filterParams.value.id,
  };
};

const onClearFilter = () => {
  apiParams.value = {
    ...BusListParamsModel(),
    ...props.params,
    id: props.id ?? null,
  };

  filterParams.value = BusListParamsModel();
}
</script>
