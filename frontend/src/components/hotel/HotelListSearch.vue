<!-- Inside frontend/src/components/hotel/HotelListSearch.vue -->
<template>
  <v-card variant="flat" class="pa-4 bg-grey-lighten-4 rounded-lg">
    <v-row dense>
      <!-- Hotel Name (Like Search text input) -->
      <v-col cols="12" sm="4">
        <v-text-field
          v-model="searchParams.hotelName"
          :label="t('Hotel Name')"
          variant="outlined"
          density="comfortable"
          hide-details
          clearable
        ></v-text-field>
      </v-col>

      <!-- Destination Selection Field -->
      <v-col cols="12" sm="4">
        <v-select
          v-model="searchParams.destinationId"
          :items="destinationList"
          item-title="name"
          item-value="id"
          :label="t('Destination')"
          variant="outlined"
          density="comfortable"
          hide-details
          clearable
        ></v-select>
      </v-col>

      <!-- Status Selection Field (All, Active, Inactive) -->
      <v-col cols="12" sm="4">
        <v-select
          v-model="searchParams.status"
          :items="statusOptions"
          :label="t('Status')"
          variant="outlined"
          density="comfortable"
          hide-details
        ></v-select>
      </v-col>
    </v-row>

    <!-- Form Action Buttons -->
    <v-row class="mt-2 d-flex justify-end pr-3">
      <v-btn color="grey-lighten-1" class="mr-2" @click="handleReset">
        {{ t('Reset') }}
      </v-btn>
      <v-btn color="primary" @click="handleSearch">
        {{ t('Search') }}
      </v-btn>
    </v-row>
  </v-card>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { destinationApiResource } from '../../api/resources/destinationResource.js';

const emit = defineEmits(['search', 'close']);
const { t } = useI18n();

const defaultParams = {
  hotelName: '',
  destinationId: null,
  status: 'ALL' // Default dropdown state
};

const searchParams = ref({ ...defaultParams });
const destinationList = ref([]);

const statusOptions = [
  { title: t('ALL'), value: 'ALL' },
  { title: t('ACTIVE'), value: 'ACTIVE' },
  { title: t('INACTIVE'), value: 'INACTIVE' }
];

const handleSearch = () => {
  // Convert 'ALL' to null or empty string before sending parameters to the backend API if necessary
  const submissionParams = { ...searchParams.value };
  if (submissionParams.status === 'ALL') {
    submissionParams.status = '';
  }
  emit('search', submissionParams);
};

const handleReset = () => {
  searchParams.value = { ...defaultParams };
  emit('search', searchParams.value);
};

onMounted(async () => {
  // Load list items into destination query selection fields
  const res = await destinationApiResource.getActiveDestinations();
  destinationList.value = res?.data || [];
});
</script>