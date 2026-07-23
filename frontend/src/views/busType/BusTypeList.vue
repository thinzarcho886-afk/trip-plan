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
        <!-- 1. Image Slot (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.imageUrl`]="{ item }">
          <v-avatar size="40" rounded="lg" class="my-1">
            <v-img
              :src="getImageUrl(item.imageUrl)"
              cover
            >
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height bg-grey-lighten-2">
                  <v-icon size="20" color="grey">mdi-image-off</v-icon>
                </div>
              </template>
            </v-img>
          </v-avatar>
        </template>

        <!-- 2. Bus Type Name Link Slot -->
        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-primary"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>

        <!-- 3. Description Slot -->
        <template v-slot:[`item.description`]="{ item }">
          <span>{{ item.description }}</span>
        </template>

        <!-- 4. Status Slot -->
        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <!-- 5. Created Date Slot (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.createdDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 6. Updated Date Slot (Syntax ပြင်ဆင်ထားသည်) -->
        <template v-slot:[`item.updatedDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- 7. Action Slot (တစ်ခုတည်းသာ ချန်လှပ်ထားသည်) -->
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

// Helper for Image Handling
const getImageUrl = (url?: string) => {
  if (!url) return 'https://via.placeholder.com/40';
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url;
  }
  return `http://localhost:8082/${url.startsWith('/') ? url.slice(1) : url}`;
};

const busTypeListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Image'), key: 'imageUrl', width: 150 },
      {
        title: t('Bus Type Name'),
        key: 'name',
        width: 200,
      },
      { title: t('Available Seats'), key: 'availableSeats', width: 150 },
      { title: t('Description'), key: 'description', width: 150 },
      { title: t('Status'), key: 'status', width: 150 },
      { title: t('Created Date'), key: 'createdDate', width: 600 }, // ထပ်နေသော Created Date ကို ဖယ်ထုတ်ထားပါသည်
      { title: t('Created By'), key: 'createdBy', width: 600 },
      { title: t('Updated Date'), key: 'updatedDate', width: 600 },
      { title: t('Updated By'), key: 'updatedBy', width: 600 },
      { title: t('Action'), key: 'action', sortable: false },
    ],
    apiResource: busTypeApiResource.getBusTypes,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

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