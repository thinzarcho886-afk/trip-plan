import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
} from 'axios';
import { AuthService } from './AuthService.js';
import { arrayBufferToJson } from '../utils/index.js';
import { CustomError } from '../utils/CustomError.js';
import { getLocale } from '../plugins/i18n.js';
import router from '../router/index.js';
import { useAuthStore } from '../store/auth.js';
import { routeNames } from '../router/routes.js';
const authStore = useAuthStore();

export class ApiService {
  private instance: AxiosInstance;
  private authService: AuthService;

  constructor() {
    this.instance = axios.create({
      baseURL: import.meta.env.VITE_APP_API_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    this.authService = new AuthService();

    this.setRequestInterceptors();
    this.setResponseInterceptors();
  }

  private setRequestInterceptors() {
    this.instance.interceptors.request.use((req) => {
      if (this.authService.isAuth()) {
        req.headers['Authorization'] = this.authService.getBearerAuth();
      } else {
        delete req.headers['Authorization'];
      }
      req.headers['Accept-Language'] = getLocale();
      return req;
    });
  }

  private setResponseInterceptors() {
    this.instance.interceptors.response.use(
      (res: AxiosResponse) => {
        return res;
      },
      (error: any) => {
        const { response } = error as AxiosError;

        // handle arraybuffer error in downloads
        if (response?.data instanceof ArrayBuffer) {
          const errObj = arrayBufferToJson(response.data);
          throw new CustomError(errObj?.message || 'Error', errObj);
        }

        if (
          error.response &&
          error.response.status == '401' &&
          this.authService.isAuth()
        ) {
          // logout
          authStore.clear();
          router.push({ name: routeNames.login }).catch(() => {
            location.reload();
          });
          // throw error;
          throw error;
        } else if (error.response && error.response.data.fieldErrorMessages) {
          const obj = error.response.data.fieldErrorMessages;
          const message = obj[Object.keys(obj)[0]];
          error.response.data.message = message;
          throw error;
        } else {
          throw error;
        }
      }
    );
  }

  request(config: AxiosRequestConfig): Promise<AxiosResponse> {
    return this.instance.request(config);
  }

  get(resource: string, config?: AxiosRequestConfig): Promise<AxiosResponse> {
    return this.instance.get(resource, config);
  }

  post(
    resource: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    return this.instance.post(resource, data, config);
  }

  put(
    resource: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    return this.instance.put(resource, data, config);
  }

  patch(
    resource: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    return this.instance.patch(resource, data, config);
  }

  delete(
    resource: string,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse> {
    return this.instance.delete(resource, config);
  }
}
