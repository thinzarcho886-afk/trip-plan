<template>
  <List
    :title="t('Floor', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="floorListMeta"
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
      </ListDataTable>
    </template>
  </List>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { mdiPlus } from '@mdi/js';
import List from '../../layouts/default/List.vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { routeNames } from '../../router/routes';
import { ActionButton } from '../../interfaces/ActionButton';
import { ListMeta } from '../../interfaces/ListMeta';
import { formatDate } from '../../utils';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import { floorApiResource } from '../../api/resources/floorResource';
import FloorListSearch from '../../components/floor/FloorListSearch.vue';
import { FloorListParams } from '../../models/FloorModel';

const { t } = useI18n({ useScope: 'global' });

type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('General') },
  { title: t('Floor'), to: { name: routeNames.floorList } },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiPlus,
    label: 'Add',
    to: { name: routeNames.floorDetail, params: { id: 'new' } },
    // onClick: onClickNew
    color: 'primary',
  },
]);

const floorListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      {
        title: t('Floor'),
        key: 'name',
        width: 200,
      },
      { title: t('Company'), key: 'companyName', width: 200 },
      { title: t('Description'), key: 'description', width: 200 },
      { title: t('Status'), key: 'status', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 200 },
    ],
    apiResource: floorApiResource.getList,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

// apiParams must be undefined
const apiParams = ref();

const getDetailRoute = (item: any) => {
  return { name: routeNames.floorDetail, params: { id: item.id } };
};

const filters = {
  component: FloorListSearch,
  onSearch: (params: FloorListParams) => (apiParams.value = { ...params }),
};
</script>
