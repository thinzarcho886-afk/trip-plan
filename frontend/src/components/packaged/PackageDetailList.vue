<template>
  

  <v-card variant="outlined" style="border-color: grey !important">
    <v-table density="comfortable" fixed-header height="440">
      <thead>
        <tr>
          <th class="text-left" style="min-width: 200px">{{t("Place Name")}}</th>
          <th class="text-center" style="min-width: 150px">{{t("Photo")}}</th>
          <th class="text-center" style="min-width: 100px">{{t("Action")}}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in packageDetailsList" :key="index">
          <td class="text-left">
            <v-text-field
              v-model="item.placeToVisit"
              variant="plain"
              density="compact"
              hide-details
              :readonly="true"
            ></v-text-field>
          </td>
          <td class="text-center">
            <div class="d-flex justify-center align-center py-2">
              <v-img
                v-if="item.imageUrl"
                :src="item.imageUrl"
                max-height="50"
                max-width="80"
                class="rounded border bg-grey-lighten-4"
                cover
              ></v-img>
              <span v-else class="text-grey text-caption">No Photo</span>
            </div>
          </td>
          <td class="text-center">
            <v-btn
              :icon="mdiClose"
              color="error"
              variant="text"
              density="compact"
              @click="removePackageDetail(index, item)"
            ></v-btn>
          </td>
        </tr>
      </tbody>
    </v-table>
    <div v-if="!packageDetailsList || packageDetailsList.length === 0" class="text-center py-6 text-grey">
      No places added yet.
    </div>
  </v-card>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { mdiClose, mdiPlus } from '@mdi/js';
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const props = defineProps({
  items: {
    type: Array as () => any[],
    default: () => [],
  },
  title: {
    type: String,
    default: '',
  },
  packageId: {
    type: [Number, String],
    default: null,
  },
});

const emit = defineEmits([
  'update:delete',
  'update:items',
  'open-dialog',
]);

const deletePackageDetailIds = ref<number[]>([]);

const packageDetailsList = computed({
  get() {
    return props.items;
  },
  set(v) {
    emit('update:items', v); 
  },
});
const removePackageDetail = (index: number, item: any) => {
  if (item.id) {
    deletePackageDetailIds.value.push(item.id);
    emit('update:delete', deletePackageDetailIds.value);
  }
  
  const updatedList = [...props.items]; 
  updatedList.splice(index, 1);         
  
  emit('update:items', updatedList);
};
</script>