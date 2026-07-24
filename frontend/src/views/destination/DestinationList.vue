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
      

        <!-- 2. Destination Name Link -->
        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-primary"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>

        <!-- 3. Status -->
        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <!-- 4. Created Date (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.createdDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 5. Updated Date (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.updatedDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 6. Action Button -->
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
import { computed, ref } from 'vue';
import { mdiPencil, mdiPlus } from '@mdi/js';
import List from '../../layouts/default/List.vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { routeNames } from '../../router/routes';
import { ListMeta } from '../../interfaces/ListMeta';
import ListStatus from '../../components/common/ListStatus.vue';
import ListDateTime from '../../components/common/ListDateTime.vue';
import { useI18n } from 'vue-i18n';
import { destinationApiResource } from '../../api/resources/destinationResource';
import { useAuthStore } from '../../store/auth.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { DestinationListParams } from '../../models/DestinationModel.js';
import DestinationListSearch from "../../components/destination/DestinationListSearch.vue";

const authStore = useAuthStore();
const { t } = useI18n({ useScope: 'global' });

const apiParams = ref();

const breadcrumbs = [
  { title: t('Admin'), disabled: false },
  { title: t('Destination'), disabled: true },
];

const actions = computed<ActionButton[]>(() => {
  if (!authStore.user?.destinationId) {
    return [
      {
        icon: mdiPlus,
        label: 'Add',
        to: { name: routeNames.destinationDetail, params: { id: 'new' } },
        color: 'primary',
      },
    ];
  }
  return [];
});

const filters = {
  component: DestinationListSearch,
  onSearch: (params: DestinationListParams) => (apiParams.value = { ...params }),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.destinationDetail, params: { id: item.id } };
};

// Image Path ချိန်ပေးသည့် Helper Function
const getImageUrl = (url?: string) => {
  if (!url) return 'https://via.placeholder.com/40';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return `http://localhost:8082/${url.startsWith('/') ? url.slice(1) : url}`;
};

const DestinationListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Destination Name'), key: 'name' },
      { title: t('Status'), key: 'status' },
      { title: t('Created Date'), key: 'createdDate' },
      { title: t('Created By'), key: 'createdBy' },
      { title: t('Updated Date'), key: 'updatedDate' },
      { title: t('Updated By'), key: 'updatedBy' },
      { title: t('Action'), key: 'action', sortable: false },
    ],
    apiResource: destinationApiResource.getDestinations,
    responseKey: 'list',
    defaultSort: [{ key: 'updatedDate', order: 'desc' }],
  };
});
</script>