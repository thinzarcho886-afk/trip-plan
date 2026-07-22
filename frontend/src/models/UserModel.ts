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
  role: string | Role;
  status: string | Status;
  username: string;
  password?: string;
  confirmPassword?: string;
  customerId: any;
  name: string;
  email: string;
  phone: string;

}

export const UserModel = (): User => ({
  customerId:null,
  name:'',
  role:'',
  username: '',
  status: Status.ACTIVE,
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',

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
