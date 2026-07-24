<template>
  <List
    :title="t('Duration', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="durationListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
      >
        <!-- 1. Duration Name Link Slot -->
        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-primary"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>

        <!-- 2. Description Slot -->
        <template v-slot:[`item.description`]="{ item }">
          <span>{{ item.description }}</span>
        </template>

        <!-- 3. Status Slot -->
        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <!-- 4. Created Date Slot (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.createdDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 5. Updated Date Slot (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.updatedDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 6. Action Slot (တစ်ခုတည်းသာ ချန်လှပ်ထားသည်) -->
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
import { mdiPlus, mdiPencil } from '@mdi/js';
import List from '../../layouts/default/List.vue';
import ListDataTable from '../../components/common/ListDataTable.vue';
import { routeNames } from '../../router/routes.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { ListMeta } from '../../interfaces/ListMeta.js';
import ListStatus from '../../components/common/ListStatus.vue';
import ListDateTime from '../../components/common/ListDateTime.vue';
import { useI18n } from 'vue-i18n';
import { durationApiResource } from '../../api/resources/durationResource.js';
import DurationListSearch from '../../components/duration/DurationListSearch.vue';
import { DurationListParams } from '../../models/DurationModel.js';
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
  { title: t('Duration'), to: { name: routeNames.durationList } },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiPlus,
    label: 'Add New',
    to: { name: routeNames.durationDetail, params: { id: 'new' } },
    color: 'primary',
  },
]);

const durationListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      {
        title: t('Duration Name'),
        key: 'name',
        width: 200,
      },
      { title: t('Description'), key: 'description', width: 150 },
      { title: t('Status'), key: 'status', width: 150 },
      { title: t('Created Date'), key: 'createdDate', width: 200 }, // ထပ်နေသော Created Date ကို ဖယ်ထုတ်ထားပါသည်
      { title: t('Created By'), key: 'createdBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Action'), key: 'action', sortable: false },
    ],
    apiResource: durationApiResource.getDurations,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

// apiParams must be undefined
const apiParams = ref();

const getDetailRoute = (item: any) => {
  return { name: routeNames.durationDetail, params: { id: item.id } };
};

const filters = {
  component: DurationListSearch,
  onSearch: (params: DurationListParams) => (apiParams.value = { ...params }),
};
</script>