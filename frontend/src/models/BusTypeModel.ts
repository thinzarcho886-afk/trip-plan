import { Status } from '../constants/Status';
import { CommonDto } from '../interfaces/CommonDto';

export interface BusTypeListParams {
  [key: string]: any;
  busTypeId:number|null;
  status: Status | string;
}

export const BusTypeListParamsModel = (): BusTypeListParams => ({
  busTypeId:null,
  status: '',
});

export interface BusType extends CommonDto {
 name:string;
  availableSeats:BigInteger|null;
  description:string;
  status: string | Status;
}

export const BusTypeModel  = (): BusType => ({
  name:'',
  availableSeats:null,
  description:'',
  status: Status.ACTIVE,
});
export interface BusDetail extends CommonDto{
  description:string;
  availableSeats:BigInteger|null;
  status:Status;

}
