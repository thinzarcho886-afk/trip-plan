import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { ApiService } from './ApiService.js';
import { FileMimeType } from '../constants/FileMimiType.js';

export enum AttachmentType {
  CUSTOMER = 'CUSTOMER',
  BOOKING = 'BOOKING',
  CONTRACT = 'CONTRACT',
}

export class AttachmentService {
  private readonly baseUrl = '/auth/attachment';
  private apiService: ApiService;

  constructor() {
    this.apiService = new ApiService();
  }

  async upload(
    id: number,
    type: AttachmentType,
    attachments: File[],
    config?: AxiosRequestConfig
  ) {
    if (attachments.length < 1 || !type || !id) {
      throw new Error('Invalid arguments');
    }

    const formData: FormData = new FormData();

    formData.append('id', String(id));
    formData.append('type', type);
    attachments.forEach((attachment) => {
      formData.append('attachments', attachment);
    });

    return await this.apiService.post(this.baseUrl, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      ...config,
    });
  }

  async delete(ids: number[], type: AttachmentType): Promise<AxiosResponse> {
    if (!type || !ids) {
      throw new Error('Invalid arguments');
    }

    return await this.apiService.delete(this.baseUrl, {
      params: {
        ids: ids.join(','),
        type,
      },
    });
  }

  async downloadAttachment(
    id: number,
    type: AttachmentType,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    if (!type || !id) {
      throw new Error('Invalid arguments');
    }

    return await this.apiService.get(this.baseUrl + `/${id}`, {
      responseType: 'arraybuffer',
      headers: {
        Accept: `${FileMimeType.OCTET_STREAM}, ${FileMimeType.JSON}, ${FileMimeType.PLAIN}`,
      },
      params: { type },
      ...config,
    });
  }
}
