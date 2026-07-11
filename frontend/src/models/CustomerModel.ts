import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface CustomerListParams {
  [key: string]: any;
  name: string;
  email: string;
  phoneNumber: string;
  profileImage: string;
  profileImageUrl: string;
  status: string | Status;
}

export const CustomerListParamsModel = (): CustomerListParams => ({
  name: '',
  email: '',
  phoneNumber: '',
  profileImage: '',
  profileImageUrl:'',
  status:'',
});

export interface Customer extends CommonDto {
  name:string;
  email: string;
  phoneNumder:string;
  profileImage:string;
  profileImageUrl:string;
  status:string |Status;
  

}

export const CustomerModel = (): Customer => ({
  name: '',
  email:'',
  phoneNumder:'',
  profileImage:'',
  profileImageUrl:'',
  status:'',
  

});
