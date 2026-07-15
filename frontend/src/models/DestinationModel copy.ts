import { Status } from '../constants/Status';
import { CommonDto } from '../interfaces/CommonDto';

export interface DestinationListParams {
  [key: string]: any;
  name: string;
  description:string;
  status: Status | string;
}

export const DestinationListParamsModel = (): DestinationListParams => ({
  name: '',
  description:'',
  status: '',
});

export interface Destination extends CommonDto {
  name: string;
  description?: string;
  status: string | Status;
}

export const DestinationModel = (): Destination => ({
  name: '',
  description: '',
  status: Status.ACTIVE,
});
