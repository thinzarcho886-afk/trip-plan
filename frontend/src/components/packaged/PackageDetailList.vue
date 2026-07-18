<!-- PackageDetailList.vue -->
<template>
  <v-card outlined class="pa-4">
    <v-row>
      <v-col cols="4"><strong>Place</strong></v-col>
      <v-col cols="4"><strong>Upload Photo</strong></v-col>
      <v-col cols="4"><strong>Action</strong></v-col>
    </v-row>

    <v-row v-for="(item, index) in items" :key="index" align="center">
      <v-col cols="4">
        <v-text-field v-model="item.placeToVisit" variant="outlined" density="compact" hide-details></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-file-input label="Choose file" density="compact" hide-details></v-file-input>
      </v-col>
      <v-col cols="4">
        <!-- Delete ခလုတ်ပဲ ကျန်ပါမယ် -->
        <v-btn icon="mdi-close" variant="text" color="red" size="small" @click="items.splice(index, 1)"></v-btn>
      </v-col>
    </v-row>
    <div v-if="!items || items.length === 0" class="text-center py-4">
      No places added yet.
    </div>
  </v-card>
</template>

<!-- PackageDetailList.vue -->
<script setup lang="ts">
// props ကို toRefs သုံးပြီး သေချာဖတ်ပါ
import { toRefs } from 'vue';
const props = defineProps<{ items: any[] }>();
const { items } = toRefs(props);
</script>