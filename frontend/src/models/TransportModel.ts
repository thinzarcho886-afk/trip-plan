import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface TransportListParams {
  [key: string]: any;
  busTypeId: number| null;
  busId: number| null;
  busName: string;
}

export const TransportListParamsModel = (): TransportListParams => ({
  busTypeId: null,
  busId: null,
  busName: '',
});

export interface Transport extends CommonDto {
  busTypeId: number| null;
  busId: number| null;
  busName: string;
  

}

export const TransportModel = (): Transport => ({
  busTypeId: null,
  busId: null,
  busName: '',
});


