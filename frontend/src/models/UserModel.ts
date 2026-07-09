import { Role } from '../constants/Role.js';
import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface UserListParams {
  [key: string]: any;
  username: string;
  role: Role | string;
  status: Status | string;
}

export const UserListParamsModel = (): UserListParams => ({
  id: '',
  username: '',
  status: '',
  role: '',
});

export interface User extends CommonDto {
  studentId:any;
  studentImage:string;
  studentImageUrl: string;
  studentName:string;
  studentEmail:string;
  studentPhone:string;
  studentAddress:string;
  studentStatus:string;
  ownerId:any;
  ownerImage:string;
  ownerImageUrl: string;
  ownerName:string;
  ownerEmail:string;
  ownerPhone:string;
  ownerAddress:string;
  ownerStatus:string;
  role: string | Role;
  status: string | Status;
  username: string;
  password?: string;
  confirmPassword?: string;

}

export const UserModel = (): User => ({
  studentId: '',
  studentImage:'',
  studentImageUrl:'',
  studentName:'',
  studentEmail:'',
  studentPhone:'',
  studentAddress:'',
  studentStatus:Status.ACTIVE,
  ownerId: '',
  ownerImage:'',
  ownerImageUrl:'',
  ownerName:'',
  ownerEmail:'',
  ownerPhone:'',
  ownerAddress:'',
  ownerStatus:Status.ACTIVE,
  role:'',
  username: '',
  status: Status.ACTIVE,
  password: '',
  confirmPassword: '',

});

export interface UserChangePassword {
  id: string | number;
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export const UserChangePasswordModel = (): UserChangePassword => ({
  id: '',
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: '',
});
