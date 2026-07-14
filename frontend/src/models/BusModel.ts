import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface BusListParams {
  [key: string]: any;
  name: string;
  status: string | Status;
  
}

export const BusListParamsModel = (): BusListParams => ({
  name: '',
  status:'',
});

export interface Bus extends CommonDto {
  name:string;
  status:string |Status;
}

export const BusModel = (): Bus => ({
  name: '',
  status:Status.ACTIVE,


});
