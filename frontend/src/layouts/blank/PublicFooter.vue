<template>
  <v-footer class="bg-grey-lighten-5 border-t pa-0" style="display: block;">
    <v-container class="py-10">
      <v-row>
        <v-col cols="12" md="4" class="text-left pr-md-6">
          <div class="d-flex align-center mb-3">
            <v-img
            src="../../assets/logoHome.png"
            max-width="40"
            max-height="40"
            contain
            class="mr-3"
          ></v-img>
            <span class="text-h6 font-weight-black text-purple-darken-4">{{t('Hostel Directory System')}}</span>
          </div>
          
          <p class="text-body-2 text-grey-darken-1 leading-relaxed">
           {{t('Connecting UCSH students with the best hostels')}}
          </p>
        </v-col>

        <v-col cols="12" sm="4" md="2" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{t('Quick Links')}}</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('home')" class="footer-link text-body-2">{{t('Home')}}</a>
            <a href="#" @click.prevent="navigateTo('hostels')" class="footer-link text-body-2">{{t('Hostels')}}</a>
            <a href="#" @click.prevent="navigateTo('room-types')" class="footer-link text-body-2">{{t('Room Types')}}</a>
            <a href="#" @click.prevent="navigateTo('about')" class="footer-link text-body-2">{{t('About Us')}}</a>
          </div>
        </v-col>

        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{t('For Students')}}</div>
          <div class="d-flex flex-column space-y-1">
            <a href="#" @click.prevent="navigateTo('hostels')" class="footer-link text-body-2">{{t('Search Hostels')}}</a>
            <a href="#" @click.prevent="navigateTo('room-types')" class="footer-link text-body-2">{{t('View Room Types')}}</a>
            <a href="#" @click.prevent="navigateTo('about')" class="footer-link text-body-2">{{t('Student Feedback')}}</a>
          </div>
        </v-col>

        <v-col cols="12" sm="4" md="3" class="text-left">
          <div class="text-subtitle-1 font-weight-black text-grey-darken-4 mb-3">{{t('Contact Info')}}</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">{{t('Hinthada,Myanmar')}}</div>
          <div class="text-body-2 text-grey-darken-2 mb-1">+959776024994</div>
          <div class="text-body-2 text-grey-darken-2">hnwe16664@gmail.com</div>
        </v-col>
      </v-row>
    </v-container>

    <v-divider></v-divider>
    <!-- <div class="py-4 text-center text-body-2 text-grey-darken-2 bg-grey-lighten-4">
      {{ version }} • {{ new Date().getFullYear() }} —
      <strong>
        <span v-if="!smAndDown">Powered by</span> JUDO ERP Solutions
      </strong>
    </div> -->
  </v-footer>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useDisplay } from 'vuetify'; 
import { version } from '../../../package.json'; 
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

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
