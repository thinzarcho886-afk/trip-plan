<template>
  <List
    :title="t('Destination', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="DestinationListMeta"
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

        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <template v-slot:[`item.updatedDate`]="{ item }">
          {{ item.updatedDateInMilliSeconds ? formatDate(item.updatedDateInMilliSeconds, 'yyyy-MM-dd hh:mm:ss a') : '-' }}
        </template>

        <template v-slot:[`item.action`]="{ item }">
          <v-btn icon size="small" :to="getDetailRoute(item)">
            <v-icon>{{ mdiPencil }}</v-icon>
          </v-btn>
        </template>
      </ListDataTable>
    </template>
  </List>
</template>

<script lang="ts" setup>
import { computed, ref, onMounted } from 'vue';
import { mdiPencil, mdiPlus } from '@mdi/js';
import List from '../../layouts/default/List.vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { routeNames } from '../../router/routes';
import { ListMeta } from '../../interfaces/ListMeta';
import { formatDate } from '../../utils';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import { destinationApiResource } from '../../api/resources/destinationResource';
import { useAuthStore } from '../../store/auth.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { DestinationListParams } from '../../models/DestinationModel.js';
import DestinationListSearch from "../../components/destination/DestinationListSearch.vue"
const authStore = useAuthStore();
const { t } = useI18n({ useScope: 'global' });


const apiParams = ref();


const breadcrumbs = [
  { title: t('Admin'), disabled: false },
  { title: t('Destination'), disabled: true },
];

const actions = computed<ActionButton[]>(() => {
  if (!authStore.user?.destinationId)
  return[
  {
    icon: mdiPlus,
    label: 'Add',
    to: { name: routeNames.destinationDetail, params: { id: 'new' } },
    // onClick: onClickNew
    color: 'primary',
  },
];
else return [];
});

const filters = {
  component: DestinationListSearch,
  onSearch: (params: DestinationListParams) => (apiParams.value = { ...params }),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.destinationDetail, params: { id: item.id } };
};

const DestinationListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Destination Name'), key: 'name' },
      { title: t('Status'), key: 'status' },
      { title: t('Updated Date'), key: 'updatedDate' },
      { title: t('Updated By'), key: 'updatedBy' },
      { title: t('Action'), key: 'action', sortable: false },
    ],
    apiResource: destinationApiResource.getDestinations,
    responseKey: 'list',
    defaultSort: [{ key: 'updatedDate', order: 'desc' }],
  };
});

const clearFilter = () => {
  apiParams.value = { page: 0, size: 25, sort: 'updatedDate,desc' };
};

</script>
