<template>
  <v-container class="mt-5">
    <v-card elevation="6" rounded="lg" class="overflow-hidden">
      <!-- Carousel / Slide Show -->
      <v-carousel
        cycle
        height="260"
        hide-delimiter-background
        show-arrows="hover"
        interval="4000"
      >
        <v-carousel-item
          v-for="(slide, i) in bannerSlides"
          :key="i"
          :src="slide.image"
          cover
        >
          <!-- Image ပေါ်မှာ စာတန်းမြင်သာအောင် Dark Overlay အုပ်ထားခြင်း -->
          <div
            class="d-flex align-center justify-center fill-height"
            style="background: rgba(0, 0, 0, 0.4);"
          >
            <h2 class="text-h4 font-weight-bold text-white text-center px-4">
              {{ t(slide.title) }}
            </h2>
          </div>
        </v-carousel-item>
      </v-carousel>
    </v-card>
  </v-container>

  <v-container>
    <div class="d-flex justify-space-between align-center my-4">
      <h3 class="text-h6 font-weight-bold">{{ t('Top Searched Hostels') }}</h3>
      <v-btn
        variant="flat"
        color="#06402B"
        rounded="lg"
        @click="goToDetails()"
      >
        {{ t('View All') }}
      </v-btn>
    </div>

    <!-- Loading State -->
    <v-row v-if="status == ApiStatus.LOADING" class="justify-center py-10">
      <v-progress-circular
        indeterminate
        color="#2C5E82"
      ></v-progress-circular>
    </v-row>

    <!-- Hostels Grid List -->
    <v-row v-else-if="hostelList.length > 0">
      <v-col
        v-for="(item, index) in hostelList"
        :key="item.id || index"
        cols="12"
        sm="6"
        md="2"
        lg="2.4"
      >
        <v-card class="pa-2 text-center" elevation="2" rounded="lg">
          <v-img
            :src="item.image || 'https://via.placeholder.com/200x150'"
            height="120"
            cover
            class="rounded-lg mb-2"
          ></v-img>

          <div class="text-subtitle-2 font-weight-bold mb-2">
            {{ item.name }}
          </div>

          <v-btn
            size="small"
            variant="outlined"
            color="primary"
            rounded="pill"
            @click="goToDetails(item.id)"
          >
            {{ t('View Detail') }}
          </v-btn>
        </v-card>
      </v-col>
    </v-row>

    <!-- Empty State -->
    <v-row v-else class="justify-center py-10">
      <div class="text-grey text-subtitle-1">
        {{ t('No hostel data found') }}
      </div>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import useApi, { ApiStatus } from '../api';

const { t } = useI18n();
const router = useRouter();

// Slide Show အတွက် ပုံများနှင့် စာတန်းများ
const bannerSlides = ref([
  {
    image: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=1200',
    title: 'Find the perfect  hotel for your stay',
  },
  {
    image: 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=1200',
    title: 'Plan Your Dream Trip With Budget',
  },
  {
    image: 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?q=80&w=1200',
    title: 'Comfortable & Affordable Stay for travellers',
  },
]);

// Dummy Data
const hostelList = ref([
  { id: 1, name: 'Pyin Oo Lwin', image: 'https://picsum.photos/200/150?random=1' },
  { id: 2, name: 'Kalaw', image: 'https://picsum.photos/200/150?random=2' },
  { id: 3, name: 'Taunggyi', image: 'https://picsum.photos/200/150?random=3' },
  { id: 4, name: 'Inle', image: 'https://picsum.photos/200/150?random=4' },
  { id: 5, name: 'Chaung Thar', image: 'https://picsum.photos/200/150?random=5' },
]);

const goToDetails = (id?: number) => {
  router.push({
    name: 'routeNames.hostelPublicList',
    params: id ? { id } : {},
  });
};

const { status } = useApi();
</script>