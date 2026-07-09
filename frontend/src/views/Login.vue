<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4" class="text-center">
        <v-icon size="64" color="primary">mdi-home-city</v-icon>
        <h2 class="mt-2">Hostel Directory Management System</h2>

        <h3 class="my-6">Welcome !</h3>

        <v-btn block size="large" color="primary" class="mb-4" to="/register">
          Create Account
        </v-btn>

        <v-btn block size="large" variant="outlined" color="primary" to="/login">
          Login
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { defineAsyncComponent } from 'vue';

const LoginForm = defineAsyncComponent(
  () => import('../components/LoginForm.vue')
);
</script>
