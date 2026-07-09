<template>
  <v-dialog v-model="dialog" width="500" persistent>
    <v-card>
      <v-card-title class="d-flex">
        <div>File {{ attachmentResult.progressType }}</div>
      </v-card-title>

      <v-card-text style="min-height: 80px; overflow-y: auto" class="pa-0">
        <div class="pa-4">
          <!-- progress bar -->
          <div v-if="attachmentResult.progress > -1" class="mb-4">
            <v-progress-linear
              v-model="attachmentResult.progress"
              height="18"
              :color="
                attachmentResult.status == ApiStatus.ERROR
                  ? 'error'
                  : attachmentResult.status == ApiStatus.SUCCESS
                  ? 'success'
                  : 'primary'
              "
            >
              <template v-slot:default="{ value }">
                <v-chip
                  :color="attachmentResult.progress < 49 ? 'primary' : 'white'"
                >
                  <strong>{{ Math.ceil(value) }}%</strong>
                </v-chip>
              </template>
            </v-progress-linear>
          </div>

          <!-- alert -->
          <div
            v-if="
              [ApiStatus.ERROR, ApiStatus.SUCCESS].includes(
                attachmentResult.status
              ) && attachmentResult.message
            "
          >
            <v-alert
              :type="attachmentResult.status"
              variant="tonal"
              density="compact"
            >
              <span>
                {{ attachmentResult.message }}
              </span>
            </v-alert>
          </div>

          <!-- action -->
          <div
            class="text-center"
            v-if="attachmentResult.status == ApiStatus.ERROR"
          >
            <v-btn variant="tonal" class="mt-3" @click="dialog = false">
              {{ t('Close') }}
            </v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { ApiStatus } from '../../api';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const props = defineProps(['dialog', 'result']);
const emits = defineEmits(['update:dialog']);

const dialog = computed({
  get() {
    return props.dialog;
  },
  set(v) {
    emits('update:dialog', v);
  },
});

const attachmentResult = computed(() => props.result);
</script>
