<template>
  <v-card
    :loading="status == ApiStatus.LOADING"
    :disabled="status == ApiStatus.LOADING"
    rounded="lg"
    elevation="2"
    style="font-size: 15px"
  >
    <v-data-table
      density="comfortable"
      v-model:sort-by="pageable.sortBy"
      :headers="headers"
      :items="items"
      :items-per-page="pageable.itemsPerPage"
      :items-length="pageable.totalItemsLength"
      :must-sort="true"
      :select-strategy="selectStrategy"
      :mobile="
        xs && typeof props.useMobile != 'undefined' && props.useMobile
          ? true
          : false
      "
      @update:options="tableOptions = $event"
      hide-default-footer
      fixed-header
      :height="status == ApiStatus.ERROR && error.message ? '65vh' : '72vh'"
      :show-select="typeof props.useSelect != 'undefined' || props.useSelect"
      v-model="selectedItems"
      return-object
      :show-expand="typeof props.useExpand != 'undefined' || props.useExpand"
      :expand-on-click="
        (typeof props.useExpand != 'undefined' || props.useExpand) &&
        (typeof props.expandOnClick != 'undefined' || props.expandOnClick)
      "
      @update:expanded="emits('update:expanded', $event)"
    >
      <!-- handle dynamic slot pass to child from parent -->
      <!-- https://v3-migration.vuejs.org/breaking-changes/slots-unification.html#slots-unification -->
      <template v-for="(index, name) in $slots" v-slot:[name]="data">
        <slot :name="name" v-bind="data"></slot>
      </template>

      <template v-slot:no-data>
        <div class="text-disabled pa-4 text-center">No data</div>
      </template>

      <!-- error alert -->
      <template #top>
        <div class="pb-2" v-if="status == ApiStatus.ERROR && error.message">
          <v-alert
            color="error"
            variant="tonal"
            border="start"
            density="comfortable"
          >
            {{ error.message }}
          </v-alert>
        </div>

        <template v-if="isParamValues">
          <div class="px-3 py-2 d-flex align-center">
            <span v-if="!xs" class="mr-2" style="min-width: 100px">
              {{ t('Searched') }}:
            </span>
            <v-slide-group show-arrows>
              <template
                v-for="([key, value], index) in Object.entries(apiParams)"
                :key="`apiParam-${key}`"
              >
                <!-- omit *Id keys -->
                <v-slide-group-item
                  v-if="
                    !(
                      /Detail/g.test(key) ||
                      /Flag/g.test(key) ||
                      /Id/g.test(key) ||
                      /InMilliSeconds/g.test(key) ||
                      typeof value === 'boolean'
                    ) && !!value
                  "
                >
                  <v-chip size="small" class="mx-1">
                    <span class="font-weight-bold">
                      {{ t(formatLabel(key)) }}
                    </span>
                    : {{ value }}
                  </v-chip>
                </v-slide-group-item>
              </template>
            </v-slide-group>

            <v-chip
              variant="text"
              size="small"
              @click="onFilterClear"
              color="error"
            >
              <v-icon :icon="mdiClose" size="small"></v-icon>
              <span v-if="!xs">Clear</span>
            </v-chip>
          </div>
        </template>
      </template>

      <!-- custom table footer -->
      <template #bottom>
        <v-card elevation="0" :disabled="status == ApiStatus.LOADING">
          <div class="text-right pt-2 d-flex justify-end align-center">
            <div class="d-flex justify-space-between align-center pr-2">
              <span v-if="!xs" class="pr-2"> Items per page: </span>
              <v-select
                :items="pageable.itemsPerPageOption"
                item-title="title"
                item-value="value"
                v-model="pageable.itemsPerPage"
                density="compact"
                variant="outlined"
                hide-details
                class="pa-2"
                :label="xs ? 'size' : ''"
              ></v-select>
            </div>

            <div class="d-flex justify-space-between align-center pr-2">
              <span v-if="!xs">
                {{
                  Math.min(
                    (pageable.page - 1) * pageable.itemsPerPage + 1,
                    pageable.totalItemsLength
                  )
                }}
                -
                {{
                  Math.min(
                    pageable.page * pageable.itemsPerPage,
                    pageable.totalItemsLength
                  )
                }}
                of&nbsp;
              </span>
              {{ pageable.totalItemsLength }}
              <span v-if="xs">&nbsp;Items</span>
            </div>

            <div class="pr-2">
              <v-pagination
                v-model="pageable.page"
                :length="pageable.totalPages"
                :total-visible="1"
              >
                <template #item>
                  <div class="d-flex align-center fill-height">
                    {{ pageable.page }}
                  </div>
                </template>
              </v-pagination>
            </div>
          </div>
        </v-card>
      </template>
    </v-data-table>
  </v-card>
