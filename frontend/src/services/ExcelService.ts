import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { ApiService } from './ApiService';
import { FileMimeType } from '../constants/FileMimiType';
import { ClassType } from '../constants/ClassType';

export class ExcelService {
  private apiService: ApiService;

  constructor() {
    this.apiService = new ApiService();
  }

  async upload(
    url: string,
    file: any,
    classType: ClassType,
    config?: AxiosRequestConfig
  ) {
    const formData: FormData = new FormData();

    formData.append('file', file);
    formData.append('classType', classType);

    return await this.apiService.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      ...config,
    });
  }

  async download(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    return await this.apiService.get(url, {
      responseType: 'arraybuffer',
      headers: {
        Accept: `${FileMimeType.EXCEL}, ${FileMimeType.JSON}, ${FileMimeType.PLAIN}`,
      },
      ...config,
    });
  }
}
