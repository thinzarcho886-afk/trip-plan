import { Status } from '../constants/Status';
import { CommonDto } from '../interfaces/CommonDto';

export interface FloorListParams {
  [key: string]: any;
  name: string;
  companyId: string | null;
  company: string;
  status: Status | string;
}

export const FloorListParamsModel = (): FloorListParams => ({
  name: '',
  companyId: null,
  company: '',
  status: '',
});

export interface Floor extends CommonDto {
  name: string;
  companyId: string | null;
  companyName?: string;
  company?: string;
  description?: string;
  status: string | Status;
}

export const FloorModel = (): Floor => ({
  name: '',
  companyId: '',
  companyName: '',
  description: '',
  status: Status.ACTIVE,
});
