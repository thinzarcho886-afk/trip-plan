<template>
  <List
    :title="t('Hotel', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #filters="{ onSearch, onClose }">
      <HotelListSearch @search="onSearch" @close="onClose" />
    </template>

    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="hotelListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
        :use-mobile="true"
      >
        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-primary font-weight-bold"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>

        <!-- Dynamic Destination Name display -->
        <template v-slot:item.destinationName="{ item }">
          <span>{{ item.destinationName }}</span>
        </template>

        <!-- Status Chip Component -->
        <template v-slot:item.status="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <!-- Dynamic Date Formatting -->
        <template v-slot:item.updatedDate="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
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
import { computed, ref } from 'vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { ListMeta } from '../../interfaces/ListMeta.js';
import List from '../../layouts/default/List.vue';
import { routeNames } from '../../router/routes.js';
import { mdiPlus, mdiPencil } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ListStatus from '../../components/common/ListStatus.vue';
import ListDateTime from '../../components/common/ListDateTime.vue';
import HotelListSearch from '../../components/hotel/HotelListSearch.vue'; 
import { useI18n } from 'vue-i18n';
import { hotelApiResource } from '../../api/resources/hotelResource.js';

const { t } = useI18n({ useScope: 'global' });
const apiParams = ref();

const hotelListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Hotel Name'), key: 'name', minWidth: 150 },
      { title: t('Address'), key: 'address', minWidth: 150 },
      { title: t('Destination Name'), key: 'destinationName', minWidth: 150 },
      { title: t('Price Per Night'), key: 'pricePerNight', minWidth: 120 },
      { title: t('Description'), key: 'description', minWidth: 200 },
      { title: t('Status'), key: 'status', minWidth: 100 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Action'), key: 'action',sortable: false },
    ],
    apiResource: hotelApiResource.getList,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Hotel', 2), to: { name: routeNames.hotelList } },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiPlus,
    label: t('Add Hotel'),
    to: { name: routeNames.hotelDetail, params: { id: 'new' } },
    color: 'primary',
  },
]);

const filters = {
  component: HotelListSearch,
  onSearch: (params: any) => (apiParams.value = { ...params }),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.hotelDetail, params: { id: item.id } };
};
</script>