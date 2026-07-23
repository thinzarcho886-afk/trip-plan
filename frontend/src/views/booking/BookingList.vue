 <template>
  <List
    :title="t('Booking', 2)"
    :breadcrumbs="breadcrumbs"
    :actions="actions"
    :filters="filters"
  >
    <template #filters="{ onSearch, onClose }">
      <BusListSearch @search="onSearch" @close="onClose" />
    </template>

    <template #datatable="{ clearFilter }">
      <ListDataTable
        v-bind="bookingListMeta"
        :api-params="apiParams"
        @clear-filter="clearFilter"
        :use-mobile="true"
      >

       <template v-slot:item.paymentReceiveImageUrl="{ item }">
          <v-avatar size="40" rounded="lg" class="my-1">
            <v-img
              :src="item.paymentReceiveImageUrl || 'https://via.placeholder.com/40'"
              cover
            ></v-img>
          </v-avatar>
        </template>

        <template v-slot:[`item.packageName`]="{ item }">
          <router-link
            class="text-decoration-none text-green font-weight-bold"
            :to="getDetailRoute(item)"
          >
            {{ item.packageName }}
          </router-link>
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

       <template v-slot:item.departureDate="{ item }">
         <span>{{ item.departureDate ? item.departureDate.split('T')[0] : '' }}</span>
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
import BookingListSearch from '../../components/booking/BookingListSearch.vue';
import { CustomerListParams } from '../../models/CustomerModel.js';
import { useAuthStore } from '../../store/auth.js';
import ListDateTime from '../../components/common/ListDateTime.vue';
import { bookingApiResource } from '../../api/resources/bookingResource.js';
import { BookingListParams } from '../../models/BookingModel.js';
const { t } = useI18n({ useScope: 'global' });
const authStore = useAuthStore();

const bookingListMeta = computed<ListMeta>(() => {
  return {
    headers: [
      { title: t('Payment Receive Image'), key: 'paymentReceiveImageUrl', minWidth: 150 },
     
      { title: t('Package Name'), key: 'packageName', minWidth: 150 },
      { title: t('Customer Name'), key: 'customerName', minWidth: 150 },
      { title: t('Payment Method'), key: 'paymentMethodName', minWidth: 150 },
      { title: t('Travelers Qty'), key: 'travelersQty', minWidth: 150 },
      { title: t('Departure Date'), key: 'departureDate', minWidth: 150 },
      { title: t('Note'), key: 'note', minWidth: 150 },
      { title: t('Status'), key: 'status', minWidth: 150 },   
      { title: t('Created Date'), key: 'createdDate', width: 150 },
      { title: t('Created By'), key: 'createdBy', width: 150 },
      { title: t('Updated Date'), key: 'updatedDate', width: 150 },
      { title: t('Updated By'), key: 'updatedBy', width: 150 },
      { title: t('Action'), key: 'action', sortable:false},

    ],
    apiResource: bookingApiResource.getBookings,
    responseKey: 'list',
    defaultSort: [{ key: 'createdDate', order: 'desc' }],
  };
});

const apiParams = ref();

// custom breadcrumbs
const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Booking', 2), to: { name: routeNames.bookingList } },
]);

const actions = computed<ActionButton[]>(() => {
  if (!authStore.user?.bookingId)
    return [
      {
        icon: mdiPlus,
        label: 'Add Booking',
        to: { name: routeNames.bookingDetail, params: { id: 'new' } },
        color: 'primary',
      },
    ];
  else return [];
});

const filters = {
  component: BookingListSearch,
  onSearch: (params: BookingListParams) => (apiParams.value ={ ...params }),
};

const getDetailRoute = (item: any) => {
  return { name: routeNames.bookingDetail, params: { id: item.id } };
};

const handleSearch = (params: any) => {
  apiParams.value = { ...params};
}
</script>
