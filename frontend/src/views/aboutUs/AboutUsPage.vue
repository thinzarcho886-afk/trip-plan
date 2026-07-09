<template>
  <v-container fluid class="pa-0 gray-bg">
    <v-container class="py-10">
      <v-row align="center" justify="center">
        <v-col cols="12" md="6" class="text-center">
          <v-img
            src="../../assets/H.jpg"
            max-height="350"
            contain
            class="mx-auto rounded-xl elevation-1"
          ></v-img>
        </v-col>

        <v-col cols="12" md="6" class="text-left px-6">
          <h1 class="text-h4 font-weight-black text-purple-darken-4 mb-4">
            About Hostel Directory<br />System
          </h1>
          <p class="text-body-1 text-grey-darken-3 leading-relaxed mb-6">
            Hostel Directory System is a smart platform that helps
            students find suitable hostels easily and helps hostel owners manage
            their properties, rooms, and information efficiently.
          </p>
          <v-divider class="mb-6 border-opacity-25" color="purple" thickness="2" width="60"></v-divider>

          <div class="d-flex align-center mb-4">
            <v-icon color="purple-darken-3" class="mr-3" size="24" :icon="mdiMagnify"></v-icon>
            <span class="text-body-1 font-weight-medium text-grey-darken-4">Easy search and filter for hostels</span>
          </div>
          <div class="d-flex align-center">
            <v-icon color="purple-darken-3" class="mr-3" size="24" :icon="mdiHomeCityOutline"></v-icon>
            <span class="text-body-1 font-weight-medium text-grey-darken-4">Detailed room type and hostel information</span>
          </div>
        </v-col>
      </v-row>
    </v-container>

    <v-container class="py-6">
      <v-row>
        <v-col cols="12" md="4">
          <v-card class="pa-6 rounded-xl elevation-2 text-center h-100 border-card">
            <v-avatar color="purple-lighten-5" size="64" class="mb-4">
              <v-icon color="purple-darken-3" size="32" :icon="mdiTarget"></v-icon>
            </v-avatar>
            <h3 class="text-h6 font-weight-bold text-purple-darken-4 mb-2">Our Mission</h3>
            <p class="text-body-2 text-grey-darken-2">
              To provide a reliable and user-friendly platform that connects students with the best hostel options and helps hostel owners grow their management efficiency.
            </p>
          </v-card>
        </v-col>

        <v-col cols="12" md="4">
          <v-card class="pa-6 rounded-xl elevation-2 text-center h-100 border-card">
            <v-avatar color="purple-lighten-5" size="64" class="mb-4">
              <v-icon color="purple-darken-3" size="32" :icon="mdiEyeOutline"></v-icon>
            </v-avatar>
            <h3 class="text-h6 font-weight-bold text-purple-darken-4 mb-2">Our Vision</h3>
            <p class="text-body-2 text-grey-darken-2">
              To become the most trusted hostel directory platform in Myanmar, making hostel search simple and convenient for everyone.
            </p>
          </v-card>
        </v-col>

        <v-col cols="12" md="4">
          <v-card class="pa-6 rounded-xl elevation-2 text-left h-100 border-card">
            <v-row align="center" no-gutters>
              <v-col cols="12" class="text-center mb-4">
                <v-avatar color="purple-lighten-5" size="64">
                  <v-icon color="purple-darken-3" size="32" :icon="mdiDiamondStone"></v-icon>
                </v-avatar>
                <h3 class="text-h6 font-weight-bold text-purple-darken-4 mt-2 mb-2">Our Values</h3>
              </v-col>
            </v-row>
            <div class="space-y-2 pl-4">
              <div class="d-flex align-center mb-2">
                <v-icon color="purple-darken-3" size="18" class="mr-2" :icon="mdiCheckCircle"></v-icon>
                <span class="text-body-2 font-weight-bold text-grey-darken-3">Trust</span>
              </div>
              <div class="d-flex align-center mb-2">
                <v-icon color="purple-darken-3" size="18" class="mr-2" :icon="mdiCheckCircle"></v-icon>
                <span class="text-body-2 font-weight-bold text-grey-darken-3">User-Focused Design</span>
              </div>
              <div class="d-flex align-center">
                <v-icon color="purple-darken-3" size="18" class="mr-2" :icon="mdiCheckCircle"></v-icon>
                <span class="text-body-2 font-weight-bold text-grey-darken-3">Accuracy & Reliability</span>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <v-container class="py-6 mt-4">
      <v-card class="pa-4 rounded-xl elevation-1 bg-purple-lighten-5" flat>
        <v-row class="text-center align-center justify-space-between divider-row">
          <v-col cols="6" sm="3">
            <div class="d-flex align-center justify-center justify-sm-start px-4">
              <v-icon color="purple-darken-3" size="36" class="mr-3" :icon="mdiOfficeBuilding"></v-icon>
              <div class="text-left">
                <h2 class="text-h5 font-weight-black text-purple-darken-4">{{ stats.hostels }}</h2>
                <span class="text-caption font-weight-bold text-grey-darken-1 text-uppercase">Hostels Listed</span>
              </div>
            </div>
          </v-col>
          <v-col cols="6" sm="3">
            <div class="d-flex align-center justify-center px-4">
              <v-icon color="purple-darken-3" size="36" class="mr-3" :icon="mdiBedEmpty"></v-icon>
              <div class="text-left">
                <h2 class="text-h5 font-weight-black text-purple-darken-4">{{ stats.roomTypes }}</h2>
                <span class="text-caption font-weight-bold text-grey-darken-1 text-uppercase">Room Types</span>
              </div>
            </div>
          </v-col>
          <v-col cols="6" sm="3">
            <div class="d-flex align-center justify-center px-4">
              <v-icon color="purple-darken-3" size="36" class="mr-3" :icon="mdiHomeCircle"></v-icon>
              <div class="text-left">
                <h2 class="text-h5 font-weight-black text-purple-darken-4">{{ stats.rooms }}</h2>
                <span class="text-caption font-weight-bold text-grey-darken-1 text-uppercase">Room No</span>
              </div>
            </div>
          </v-col>
          <v-col cols="6" sm="3">
            <div class="d-flex align-center justify-center justify-sm-end px-4">
              <v-icon color="purple-darken-3" size="36" class="mr-3" :icon="mdiAccountGroup"></v-icon>
              <div class="text-left">
                <h2 class="text-h5 font-weight-black text-purple-darken-4">{{ stats.students }}</h2>
                <span class="text-caption font-weight-bold text-grey-darken-1 text-uppercase">Student Using</span>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-container>

    <v-container class="py-10">
      <h2 class="text-h5 font-weight-black text-purple-darken-4 mb-6 text-left pl-2">Contact Us</h2>
      <v-row>
        <v-col cols="12" md="5">
          <v-card class="pa-4 rounded-xl elevation-1 border mb-4 d-flex align-center bg-white">
            <v-avatar color="purple-lighten-5" class="mr-4" size="48">
              <v-icon color="purple-darken-3" :icon="mdiEmail"></v-icon>
            </v-avatar>
            <div class="text-left">
              <div class="text-subtitle-2 font-weight-bold text-purple-darken-4">Email</div>
              <div class="text-body-2 text-grey-darken-3">nyeinyielin202@gmail.com</div>
            </div>
          </v-card>

          <v-card class="pa-4 rounded-xl elevation-1 border mb-4 d-flex align-center bg-white">
            <v-avatar color="purple-lighten-5" class="mr-4" size="48">
              <v-icon color="purple-darken-3" :icon="mdiPhone"></v-icon>
            </v-avatar>
            <div class="text-left">
              <div class="text-subtitle-2 font-weight-bold text-purple-darken-4">Phone</div>
              <div class="text-body-2 text-grey-darken-3">+959667542516</div>
            </div>
          </v-card>

          <v-card class="pa-4 rounded-xl elevation-1 border mb-4 d-flex align-center bg-white">
            <v-avatar color="purple-lighten-5" class="mr-4" size="48">
              <v-icon color="purple-darken-3" :icon="mdiMapMarker"></v-icon>
            </v-avatar>
            <div class="text-left">
              <div class="text-subtitle-2 font-weight-bold text-purple-darken-4">Address</div>
              <div class="text-body-2 text-grey-darken-3">Bago Region, Phyu Township, Kanyutkwin</div>
            </div>
          </v-card>

          <v-card class="pa-4 rounded-xl elevation-1 border d-flex align-center bg-white">
            <v-avatar color="purple-lighten-5" class="mr-4" size="48">
              <v-icon color="purple-darken-3" :icon="mdiClockOutline"></v-icon>
            </v-avatar>
            <div class="text-left">
              <div class="text-subtitle-2 font-weight-bold text-purple-darken-4">Working Hours</div>
              <div class="text-body-2 text-grey-darken-3">Mon - Sat : 9:00 AM - 5:00 PM</div>
            </div>
          </v-card>
        </v-col>

        <v-col cols="12" md="7" class="pl-md-8">
          <div class="text-left mb-4">
            <h3 class="text-h6 font-weight-bold text-grey-darken-4">We'd love to hear from you!</h3>
            <p class="text-body-2 text-grey-darken-2">
              If you have any question, suggestions, or need support, feel free to contact us using the information provided.
            </p>
          </div>

          <v-form @submit.prevent="sendReviewMessage">
            <div class="text-subtitle-2 font-weight-bold text-grey-darken-3 text-left mb-1">Your Name</div>
            <v-text-field
              v-model="contactForm.name"
              variant="outlined"
              density="comfortable"
              class="mb-2 bg-grey-lighten-3"
              readonly
              disabled
              placeholder="Not Logged In"
              hide-details
            ></v-text-field>

            <div class="text-subtitle-2 font-weight-bold text-grey-darken-3 text-left mb-1">Your Email</div>
            <v-text-field
              v-model="contactForm.email"
              variant="outlined"
              density="comfortable"
              class="mb-2 bg-grey-lighten-3"
              readonly
              disabled
              placeholder="Not Logged In"
              hide-details
            ></v-text-field>

            <div class="text-subtitle-2 font-weight-bold text-grey-darken-3 text-left mb-1">Message</div>
            <v-textarea
              v-model="contactForm.review"
              placeholder="Enter your message"
              variant="outlined"
              rows="4"
              class="mb-4"
              hide-details
            ></v-textarea>

            <v-btn
              type="submit"
              color="purple-darken-3"
              block
              height="46"
              class="rounded-lg font-weight-bold text-capitalize text-white text-subtitle-1 elevation-1"
              :loading="isSubmitting"
            >
              Send Message
            </v-btn>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import useApi, { ApiStatus } from '../../api';
