import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface PaymentMethodListParams {
  [key: string]: any;
  name: string;
  accountNumber: string;
  accountName: string;
  description: string;
  imageUrl: string;
  image: string;
  status: string | Status;
  
}

export const PaymentMethodListParamsModel = (): PaymentMethodListParams => ({
  name: '',
  accountNumber:'',
  accountName:'',
  description:'',
  imageUrl:'',
  image:'',
  status:'',
});

export interface PaymentMethod extends CommonDto {
  name:string;
  accountNumber: string;
  accountName: string;
  description: string;
  imageUrl: string;
  image: string;
  status:string |Status;
}

export const PaymentMethodModel = (): PaymentMethod => ({
  name: '',
   accountNumber:'',
  accountName:'',
  description:'',
  imageUrl:'',
  image:'',
  status:Status.ACTIVE,


});
