<template>
  <v-footer class="bg-grey-lighten-5 border-t pa-0" style="display: block;">
    <v-container class="py-10">
      <v-row>
        <v-col cols="12" md="4" class="text-left pr-md-6">
          <div class="d-flex align-center mb-3">
            <v-icon color="purple-darken-3" size="32" class="mr-2">mdi-home-account</v-icon>
            <span class="text-h6 font-weight-black text-purple-darken-4">Hostel Directory</span>
          </div>
          <div class="text-subtitle-2 font-weight-bold text-grey-darken-3 mb-2">
            Hostel Directory Management System
          </div>
          <p class="text-body-2 text-grey-darken-1 leading-relaxed">
            Connecting students with the best hostels across Myanmar.
          </p>
        </v-col>

        <v-col cols="12" sm="4" md="2" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">Quick Links</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('home')" class="footer-link text-body-2">Home</a>
            <a href="#" @click.prevent="navigateTo('hostels')" class="footer-link text-body-2">Hostels</a>
            <a href="#" @click.prevent="navigateTo('room-types')" class="footer-link text-body-2">Room Types</a>
            <a href="#" @click.prevent="navigateTo('about')" class="footer-link text-body-2">About Us</a>
          </div>
        </v-col>

        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">For Students</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('hostels')" class="footer-link text-body-2">Search Hostel</a>
            <a href="#" @click.prevent="navigateTo('room-types')" class="footer-link text-body-2">View Room Types</a>
            <a href="#" @click.prevent="navigateTo('about')" class="footer-link text-body-2">Student Feedback</a>
          </div>
        </v-col>

        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">Contact Info</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">Yangon, Myanmar</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">+959123456789</div>
          <div class="text-body-2 text-grey-darken-2">nyeinyielin202@gmail.com</div>
        </v-col>
      </v-row>
    </v-container>

    <v-divider></v-divider>
    <div class="py-4 text-center text-body-2 text-grey-darken-2 bg-grey-lighten-4">
      {{ version }} • {{ new Date().getFullYear() }} — 
      <strong>
        <span v-if="!smAndDown">Powered by</span> JUDO ERP Solutions
      </strong>
    </div>
  </v-footer>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useDisplay } from 'vuetify'; // 💡 Vuetify 3 စတိုင်အတိုင်း clean import ပြင်ထားပါတယ်
import { version } from '../../../package.json'; // မူရင်း package.json မှ version ဆွဲယူမှု

const router = useRouter();
const { smAndDown } = useDisplay();

const navigateTo = (routeName: string) => {
  if (routeName === 'home') router.push('/');
  else if (routeName === 'hostels') router.push('/public-hostels');
  else if (routeName === 'room-types') router.push('/room');
  else if (routeName === 'about') router.push('/about');
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
  color: #4a148c;
  text-decoration: underline;
}
.leading-relaxed {
  line-height: 1.6;
}
</style>