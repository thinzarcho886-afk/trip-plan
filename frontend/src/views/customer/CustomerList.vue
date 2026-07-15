 <template>
  <List
    :title="t('Customer', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #filters="{ onSearch, onClose }">
      <CustomerListSearch @search="onSearch" @close="onClose" />
    </template>

    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="customerListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
        :use-mobile="true"
      >

      <template v-slot:item.profileImageUrl="{ item }">
          <v-avatar size="40" rounded="lg" class="my-1">
            <v-img
              :src="item.profileImageUrl || 'https://via.placeholder.com/40'"
              cover
            ></v-img>
          </v-avatar>
        </template>

        <template v-slot:[`item.name`]="{ item }">
          <router-link
            class="text-decoration-none text-green font-weight-bold"
            :to="getDetailRoute(item)"
          >
            {{ item.name }}
          </router-link>
        </template>

        <template v-slot:item.email="{ item }">
          <span>{{ item.email }}</span>
        </template>

        <template v-slot:item.phoneNumber="{ item }">
          <span>{{ item.phoneNumber }}</span>
        </template>

        

        <template v-slot:item.status="{ item }">
          <ListStatus :status="item.status"></ListStatus>
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
import { ListMeta } from '../../interfaces/ListMeta.js';
import List from '../../layouts/default/List.vue';
import { routeNames } from '../../router/routes.js';
import { mdiPlus , mdiPencil} from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import ListStatus from '../../components/common/ListStatus.vue';
import { useI18n } from 'vue-i18n';
import CustomerListSearch from '../../components/customer/CustomerListSearch.vue';
import { CustomerListParams } from '../../models/CustomerModel.js';
import { useAuthStore } from '../../store/auth.js';
import ListDateTime from '../../components/common/ListDateTime.vue';
import { Role } from '../../constants/Role.js';
import { customerApiResource } from '../../api/resources/customerResource.js';
const { t } = useI18n({ useScope: 'global' });
const authStore = useAuthStore();

const customerListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Image'), key: 'profileImageUrl', minWidth: 150 },
      { title: t('Name'), key: 'name', minWidth: 150 },
      { title: t('Email'), key: 'email', minWidth: 150 },
      { title: t('Phone'), key: 'phoneNumber', minWidth: 150 },
      { title: t('Status'), key: 'status', minWidth: 150 },
      { title: t('Created Date'), key: 'createdDate', width: 150 },
      { title: t('Created By'), key: 'createdBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Action'), key: 'action', sortable:false},

    ],
    apiResource: customerApiResource.getCustomers,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const apiParams = ref();

// custom breadcrumbs
const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Customer', 2), to: { name: routeNames.customerList } },
]);

const actions = computed<ActionButton[]>(() => {
  if (!authStore.user?.customerId)
    return [
      {
        icon: mdiPlus,
        label: 'Add Customer',
        to: { name: routeNames.customerDetail, params: { id: 'new' } },
        color: 'primary',
      },
    ];
  else return [];
});

const filters = {
  component: CustomerListSearch,
  onSearch: (params: CustomerListParams) => (apiParams.value ={ ...params,role:Role.CUSTOMER}),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.customerDetail, params: { id: item.id } };
};

const handleSearch = (params: any) => {
  apiParams.value = { ...params};
}
</script>
