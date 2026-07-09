<template>
  <v-dialog v-model="dialog" width="500" persistent>
    <v-card>
      <v-card-title class="d-flex">
        <div>{{ t('Select File') }}(s)</div>
      </v-card-title>

      <v-card-text style="min-height: 80px; overflow-y: auto" class="pa-0">
        <div class="pa-4">
          <v-file-input
            :label="t('Select File(s)')"
            v-model="attachments"
            multiple
            :rules="[rules.fileSize(30, true)]"
          ></v-file-input>
        </div>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="dialog = false"> {{ t('Close') }} </v-btn>
        <v-btn color="primary" variant="elevated" @click="apply">
          {{ t('Apply') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { fileSize } from '../../utils/validations';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const props = defineProps(['dialog']);
const emits = defineEmits(['update:dialog', 'apply']);

const dialog = computed({
  get() {
    return props.dialog;
  },
  set(v) {
    emits('update:dialog', v);
  },
});
const rules = { fileSize };
const attachments = ref<File[]>([]);

watch(dialog, (v) => {
  if (v) {
    attachments.value = [];
  }
});

const apply = async () => {
  // recheck with rules to prevent applying
  if (
    attachments.value.length > 0 &&
    rules.fileSize(30, true)(attachments.value) === true
  ) {
    emits('apply', attachments.value);
    dialog.value = false;
  }
};
</script>
