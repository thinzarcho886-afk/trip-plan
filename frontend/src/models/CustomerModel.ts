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
  password: string;
}

export const CustomerListParamsModel = (): CustomerListParams => ({
  name: '',
  email: '',
  phoneNumber: '',
  profileImage: '',
  profileImageUrl:'',
  status:'',
  password:'',
});

export interface Customer extends CommonDto {
  name:string;
  email: string;
  phoneNumber:string;
  profileImage:string;
  profileImageUrl:string;
  status:string |Status;
  password: string;
  confirmPassword: string;
  

}

export const CustomerModel = (): Customer => ({
  name: '',
  email:'',
  phoneNumber:'',
  profileImage:'',
  profileImageUrl:'',
  status:Status.ACTIVE,
  password:'',
  confirmPassword:'',
  

});

export interface CustomerChangePassword {
  id: string | number;
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export const CustomerChangePasswordModel = (): CustomerChangePassword => ({
  id: '',
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: '',
});

