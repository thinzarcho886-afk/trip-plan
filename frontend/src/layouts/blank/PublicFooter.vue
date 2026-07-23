<template>
  <v-footer class="bg-grey-lighten-5 border-t pa-0" style="display: block;">
    <v-container class="py-10">
      <v-row>
        <!-- Branding Section -->
        <v-col cols="12" md="4" class="text-left pr-md-6">
          <div class="d-flex align-center mb-3">
            <v-icon size="32" color="green-darken-2" class="mr-2" :icon="mdiBeach"></v-icon>
            <div class="d-flex flex-column lh-tight">
              <span class="text-subtitle-1 font-weight-black text-light-green-darken-3 text-uppercase">
                {{ t('Budget Friendly') }}
              </span>
              <span class="text-caption font-weight-bold text-grey-darken-3" style="margin-top: -4px;">
                {{ t('Trip Planner Management System') }}
              </span>
            </div>
          </div>
          <p class="text-body-2 text-grey-darken-1 leading-relaxed">
            {{ t('Plan your dream travel packages with affordable budgets easily.') }}
          </p>
        </v-col>

        <!-- Quick Links -->
        <v-col cols="12" sm="4" md="2" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{ t('Quick Links') }}</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('home')" class="footer-link text-body-2">{{ t('Home') }}</a>
            <a href="#" @click.prevent="navigateTo('about')" class="footer-link text-body-2">{{ t('About Us') }}</a>
            <a href="#" @click.prevent="navigateTo('hotels')" class="footer-link text-body-2">{{ t('Hotels') }}</a>
            <a href="#" @click.prevent="navigateTo('transport')" class="footer-link text-body-2">{{ t('Transport') }}</a>
          </div>
        </v-col>

        <!-- For Travelers / Users -->
        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{ t('For Travellers') }}</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('bookingHistoryPublic')" class="footer-link text-body-2">{{ t('Booking List') }}</a></div>
        </v-col>

        <!-- Contact Info -->
        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{ t('Contact Info') }}</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">{{ t('Hinthada, Myanmar') }}</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">+95945678678</div>
          <div class="text-body-2 text-grey-darken-2">mama@gmail.com</div>
        </v-col>
      </v-row>
    </v-container>

    <v-divider></v-divider>

    <!-- Bottom Copyright -->
    <div class="py-4 text-center text-body-2 text-grey-darken-2 bg-grey-lighten-4">
      © {{ new Date().getFullYear() }} {{ t('Budget Friendly Trip Planner. All rights reserved.') }}
    </div>
  </v-footer>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { mdiBeach } from '@mdi/js';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const router = useRouter();

// AppBar Menu များနှင့် ကိုက်ညီသော Router Navigation
const navigateTo = (routeName: string) => {
  switch (routeName) {
    case 'home':
      router.push('/');
      break;
    case 'about':
      router.push('/about');
      break;
    case 'hotels':
      router.push('/public-hotels');
      break;
    case 'transport':
      router.push('/public-transports');
      break;
    case 'bookingHistoryPublic':
      router.push('/bookingHistoryPublic');
      break;
    case 'login':
      router.push('/login');
      break;
    default:
      router.push('/');
  }
};
</script>

<style scoped>
.footer-link {
  color: #616161;
  text-decoration: none;
  transition: color 0.2s;
  margin-bottom: 6px;
}
.footer-link:hover {
  color: #0288d1;
  text-decoration: underline;
}
.leading-relaxed {
  line-height: 1.6;
}
</style>