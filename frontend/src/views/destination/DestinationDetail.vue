  <template>
  <Detail
    v-bind="{
      title: t('Destination'),
      loading: status == ApiStatus.LOADING,
      error: status == ApiStatus.ERROR,
      message: status == ApiStatus.ERROR && error.message,
      formValid,
      breadcrumbs,
      actions,
    }"
  >
    <template #form>
      <v-form v-model="formValid" ref="detailFormRef" @submit.prevent>
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-text-field
                name="name"
                v-model="destinationModel.name"
                :rules="[rules.required, rules.maxLength(100)]"
                :label="t('Destination Name')"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12">
              <v-textarea
                name="description"
                v-model="destinationModel.description"
                :rules="[rules.maxLength(300)]"
                :label="t('Description')"
                rows="5"
              ></v-textarea>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" v-if="destinationModel.id">
              <v-switch
                name="status"
                :label="t(destinationModel.status)"
                v-model="destinationModel.status"
                :true-value="Status.ACTIVE"
                :false-value="Status.INACTIVE"
              ></v-switch>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </template>
  </Detail>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import Detail from '../../layouts/default/Detail.vue';
import { routeNames } from '../../router/routes.js';
import { required, maxLength } from '../../utils/validations.js';
import useApi, { ApiStatus } from '../../api/index.js';
import { useRoute, useRouter } from 'vue-router';
import { Status } from '../../constants/Status.js';
import { mdiContentSave, mdiArrowLeft, mdiRefresh } from '@mdi/js';
import { ActionButton } from '../../interfaces/ActionButton.js';
import { Destination, DestinationModel } from '../../models/DestinationModel.js';
import { useI18n } from 'vue-i18n';
import { useAuthStore } from '../../store/auth.js';
import { Role } from '../../constants/Role.js';
import { destinationApiResource } from '../../api/resources/destinationResource.js';

const { t } = useI18n({ useScope: 'global' });
const route = useRoute();
const router = useRouter();
const formValid = ref(true);
const detailFormRef = ref<null | any>(null);

const initialData = ref<Destination | null>(null);
const destinationModel = ref<Destination>(DestinationModel());

const rules = { required, maxLength };
const { call, response, error, status } = useApi();
const authStore = useAuthStore();

const getDetail = async (id: any) => {
  await call(destinationApiResource.getById, null, { id });
  if (status.value == ApiStatus.SUCCESS) {
    destinationModel.value = response.value?.data as Destination;
    initialData.value = JSON.parse(JSON.stringify(destinationModel.value));
  }
};

const onSave = async () => {
  const { valid } = await detailFormRef.value.validate();
  if (!valid) return;


  let apiUrl = destinationApiResource.register;
  if (destinationModel.value.id) apiUrl = destinationApiResource.update;

  await call(apiUrl, { data: destinationModel.value });

  if (status.value == ApiStatus.SUCCESS) {
    router.push({ name: routeNames.destinationList });
  }
};

// const onReset = () => {
//   if (initialData.value) {
//     // Edit ဆိုရင် မူလ Data ကို ပြန်ယူ
//     destinationModel.value = JSON.parse(JSON.stringify(initialData.value));
//   } else {
//     // Create ဆိုရင် Form ကို ရှင်းထုတ်
//     destinationModel.value = DestinationModel();
//   }
// };

const actions = computed<ActionButton[]>(() => [
  {
    icon: mdiArrowLeft,
    label: t('Back'),
    onClick: () => router.push({ name: routeNames.destinationList }),
    color: '',
  },
  // {
  //   icon: mdiRefresh,
  //   label: t('Reset'),
  //   onClick: onReset,
  //   color: 'warning',
  // },
  {
    icon: mdiContentSave,
    label: t('Save'),
    onClick: onSave,
    color: 'primary',
    useLoading: true,
  },
]);

const breadcrumbs = computed(() => [
  { title: t('General') },
  { title: t('Destination'), to: { name: routeNames.destinationList } },
  { title: t('Detail') },
]);

onMounted(() => {
  let { id } = route.params;
  if (id != 'new') getDetail(id);
});
</script>