</template>

<script lang="ts" setup>
import { mdiClose } from '@mdi/js';
import { ref, computed, watch } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const props = defineProps([
  'headers',
  'apiResource',
  'apiParams',
  'responseKey',
  'defaultSort',
  'useSelect',
  'selected',
  'useExpand',
  'expandOnClick',
  'useMobile',
  'selectionMode',
]);
const emits = defineEmits([
  'clearFilter',
  'update:selected',
  'update:expanded',
]);

const headers = computed(() => [
  ...props.headers,
  ...((typeof props.useExpand !== 'undefined' &&
    props.useExpand === true &&
    !props.headers.some(
      (header: any) => header.key === 'data-table-expand'
    ) && [{ title: '', key: 'data-table-expand' }]) ||
    []),
]);
const apiResource = ref(props.apiResource);
const apiParams = computed(() => props.apiParams); // auto-updates when the prop changes

const { xs } = useDisplay();

const isParamValues = computed(() => {
  return apiParams.value
    ? Object.entries(apiParams.value).some((i) => !/Id/g.test(i[0]) && !!i[1])
    : false;
});

const pageable = ref({
  page: 1,
  sortBy: [...props.defaultSort],
  itemsPerPage: 25,
  itemsPerPageOption: [
    { title: '25', value: 25 },
    { title: '50', value: 50 },
    { title: '100', value: 100 },
    { title: 'All', value: '' },
  ],
  totalItemsLength: 0,
  totalPages: 0,
});
const tableOptions = ref<any>(null);

const items = ref([]);
const selectedItems = computed({
  get() {
    // Vuetify expects an array even for single selection
    return props.selected || [];
  },
  set(v) {
    if (props.selectionMode === 'single' && v.length > 1) {
      // Keep only the most recently selected item
      emits('update:selected', [v[v.length - 1]]);
    } else {
      emits('update:selected', v);
    }
  },
});
const { call, response, error, status } = useApi();

const onApiCall = async (params: any) => {
  await call(apiResource.value, { params });

  if (status.value == ApiStatus.SUCCESS) {
    const data: any = response.value?.data;
    if (!data) return;

    items.value = data[props.responseKey];

    pageable.value.totalItemsLength = data.totalElements;
    pageable.value.totalPages = data.totalPages;
  }
};

const selectStrategy = computed(() => {
  // If useSelect is false, we don't return a strategy (hides checkboxes)
  if (!props.useSelect) return undefined;

  // Check if single mode is requested
  if (props.selectionMode === 'single') {
    return 'single';
  }

  return 'all'; // Default to multi-select
});

const showSelect = computed(() => {
  if (!props.useSelect) return false;
  return props.selectionMode === 'multi' || !props.selectionMode;
});

// watch table sort, itemPerPage and pagination changes
// and then, set apiParams with list filters
watch(
  [() => pageable.value.page, tableOptions, apiParams],
  async ([page, options, apiParams], [oldPage]) => {
    // check apiParams undefined, to be sure that all search are coming form search components
    if (!tableOptions.value || !apiParams) return;

    if (page === oldPage && page != 1) {
      // on option or apiParams change
      // if on changes, reset page to 1 and call watch again
      pageable.value.page = 1;
      return;
    }

    let sort = '';
    if (options.sortBy && options.sortBy.length && options.sortBy[0]) {
      sort = options.sortBy[0]['key'] + ',' + options.sortBy[0]['order'];
    }

    const queryParams = {
      page:
        pageable.value.page > 0 ? pageable.value.page - 1 : pageable.value.page,
      size: options.itemsPerPage,
      sort,
      ...apiParams,
    };

    onApiCall(queryParams);
  }
);

const formatLabel = (input: string): string => {
  return input
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, (str) => str.toUpperCase())
    .trim();
};

const onFilterClear = () => {
  emits('clearFilter');
};
</script>
