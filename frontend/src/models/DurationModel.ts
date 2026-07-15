import { Status } from '../constants/Status';
import { CommonDto } from '../interfaces/CommonDto';

export interface DurationListParams {
  [key: string]: any;
  durationId:number|null;
  status: Status | string;
}

export const DurationListParamsModel = (): DurationListParams => ({
  durationId:null,
  status: '',
});

export interface Duration extends CommonDto {
 name:string;
  description:string;
  status: string | Status;
}

export const DurationModel  = (): Duration => ({
  name:'',
  description:'',
  status: Status.ACTIVE,
});
export interface DurationDetail extends CommonDto{
  description:string;
  status:Status;

}
