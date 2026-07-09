<template>
  <div class="d-flex my-4 align-center">
    <div>
      <div class="text-h7 text-md-h6 font-weight-bold">
        {{ t('Attachment', 2) }}
      </div>
    </div>
    <v-btn
      class="mx-3"
      :icon="mdiPlus"
      @click="fileSelectDialog = true"
      color="primary"
      variant="tonal"
    ></v-btn>
  </div>

  <v-card variant="outlined" style="border-color: grey !important">
    <div class="pb-2" v-if="errorMessage">
      <v-alert
        color="error"
        variant="tonal"
        border="start"
        density="comfortable"
      >
        {{ errorMessage }}
      </v-alert>
    </div>

    <v-table density="comfortable" fixed-header height="550">
      <thead>
        <tr>
          <th class="text-left" style="width: 200px">{{ t('File Name') }}</th>
          <th class="text-center" style="width: 180px">{{ t('Action') }}</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-if="newAttachments.length > 0"
          v-for="(newAttachment, index) in newAttachmentList"
          :key="`newAttachment-${index}`"
        >
          <td class="text-left">{{ newAttachment.fileName }}</td>
          <td class="text-center">
            <div class="d-flex justify-center align-center" style="gap: 0.5rem">
              <v-chip color="info">{{ t('Upload Pending') }}</v-chip>

              <v-btn
                :icon="mdiClose"
                @click="removeAttachment(index, newAttachment)"
                variant="text"
              ></v-btn>
            </div>
          </td>
        </tr>
        <tr
          v-for="(attachment, index) in attachmentList"
          :key="`attachment-${index}`"
        >
          <td class="text-left">{{ attachment.fileName }}</td>
          <td class="text-center">
            <div class="d-flex justify-center" style="gap: 0.5rem">
              <v-btn
                :icon="mdiDownload"
                @click="downloadAttachment(Number(attachment.id))"
                variant="text"
                color="success"
              >
              </v-btn>
              <v-btn
                :icon="mdiClose"
                @click="removeAttachment(index, attachment)"
                variant="text"
                color="error"
              ></v-btn>
            </div>
          </td>
        </tr>
      </tbody>
    </v-table>
  </v-card>

  <attachment-progress-dialog
    v-model:dialog="progressDialog"
    :result="attachmentResult"
  ></attachment-progress-dialog>

  <file-select-dialog
    v-model:dialog="fileSelectDialog"
    @apply="onApplyFiles"
  ></file-select-dialog>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue';
import { mdiClose, mdiPlus, mdiDownload } from '@mdi/js';
import { Attachment } from '../../interfaces/Attachment';
import { useAttachment } from '../../utils/useAttachment';
import { AttachmentService } from '../../services/AttachmentService';
import AttachmentProgressDialog from './AttachmentProgressDialog.vue';
import FileSelectDialog from './FileSelectDialog.vue';
import { ApiStatus } from '../../api';
import { AxiosError } from 'axios';
import { useI18n } from 'vue-i18n';

const { t } = useI18n({ useScope: 'global' });

const attachmentService = new AttachmentService();

const props = defineProps(['modelValue', 'attachmentType']);
const emits = defineEmits(['update:modelValue', 'update:delete']);

const attachmentList = computed<Attachment[]>({
  get() {
    return props.modelValue;
  },
  set(v) {
    emits('update:modelValue', v);
  },
});
const newAttachments = ref<File[]>([]);
const removedAttachmentIds = ref<number[]>([]);
const progressDialog = ref(false);
const fileSelectDialog = ref(false);

const newAttachmentList = computed<Attachment[]>(() =>
  newAttachments.value.map((file) => ({
    fileName: file.name,
    fileUrl: file.webkitRelativePath ?? '',
  }))
);

const errorMessage = ref('');

const { upload, download, attachmentResult } = useAttachment(
  props.attachmentType
);

const removeAttachment = async (index: number, item: Attachment) => {
  if (item.id) {
    removedAttachmentIds.value.push(item.id);
    attachmentList.value.splice(index, 1);
  } else {
    newAttachments.value.splice(index, 1);
  }
};

const uploadAttachments = async (
  referenceId: number,
  cb: (error: boolean, message?: string) => void
) => {
  // open upload progress dialog

  if (newAttachments.value.length > 0) {
    progressDialog.value = true;

    await upload(referenceId, newAttachments.value as File[]);

    if (
      cb &&
      typeof cb == 'function' &&
      [ApiStatus.SUCCESS, ApiStatus.ERROR].includes(attachmentResult.status)
    )
      setTimeout(() => {
        cb(
          attachmentResult.status === ApiStatus.ERROR,
          attachmentResult.message as string
        );
      }, 1000);
  } else {
    cb(false);
  }
};

const deleteAttachments = async () => {
  if (removedAttachmentIds.value.length > 0) {
    await attachmentService.delete(
      removedAttachmentIds.value,
      props.attachmentType
    );
  }
};

const updateAttachments = async (
  referenceId: number,
  cb: (error: boolean, message?: string) => void
) => {
  try {
    await deleteAttachments();
    await uploadAttachments(referenceId, cb);
  } catch (error) {
    console.error(error);
    const { response } = error as AxiosError;
    errorMessage.value = (response?.data as any)?.message;
  }
};

const downloadAttachment = async (id: number) => {
  progressDialog.value = true;

  await download(id);

  if (attachmentResult.status == ApiStatus.SUCCESS) {
    setTimeout(() => {
      progressDialog.value = false;
    }, 1000);
  }
};

const onApplyFiles = (e: File[]) => {
  newAttachments.value.push(...e);
};

// to use with Ref in List component
defineExpose({
  updateAttachments,
});
</script>
