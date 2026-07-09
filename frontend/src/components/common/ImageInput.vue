<template>
  <v-card
    variant="outlined"
    style="border-color: grey !important"
    rounded="lg"
    :width="`${props.width}`"
    :height="`${props.height}`"
  >
    <v-img
      :src="imageUrl"
      aspect-ratio="1"
      contain
      :width="`${props.imageWidth}`"
      :height="`${props.imageHeight}`"
      class="align-end"
      gradient="to bottom, rgba(0,0,0,.08), rgba(0,0,0,.5)"
    >
      <div class="text-white text-center py-2">{{ props.label }}</div>
      <template v-slot:placeholder>
        <div
          class="d-flex align-center justify-center fill-height"
          v-if="imageUrl"
        >
          <v-progress-circular
            color="grey-lighten-4"
            indeterminate
          ></v-progress-circular>
        </div>
      </template>
    </v-img>

    <v-divider></v-divider>
    <div class="bg-white px-4 py-2 rounded-lg" style="height: 100%">
      <div class="mt-2">
        <v-file-input
          show-size
          :label="imageUrl ? t('Update Image') : t('Select Image')"
          density="compact"
          v-model="files"
          @change="onChange"
          variant="outlined"
          accept="image/png, image/jpeg"
          :rules="rules"
          chips
          :clearable="false"
        ></v-file-input>
      </div>

      <div v-if="imageUrl">
        <div class="text-center" v-if="!isDeleteConfirm">
          <v-btn
            variant="tonal"
            color="error"
            @click="isDeleteConfirm = true"
            density="compact"
          >
           {{ t('Remove')}}
          </v-btn>
        </div>

        <div class="text-left" v-else>
          <span> Remove? </span>
          <v-btn
            @click="onImageDelete"
            color="error"
            variant="text"
            class="mx-2"
            density="compact"
          >
            {{t('Yes')}}
          </v-btn>
          <v-btn
            @click="isDeleteConfirm = false"
            variant="text"
            density="compact"
          >
            {{t('No')}}
          </v-btn>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import { convertImageFileToBase64 } from '../../utils/index';
import { useI18n } from 'vue-i18n';
const { t }=useI18n();
const props = defineProps([
  'imageUrl',
  'modelValue',
  'imageWidth',
  'imageHeight',
  'width',
  'height',
  'label',
]);
const emits = defineEmits(['update:modelValue', 'delete']);

const files = ref<File | null>(null);
const imageUrl = ref('');
const isDeleteConfirm = ref(false);

const rules = ref([
  (v: any) =>
    !v || !v.length || v[0].size <= 5000000 || 'should be less than 5 MB!',
]);

watch(
  () => props.imageUrl,
  (v: string) => {
    if (v) {
      imageUrl.value = v;
    } else {
      imageUrl.value = '';
    }
  },
  { immediate: true }
);

const onChange = () => {
  const inputFiles: File | null = files.value ?? null;
  if (!inputFiles) return;

  // set image preview
  imageUrl.value = URL.createObjectURL(inputFiles);
  convertImageFileToBase64(inputFiles, (base64: any) => {
    emits('update:modelValue', base64);
  });
};

const onImageDelete = () => {
  if (props.imageUrl) {
    emits('delete');
  }
  reset();
};

const reset = () => {
  imageUrl.value = '';
  files.value = null;
  isDeleteConfirm.value = false;
};
</script>