import { useAuthStore } from '../../store/auth'; 
import { Role } from '../../constants/Role.js';
import { userApiResource } from '../../api/resources/userResource';
import { reviewsApiResource } from '../../api/resources/reviewsResource';
import{mdiAccountGroup, mdiBedEmpty, mdiCheckCircle, mdiClockOutline, mdiDiamondStone, mdiEmail, mdiEyeOutline, mdiHomeCircle, mdiHomeCityOutline, mdiMagnify, mdiMapMarker, mdiOfficeBuilding, mdiPhone, mdiTarget}from '@mdi/js';
import { roomPublicApiResource } from '../../api/resources/roomPublicResource.js';
import { hostelPublicApiResource } from '../../api/resources/hostelPublicResource.js';
import { roomTypePublicApiResource } from '../../api/resources/roomTypePublicResource.js';
import { studentPublicApiResource } from '../../api/resources/studentPublicResource';

const authStore = useAuthStore();
const stats = ref({ hostels: 0, roomTypes: 0, rooms: 0, students: 0 });
const isSubmitting = ref(false);
const contactForm = ref({ name: '', email: '', review: '' });

const { call, response, status } = useApi<any>();

const onApiCall = async (params: any = {}) => {
  try {
    await call(roomPublicApiResource.getRooms, { params: { status: 'ACTIVE' } });
    let totalRooms = 0;
    if (status.value == ApiStatus.SUCCESS) {
      const resData = response.value as any;
      totalRooms = resData?.list?.length || resData?.data?.list?.length || 0;
    }

    await call(roomTypePublicApiResource.getRoomTypes, { params: { status: 'ACTIVE' } });
    let roomType = 0;
    if (status.value == ApiStatus.SUCCESS) {
      const resData = response.value as any;
      roomType = resData?.list?.length || resData?.data?.list?.length || 0;
    }

    await call(hostelPublicApiResource.getHostels, { params: { status: 'ACTIVE' } });
    let hostel = 0;
    if (status.value == ApiStatus.SUCCESS) {
      const resData = response.value as any;
      hostel = resData?.list?.length || resData?.data?.list?.length || 0;
    }

    await call(studentPublicApiResource.getAllStudents);
    let student = 0;
    if (status.value == ApiStatus.SUCCESS) {
      const resData = response.value as any;
      student = resData?.list?.length || resData?.data?.list?.length || 0;
    }

    stats.value = {
      hostels: hostel,
      roomTypes: roomType,
      rooms: totalRooms,
      students: student,
    };
  } catch (error) {
    console.error("API Error:", error);
  }
};

