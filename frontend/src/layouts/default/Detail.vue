<template>
  <div class="px-2 px-sm-4 px-md-6">
    <!-- title -->
    <Teleport to="#appbar-title"> {{ props.title || 'List' }} </Teleport>

    <!-- breadcrumbs and actions -->
    <div
      class="d-flex justify-space-between align-center py-4"
      style="height: 68px; font-size: 14px"
    >
      <!-- breadcrumbs -->
      <div>
        <slot name="breadcrumbs">
          <v-breadcrumbs
            v-if="
              !breakpointXs && props.breadcrumbs && props.breadcrumbs.length
            "
            :items="props.breadcrumbs"
            class="text-capitalize"
          >
          </v-breadcrumbs>
          <v-spacer v-else></v-spacer>
        </slot>
      </div>

      <!-- actions -->
      <div class="d-flex" style="gap: 1em">
        <slot name="actions">
          <template v-if="props.actions" v-for="action in props.actions">
            <v-btn
              :color="action.color"
              :to="action.to"
              @click="action.onClick"
              v-bind:="{
                ...(action.useDisabled && {
                  disabled:
                    isFormValid === false || isLoading || isBtnDisable === true,
                }),
                ...(action.useLoading && {
                  loading: isLoading,
                }),
              }"
            >
              <v-icon :icon="action.icon" start v-if="action.icon"></v-icon>
              {{ t(action.label) }}
            </v-btn>
          </template>
        </slot>
      </div>
    </div>

    <!-- form -->
    <div>
      <v-card
        :loading="isLoading"
        :disabled="isLoading"
        style="font-size: 15px !important"
        rounded="lg"
      >
        <div class="pb-2" v-if="message">
          <v-alert
            :color="isError ? 'error' : 'success'"
            variant="tonal"
            border="start"
            density="comfortable"
          >
            {{ message }}
          </v-alert>
        </div>
        <slot name="form"> </slot>
      </v-card>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const props = defineProps([
  'loading',
  'error',
  'message',
  'title',
  'breadcrumbs',
  'actions',
  'formValid',
  'btnDisable',
]);
const { xs: breakpointXs } = useDisplay();
const isLoading = computed(() => props.loading);
const isError = computed(() => props.error);
const message = computed(() => props.message);
const isFormValid = computed(() => props.formValid);
const isBtnDisable = computed(() => props.btnDisable || false);
</script>
