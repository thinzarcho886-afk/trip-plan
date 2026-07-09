<template>
  <v-container class="fill-height welcome-bg" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="5" lg="4">
        <v-card class="pa-10 text-center elevation-0 rounded-xl" color="rgba(255, 255, 255, 0.9)">

          <div class="mb-6">
            <div class="d-flex align-center justify-center mb-2">
             <v-icon size="30" color="primary" class="mr-2" :icon="mdiCity"></v-icon>
              <div class="text-h6 font-weight-bold text-primary">Hostel Directory System</div>
            </div>
          </div>

          <div class="mb-10">
            <h2 class="text-h3 font-weight-black text-primary mb-2">Welcome !</h2>
          </div>

          <div class="d-flex flex-column align-center ga-4">
            <v-btn
              width="280"
              size="large"
              color="primary"
              elevation="1"
              rounded="pill"
              to="/registerPage"
              class="text-none font-weight-semibold"
            >
              Create Account
            </v-btn>

            <v-btn
              width="280"
              size="large"
              variant="outlined"
              color="primary"
              rounded="pill"
              to="/loginPage"
              class="text-none font-weight-semibold"
            >
              Login
            </v-btn>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.welcome-bg {
  background: linear-gradient(135deg, #f3e5f5 0%, #e0f7fa 100%);
}
.v-card {
  box-shadow: 0 4px 20px rgba(0,0,0,0.05) !important;
}
</style>

<script setup lang="ts">
import { mdiCity } from '@mdi/js';
</script>
