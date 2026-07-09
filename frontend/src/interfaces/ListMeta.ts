import { ApiResource } from '../api';

export interface ListMeta {
  headers: { [key: string]: any };
  apiResource: ApiResource;
  responseKey: string;
  defaultSort: { key: string; order: 'asc' | 'desc' }[];
}
