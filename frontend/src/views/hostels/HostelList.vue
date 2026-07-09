<template>
  <List
    :title="t('Hostel',2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #filters="{ onSearch, onClose }">
      <HostelListSearch @search="onSearch" @close="onClose" />
    </template>

    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="HostelListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
        :use-mobile="true"
      >
        <template v-slot:item.hostelImage="{ item }">
          <v-avatar size="40" rounded="lg" class="my-1">
            <v-img
              :src="item.hostelImage || 'https://via.placeholder.com/40'"
              cover
            ></v-img>
          </v-avatar>
        </template>



        <template v-slot:item.hostelName="{ item }">
          <router-link
            class="text-decoration-none text-primary font-weight-bold"
            :to="getDetailRoute(item)"
          >
            {{ item.hostelName }}
          </router-link>
        </template>

        <template v-slot:item.gender="{ item }">
          <span>{{ item.gender }}</span>
        </template>

        <template v-slot:item.description="{ item }">
          <span>{{ item.description }}</span>
        </template>

        <template v-slot:item.cityName="{ item }">
          <span>{{ item.cityName }}</span>
        </template>

        <template v-slot:item.townshipName="{ item }">
          <span>{{ item.townshipName }}</span>
        </template>

        <template v-slot:item.streetName="{ item }">
          <span>{{ item.streetName }}</span>
        </template>

        <template v-slot:item.hostelStatus="{ item }">
          <ListStatus :status="item.hostelStatus"></ListStatus>
        </template>

        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

       <template v-slot:item.latitude="{ item }">
        <span>{{ Number(item.latitude).toFixed(6) }}</span>
      </template>

      <template v-slot:item.longitude="{ item }">
        <span>{{ Number(item.longitude).toFixed(6) }}</span>
      </template>


        <template v-slot:item.createdDate="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <template v-slot:item.updatedDate="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <template v-slot:item.actions="{ item }">
          <v-btn
            icon
            variant="text"
            size="small"
            color="primary"
            :to="getDetailRoute(item)"
          >
           <v-icon :icon="mdiPencil"></v-icon>
          </v-btn>
        </template>
      </ListDataTable>
    </template>
  </List>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { hostelApiResource } from '../../api/resources/hostelResource.js';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { ListMeta } from '../../interfaces/ListMeta.js';
import List from '../../layouts/default/List.vue';
import { routeNames } from '../../router/routes.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import HostelListSearch from '../../components/hostel/HostelListSearch.vue';
import { HostelListParams } from '../../models/HostelModel.js';
import { useAuthStore } from '../../store/auth.js';
import ListDateTime from '../../components/common/ListDateTime.vue';
import { mdiPlus, mdiContentSave, mdiPencil } from '@mdi/js';


const { t } = useI18n({ useScope: 'global' });
const authStore = useAuthStore();

const HostelListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Image'), key: 'hostelImage', minWidth: 100, sortable: false },
      { title: t('Hostel Name'), key: 'hostelName', minWidth: 150 },
      { title: t('Gender'), key: 'gender', minWidth: 100 },
      { title: t('Description'), key: 'description', minWidth: 180 },
      { title: t('City Name'), key: 'cityName', minWidth: 130 },
      { title: t('Township Name'), key: 'townshipName', minWidth: 130 },
      { title: t('Street Name'), key: 'streetName', minWidth: 130 },
      { title: t('Hostel Status'), key: 'hostelStatus', minWidth: 120 },
      { title: t('Status'), key: 'status', minWidth: 120 },
      { title: t('Latitude'), key: 'latitude', minWidth: 120 },
      { title: t('Longitude'), key: 'longitude', minWidth: 120 },
      { title: t('Created Date'), key: 'createdDate', minWidth: 180 },
      { title: t('Created By'), key: 'createdBy', minWidth: 180 },
      { title: t('Updated Date'), key: 'updatedDate', minWidth: 180 },
      { title: t('Updated By'), key: 'updatedBy', minWidth: 180 },
    ],
    apiResource: hostelApiResource.getList,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const apiParams = ref();
const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Hostel',2), to: { name: routeNames.hostelList } },
]);

const actions = computed<ActionButton[]>(() => {
  if (!authStore.user?.HostelId)
    return [
      {
        icon: mdiPlus,
        label: 'Add New',
        to: { name: routeNames.hostelDetail, params: { id: 'new' } },
        color: 'primary',
      },
    ];
  else return [];
});

const filters = {
  component: HostelListSearch,
  onSearch: (params: HostelListParams) => (apiParams.value = { ...params}),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.hostelDetail, params: { id: item.id } };
};

const handleSearch = (params: any) => {
  apiParams.value = { ...params };
};
</script>
