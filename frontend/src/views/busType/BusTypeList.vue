<template>
  <List
    :title="t('Bus Types', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="busTypeListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
      >
        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-primary"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>
         <template v-slot:item.description="{ item }">
          <span>{{ item.description }}</span>
        </template>
        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>
        <template v-slot:[`item.createdDate`]="{ item }">
          {{
            formatDate(item.createdDateInMilliSeconds, 'yyyy-MM-dd hh:mm:ss a')
          }}
        </template>
        <template v-slot:[`item.updatedDate`]="{ item }">
          {{
            formatDate(item.updatedDateInMilliSeconds, 'yyyy-MM-dd hh:mm:ss a')
          }}
        </template>
        <template v-slot:['item.action']="{ item }">
          <div class="d-flex align-items-center">
            <button class="btn btn-outline-primary btn-sm me-2" @click="editbusType(item)">
              &#x270F;
            </button>
         </div>
        </template>
      </ListDataTable>
    </template>
  </List>
</template>
<script lang="ts" setup>
import { computed, ref } from 'vue';
import { mdiPlus } from '@mdi/js';
import List from '../../layouts/default/List.vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { routeNames } from '../../router/routes.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { ListMeta } from '../../interfaces/ListMeta.js';
import { formatDate } from '../../utils/index.js';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import { busTypeApiResource } from '../../api/resources/busTypeResource.js';
import BusTypeListSearch from '../../components/busType/BusTypeListSearch.vue';
import { BusTypeListParams } from '../../models/BusTypeModel.js';
import { useAuthStore } from '../../store/auth.js';
import { useRouter } from 'vue-router';
const { t } = useI18n({ useScope: 'global' });
const authStore = useAuthStore();
const router = useRouter();
type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('General') },
  { title: t('Bus Types'), to: { name: routeNames.busTypeList } },
]);

const actions = computed<ActionButton[]>(() => [
      {
        icon: mdiPlus,
        label: 'Add New',
        to: { name: routeNames.busTypeDetail, params: { id: 'new' } },
        color: 'primary',
      },
]);

const busTypeListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      {
        title: t('Bus Type Name'),
        key: 'name',
        width: 200,
      },
      { title: t('Available Seats'), key: 'availableSeats', width: 150 },
      { title: t('Description'), key: 'description', width: 150 },
      { title: t('Status'), key: 'status', width: 150 },
      { title: t('Created Date'), key: 'createdDate', width: 200 },
      { title: t('Created Date'), key: 'createdDate', width: 150 },
      { title: t('Created By'), key: 'createdBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
    ],
    apiResource: busTypeApiResource.getBusTypes,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const editbusType = (item: any) => {
    console.log("Editing bus type:", item);
};

// apiParams must be undefined
const apiParams = ref();

const getDetailRoute = (item: any) => {
  return { name: routeNames.busTypeDetail, params: { id: item.id } };
};

const filters = {
  component: BusTypeListSearch,
  onSearch: (params: BusTypeListParams) => (apiParams.value = { ...params }),
};
</script>
