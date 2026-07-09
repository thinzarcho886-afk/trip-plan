import { reactive, toRefs } from 'vue';
import { AxiosError, AxiosRequestConfig, AxiosResponse, Method } from 'axios';
import { ApiService } from '../services/ApiService.js';

const apiService: ApiService = new ApiService();

// API status enum
export enum ApiStatus {
  IDLE = 'idle',
  LOADING = 'loading',
  SUCCESS = 'success',
  ERROR = 'error',
}

export enum ApiProgressType {
  IDLE = 'idle',
  UPLOAD = 'upload',
  DOWNLOAD = 'download',
}

export interface ApiResource {
  url: string;
  method: Method;
}

export type ApiResources<T extends string> = {
  [key in T]: ApiResource;
};

export interface ApiResult<K> {
  status: ApiStatus;
  response: any | null | AxiosResponse<K>;
  error: null | ApiError | any;
}

export interface ApiError {
  error: string;
  message: string;
  status: number;
}




export default function useApi<T>() {
  const result = reactive<ApiResult<T>>({
    status: ApiStatus.IDLE,
    error: null,
    response: null,
  });

  async function call(
    resource: ApiResource,
    config?: AxiosRequestConfig | null,
    urlParams?: { [key: string]: any }
  ) {
    result.status = ApiStatus.LOADING;

    let { url, method } = resource;
    // check url params and combine
    if (urlParams)
      Object.keys(urlParams).forEach((paramKey) => {
        let regex = new RegExp(`:${paramKey}`, 'g');
        url = url.replace(regex, String(urlParams[paramKey]));
      });

    try {
      const requestResponse = await apiService.request({
        url,
        method,
        ...config,
      });

      result.response = requestResponse;
      result.status = ApiStatus.SUCCESS;
    } catch (error: unknown) {
      const { response } = error as AxiosError;

      result.error = response?.data;
      result.status = ApiStatus.ERROR;

      if (response?.status === 500) {
        console.error('Something went wrong!');
      }
    }
  }

  function reset() {
    result.status = ApiStatus.IDLE;
    result.response = null;
    result.error = null;
  }

  return { ...toRefs(result), call, reset };
}
