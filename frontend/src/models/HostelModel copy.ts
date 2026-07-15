import { Gender } from '../constants/Gender.js';
import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface HostelListParams {
  [key: string]: any;
  hostelName: string;
  studentPhone: string;
  studentAddress: string;
  hostelStatus?: string;
  cityId?: string | null;
  cityName?: string;
  townshipId?: string | null;
  townshipName?: string;
  streetId?: string | null;
  streetName?: string;
  phoneNo?: string;
  gender?: string;
  status: Status | string;
  ownerPhone:string;
 
}

export const HostelListParamsModel = (): HostelListParams => ({
  hostelName: '',
  studentPhone: '',
  studentAddress: '',
  status:'',
  ownerPhone:'',
  gender:'',
});

export interface HostelListPublicParams {
  [key: string]: any;
  hostelPublicName: string;
  studentPhone: string;
  studentAddress: string;

  status: Status | string;
}

export const HostelListPublicParamsModel = (): HostelListPublicParams => ({
  hostelPublicName: '',
  studentPhone: '',
  studentAddress: '',
 
  status:'',
});

export interface Hostel extends CommonDto {
  hostelImage:string;
  hostelImageUrl?:string | null;
  hostelName:string;
  gender:string | Gender;
  description:string;
  hostelStatus:string;
  status:string | Status;
  streetId:any;
  streetName:string;
  townshipId:any;
  townshipName:string;
  cityId:any;
  cityName:string;
  roomList:string;
  deleteRoomDetailIds:string;
  userId:number | null;
  userName:string;
  rooms:any;
  ownerId:any;
  ownerName:string;
  phone:string;
  latitude: string;
  longitude: string;
}

export const HostelModel = (): Hostel => ({
  hostelImageUrl:'',
  hostelImage:'',
  hostelName:'',
  gender:'',
  description:'',
  hostelStatus:'',
  status:Status.INACTIVE,
  streetId:'',
  streetName:'',
  townshipId:'',
  townshipName:'',
  cityId:'',
  cityName:'',
  roomList:'',
  deleteRoomDetailIds:'',
  userId:null,
  userName:'',
  rooms:'',
  ownerId:'',
  ownerName:'',
  phone:'',
  latitude:'',
  longitude:''
});



