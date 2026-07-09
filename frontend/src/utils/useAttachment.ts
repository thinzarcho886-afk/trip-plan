import { reactive } from 'vue';
import { AttachmentType } from '../services/AttachmentService';
import { ApiStatus, ApiProgressType } from '../api';
import { AxiosError, AxiosProgressEvent } from 'axios';
import { AttachmentService } from '../services/AttachmentService';
import { downloadFile } from '.';
import { FileMimeType } from '../constants/FileMimiType';

interface AttachmentResult {
  progressType: ApiProgressType;
  status: ApiStatus;
  progress: number;
  message: any;
}

export function useAttachment(type: AttachmentType) {
  const attachmentService: AttachmentService = new AttachmentService();
  const attachmentResult = reactive<AttachmentResult>({
    status: ApiStatus.IDLE,
    progress: 0,
    message: null,
    progressType: ApiProgressType.IDLE,
  });

  const upload = async (id: number, attachments: File[]) => {
    setAttachmentResult({
      progressType: ApiProgressType.UPLOAD,
      status: ApiStatus.LOADING,
      progress: 0,
      message: null,
    });

    try {
      const res = await attachmentService.upload(id, type, attachments, {
        onUploadProgress: onHandleProgress,
      });

      setAttachmentResult({
        status: ApiStatus.SUCCESS,
        message: res.data.message,
      });
    } catch (error: unknown) {
      const { response } = error as AxiosError;
      console.error(error);

      setAttachmentResult({
        status: ApiStatus.ERROR,
        message: (response?.data as any)?.message || error,
      });
    }
  };

  async function download(id: number) {
    setAttachmentResult({
      progressType: ApiProgressType.DOWNLOAD,
      status: ApiStatus.LOADING,
      progress: 0,
      message: null,
    });

    try {
      const requestResponse = await attachmentService.downloadAttachment(
        id,
        type,
        {
          onDownloadProgress: onHandleProgress,
        }
      );

      await downloadFile(requestResponse, { type: FileMimeType.OCTET_STREAM });

      setAttachmentResult({ status: ApiStatus.SUCCESS });
    } catch (error: unknown) {
      const { response } = error as AxiosError;
      console.error(error);

      setAttachmentResult({
        status: ApiStatus.ERROR,
        message: (response?.data as any)?.message || error,
      });
    }
  }

  const onHandleProgress = (progressEvent: AxiosProgressEvent) => {
    if (typeof progressEvent.total === 'number') {
      setAttachmentResult({
        progress: Math.round(
          (progressEvent.loaded / progressEvent.total) * 100
        ),
      });
    } else {
      setAttachmentResult({ progress: -1 });
    }
  };

  function setAttachmentResult(args: Partial<AttachmentResult>) {
    typeof args.status != 'undefined' &&
      (attachmentResult.status = args.status);
    typeof args.message != 'undefined' &&
      (attachmentResult.message = args.message);
    typeof args.progress != 'undefined' &&
      (attachmentResult.progress = args.progress);
    typeof args.progressType != 'undefined' &&
      (attachmentResult.progressType = args.progressType);
  }

  function resetAttachmentResult() {
    setAttachmentResult({
      progressType: ApiProgressType.IDLE,
      status: ApiStatus.IDLE,
      progress: 0,
      message: null,
    });
  }

  return { attachmentResult, download, upload, resetAttachmentResult };
}