const sendReviewMessage = async () => {
  // Message အလွတ်ဖြစ်နေရင် ဘာမှမလုပ်ဘဲ ပြန်ထွက်မည်
  if (!contactForm.value.review.trim()) return;
  
  isSubmitting.value = true;
  
  try {
    await call(reviewsApiResource.register, { 
      data: contactForm.value 
    });

    if (status.value === ApiStatus.SUCCESS) {
      alert("Reviews Message အား အောင်မြင်စွာ ပေးပို့ပြီးပါပြီဗျာ!");
      contactForm.value.review = ''; 
    } else {
      alert("Message ပို့ရန် အဆင်မပြေဖြစ်နေပါသည်။ ပြန်လည်ကြိုးစားကြည့်ပါဦးဗျာ။");
    }

  } catch (error) {
    console.error("Review Send Error:", error);
    alert("စနစ်ချို့ယွင်းမှုတစ်ခု ဖြစ်ပွားခဲ့ပါသည်။");
  } finally {
    isSubmitting.value = false;
  }
};


onMounted(async () => {
  await onApiCall();

  const userId = authStore.user?.id;
  if (!userId) return;
  await call(userApiResource.getById, null, { id: userId });
  if (status.value === 'success' && response.value) {
    const data = response.value.data;
    contactForm.value.name = data.username || '';
    contactForm.value.email = data.studentEmail  ||data.ownerEmail || data.email ||  '';
     }
});




  


</script>

<style scoped>
.gray-bg { background-color: #fafafa; }
.border-card {
  border: 1px solid #e0e0e0 !important;
  transition: transform 0.2s;
}
.border-card:hover { transform: translateY(-4px); }
.leading-relaxed { line-height: 1.6; }

@media (min-width: 600px) {
  .divider-row > div:not(:last-child) { border-right: 2px solid #e0b0ff; }
}
</style>