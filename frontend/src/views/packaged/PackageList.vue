<template>
  <List
    :title="t('Package', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #filters="{ onSearch, onClose }">
      <PackageListSearch @search="onSearch" @close="onClose" />
    </template>

    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="packageListMeta"
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
        <template v-slot:item.busTypeName="{ item }">
          <span>{{ item.busTypeName }}</span>
        </template>
        <template v-slot:item.busName="{ item }">
          <span>{{ item.busName }}</span>
        </template>
         <template v-slot:item.destinationName="{ item }">
          <span>{{ item.destinationName }}</span>
        </template>
         <template v-slot:item.hotelName="{ item }">
          <span>{{ item.hotelName }}</span>
        </template>
         <template v-slot:item.durationName="{ item }">
          <span>{{ item.durationName }}</span>
        </template>
        <template v-slot:item.transportName="{ item }">
          <span>{{ item.transportName }}</span>
        </template>
        <template v-slot:item.transportFee="{ item }">
          <span>{{ item.transportFee }}</span>
        </template>
        <template v-slot:item.hotelFee="{ item }">
          <span>{{ item.hotelFee }}</span>
        </template>
        <template v-slot:item.serviceFee="{ item }">
          <span>{{ item.serviceFee }}</span>
        </template>
        <template v-slot:item.budgetAmount="{ item }">
          <span>{{ item.budgetAmount }}</span>
        </template>
        <!-- Status Chip Component -->
        <template v-slot:item.status="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>
        <template v-slot:item.extraService="{ item }">
          <span>{{ item.extraService }}</span>
        </template>
        <template v-slot:item.description="{ item }">
          <span>{{ item.description }}</span>
        </template>
        <!-- Dynamic Date Formatting -->
        <template v-slot:item.updatedDate="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- Action Column -->
        <template v-slot:item.actions="{ item }">
          <v-btn
            :icon="mdiPencil"
            variant="text"
            size="small"
            color="primary"
            :to="getDetailRoute(item)"
          ></v-btn>
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
import PackageListSearch from '../../components/packaged/PackageListSearch.vue'; 
import { useI18n } from 'vue-i18n';
import {packageApiResource } from '../../api/resources/packageResource.js';
import BusTypePicker from '../../components/busType/BusTypePicker.vue';
import DurationPicker from '../../components/duration/DurationPicker.vue';
import {Package, PackageModel } from '../../models/PackageModel.js';
const { t } = useI18n({ useScope: 'global' });
const apiParams = ref();
const packageModel = ref<Package>(PackageModel());
const packageListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Package Name'), key: 'name', minWidth: 150 },
      { title: t('Bus Type Name'), key: 'name', minWidth: 150 },
      { title: t('Bus Name'), key: 'name', minWidth: 150 },
      { title: t('Destination'), key: 'destinationName', minWidth: 150 },
      { title: t('Hotel Name'), key: 'hotelName', minWidth: 150 },
      { title: t('Duration'), key: 'duration', minWidth: 150 },
      { title: t('Departure Date'), key: 'departureDate', minWidth: 150 },
      { title: t('Duration'), key: 'durationName', minWidth: 150 },
      { title: t('Transport Fee'), key: 'transportFee', minWidth: 150 },
      { title: t('Hotel Fee'), key: 'hotelFee', minWidth: 150 },
      { title: t('Service Fee'), key: 'serviceFee', minWidth: 150 },
      { title: t('Budget Amount'), key: 'budgetAmount', minWidth: 150 },
      { title: t('Extra Service'), key: 'extraService', minWidth: 150 },
      { title: t('Description'), key: 'description', minWidth: 200 },
      { title: t('Status'), key: 'status', minWidth: 100 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
    //   { title: t('Action'), key: 'actions', width: 100, sortable: false },
    ],
    apiResource: packageApiResource.getPackages,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Package', 2), to: { name: routeNames.packageList } },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiPlus,
    label: t('Add Package'),
    to: { name: routeNames.packageDetail, params: { id: 'new' } },
    color: 'primary',
  },
]);

const filters = {
  component: PackageListSearch,
  onSearch: (params: any) => (apiParams.value = { ...params }),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.packageDetail, params: { id: item.id } };
};
</script>