<template>
  <v-container fluid class="pa-0 gray-bg">
    <v-container class="py-10">
      <v-row align="stretch">
        <!-- 📍 1. Left Side Section (Left Image + Header Titles + Features) -->
        <v-col cols="12" md="6" class="d-flex flex-column justify-space-between">
          <div>
            <!-- Header Banner (Left Side Image & Title) -->
            <div class="d-flex align-center mb-4">
              <v-img
                src="../../assets/images/travel.jpg"
                max-width="110"
                contain
                class="mr-4"
                alt="Left Side Image"
              ></v-img>
              <div>
                <div class="text-subtitle-1 font-weight-bold text-green-darken-2">
                  Budget Friendly
                </div>
                <h1 class="text-h5 font-weight-black text-green-darken-3">
                  Trip Planner......... 📍
                </h1>
                <p class="text-caption font-weight-bold text-grey-darken-1 font-italic mb-0">
                  Travel More, Spend Less, Enjoy Every Moment
                </p>
              </div>
            </div>

            <!-- About Us Heading & Route Graphic -->
            <div class="mb-4">
              <h2 class="text-h5 font-weight-bold text-light-green-darken-3 font-italic">
                ABOUT US
              </h2>
              <v-img
                src="../../assets/images/airplane.jpg"
                max-height="200"
                contain
                class="my-2"
              ></v-img>
            </div>

            <!-- About Us Detail Paragraph -->
            <p class="text-body-2 text-grey-darken-3 leading-relaxed mb-6">
              Budget Friendly Trip Planner is a travel service designed to help you explore amazing destinations without overspending. We provide budget-friendly travel plans, hotel recommendations, and helpful travel trips to make your journey easy, enjoyable, and unforgettable.
            </p>

            <!-- Features Grid (2x2 Layout) -->
            <v-row class="mb-4">
              <v-col 
                v-for="(feature, index) in features" 
                :key="index" 
                cols="12" 
                sm="6"
              >
                <div class="d-flex align-start">
                  <v-avatar color="green-lighten-5" size="36" class="mr-3">
                    <v-icon color="green-darken-2" size="20" :icon="feature.icon"></v-icon>
                  </v-avatar>
                  <div>
                    <h3 class="text-caption font-weight-bold text-green-darken-3 mb-1">
                      {{ feature.title }}
                    </h3>
                    <p class="text-caption text-grey-darken-2 mb-0 leading-tight">
                      {{ feature.description }}
                    </p>
                  </div>
                </div>
              </v-col>
            </v-row>
          </div>

          <!-- Our Mission Footer Box (Left Side Footer) -->
          <v-card 
    class="pa-9 rounded-xl elevation-2 mission-card position-relative overflow-hidden my-4" 
    flat
  >
    <div class="d-flex flex-column flex-sm-row align-center align-sm-start">
      <!-- Icon Container -->
      <v-avatar color="teal-darken-1" size="52" class="elevation-2 mb-3 mb-sm-0 mr-sm-4 flex-shrink-0">
        <v-icon color="white" size="28" :icon="mdiHeart"></v-icon>
      </v-avatar>

      <!-- Text Content Container -->
      <div class="text-center text-sm-left">
        <div class="d-flex align-center justify-center justify-sm-start mb-2">
          <h3 class="text-h6 font-weight-black text-teal-darken-4">
            Our Mission:
          </h3>
          <v-chip color="teal-darken-2" size="x-small" class="ml-2 font-weight-bold" variant="flat">
            GOAL
          </v-chip>
        </div>
        
        <p class="text-body-1 text-grey-darken-3 mb-0 leading-relaxed font-weight-medium">
          To make travel accessible for everyone by offering quality at experiences at budget-friendly prices.
        </p>
      </div>
    </div>
  </v-card>
        </v-col>

        <!-- 📍 2. Right Side Section (Right Side Image + Contact Us Format) -->
        <v-col cols="12" md="6" class="d-flex flex-column justify-space-between pl-md-6">
          <!-- Top Right Side Image -->
          <div class="mb-4">
            <v-img
              src="../../assets/images/family.jpg"
              height="700"
              cover
              class="rounded-xl elevation-2"
              alt="Right Side Image"
            ></v-img>
          </div>

          <!-- Bottom Contact Us Format -->
          <v-card class="pa-4 rounded-xl elevation-1 bg-yellow-lighten-5 border-card">
            <v-row no-gutters align="center">
              <!-- Signpost / Destination Image -->
              <v-col cols="12" sm="5" class="pr-sm-3 mb-3 mb-sm-0">
                <v-img
                  src="../../assets/images/wooden direction.jpg"
                  height="140"
                  cover
                  class="rounded-lg"
                ></v-img>
              </v-col>

              <!-- Contact Us Details -->
              <v-col cols="12" sm="7">
                <v-card class="pa-3 rounded-lg bg-green-lighten-5 elevation-0">
                  <h3 class="text-subtitle-2 font-weight-black text-green-darken-3 mb-2 text-center">
                    CONTACT US
                  </h3>

                  <div class="d-flex align-center mb-1">
                    <v-icon color="green-darken-2" size="16" class="mr-2" :icon="mdiPhone"></v-icon>
                    <span class="text-caption font-weight-medium text-grey-darken-4">+95 9789969582</span>
                  </div>

                  <div class="d-flex align-center mb-1">
                    <v-icon color="green-darken-2" size="16" class="mr-2" :icon="mdiEmail"></v-icon>
                    <span class="text-caption font-weight-medium text-grey-darken-4">hello@travelsmarter.com</span>
                  </div>

                  <div class="d-flex align-start">
                    <v-icon color="green-darken-2" size="16" class="mr-2 mt-1" :icon="mdiMapMarker"></v-icon>
                    <span class="text-caption font-weight-medium text-grey-darken-4 leading-tight">
                      No. 123, Travel Street, Bahan Township, Yangon, Myanmar
                    </span>
                  </div>
                </v-card>
              </v-col>
            </v-row>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { 
  mdiCurrencyUsd, 
  mdiHomeCityOutline, 
  mdiMapSearchOutline, 
  mdiHeadset, 
  mdiHeart, 
  mdiPhone, 
  mdiEmail, 
  mdiMapMarker 
} from '@mdi/js';

const features = ref([
  {
    icon: mdiCurrencyUsd,
    title: 'BUDGET FRIENDLY',
    description: 'Best travel options that fit your budget.'
  },
  {
    icon: mdiHomeCityOutline,
    title: 'TRUSTED STAYS',
    description: 'Carefully selected hotels that are affordable and comfortable.'
  },
  {
    icon: mdiMapSearchOutline,
    title: 'CURATED ITINERARIES',
    description: 'Well-planned itineraries to save time and money'
  },
  {
    icon: mdiHeadset,
    title: 'SUPPORT & GUIDANCE',
    description: "We're here to help you before, during ,and after your trip."
  }
]);
</script>

<style scoped>
.gray-bg {
  background-color: #ffffff;
}

.border-card {
  border: 1px solid #e0e0e0 !important;
}

.border-green {
  border: 1px solid #a5d6a7 !important;
}

.leading-relaxed {
  line-height: 1.6;
}

.leading-tight {
  line-height: 1.3;
}
.mission-card {
  background: linear-gradient(135deg, #e8f5e9 0%, #e0f2f1 100%) !important;
  border: 1.5px solid #a5d6a7 !important;
  transition: all 0.3s ease-in-out;
}

.mission-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 137, 123, 0.15) !important;
  border-color: #80cbd2 !important;
}

.leading-relaxed {
  line-height: 1.6;
}
</style>