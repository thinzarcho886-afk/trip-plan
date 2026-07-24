<template>
  <List
    :title="t('User', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="userListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
      >
        <!-- Username Link -->
        <template v-slot:[`item.username`]="{ item }">
          <router-link
            class="text-decoration-none text-primary font-weight-bold"
            :to="getDetailRoute(item)"
          >
            {{ item.username }}
          </router-link>
        </template>

        <!-- Role Enum Display -->
        <template v-slot:[`item.role`]="{ item }">
          <ListEnum :value="item.role" :menu="Menu.User"></ListEnum>
        </template>

        <!-- Status Display -->
        <template v-slot:[`item.status`]="{ item }">
          <ListStatus :status="item.status"></ListStatus>
        </template>

        <!-- Created Date -->
        <template v-slot:[`item.createdDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.createdDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- Updated Date -->
        <template v-slot:[`item.updatedDate`]="{ item }">
          <ListDateTime
            :milliseconds="item.updatedDateInMilliSeconds"
          ></ListDateTime>
        </template>

        <!-- Action Pencil Icon -->
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
import { userApiResource } from '../../api/resources/userResource.js';
import ListDataTable from '../../components/common/ListDataTable.vue';
import UserListSearch from '../../components/user/UserListSearch.vue';
import { ListMeta } from '../../interfaces/ListMeta.js';
import List from '../../layouts/default/List.vue';
import { routeNames } from '../../router/routes.js';
import { mdiPlus, mdiPencil } from '@mdi/js';
import { UserListParams } from '../../models/UserModel.js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import { Menu } from '../../constants/EnumMenu.js';
import ListDateTime from '../../components/common/ListDateTime.vue';
import ListEnum from '../../components/common/ListEnum.vue';

const { t } = useI18n({ useScope: 'global' });

const userListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      {
        title: t('Username'),
        align: 'start',
        sortable: true,
        key: 'username',
        width: 200,
      },
      { title: t('Role'), key: 'role', width: 150 },
      { title: t('Status'), key: 'status', width: 150 },
      { title: t('Created Date'), key: 'createdDate', width: 150 },
      { title: t('Created By'), key: 'createdBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Action'), key: 'action', width: 150 },
    ],
    apiResource: userApiResource.getUsers,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const apiParams = ref();

type Breadcrumb = {
  title: string;
  to?: { name: string; params?: Record<string, string | number> };
};

const breadcrumbs = computed<Breadcrumb[]>(() => [
  { title: t('System Management') },
  { title: t('User'), to: { name: routeNames.userList } },
]);

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiPlus,
    label: t('Add'),
    to: { name: routeNames.userDetail, params: { id: 'new' } },
    color: 'primary',
  },
]);

const filters = {
  component: UserListSearch,
  onSearch: (params: UserListParams) => (apiParams.value = { ...params }),
};

// Safety check for ID mapping
const getDetailRoute = (item: any) => {
  const userId = item?.id ?? item?.userId;
  return { name: routeNames.userDetail, params: { id: userId } };
};
</script>