import { Status } from '../constants/Status';
import { RoomStatus } from '../constants/RoomStatus';

import { CommonDto } from '../interfaces/CommonDto';

export interface RoomListParams {
  [key: string]: any;
  roomNo:string;
  name:string;
  // roomStatus:RoomStatus | string;
  // status:Status;

}

export const RoomListParamsModel = (): RoomListParams => ({
  roomNo:'',
  name:'',
  // roomStatus:RoomStatus.AVAILABLE,
  // status:Status.ACTIVE,
});

export interface Room extends CommonDto {
  hostelId:string;
  hostelName:string;
  hostelImage:string;
  roomTypeId:string;
  name:string;
  roomImage:string;
  roomImageUrl:string|any,
  roomNo:string;
  capacityPerRoom:string;
  price:number|null;
  availableUnit:number|null;
  roomDetailList?:RoomDetail[];
  roomStatus:RoomStatus | string;
  status:Status;
}

export const RoomModel = (): Room => ({
  hostelId:'',
  hostelName:'',
  hostelImage:'',
  name:'',
  roomTypeId:'',
  roomImage:'',
  roomImageUrl:'',
  roomNo:'',
  capacityPerRoom:'',
  price:null,
  availableUnit:null,
  roomDetailList:[],
  roomStatus:RoomStatus.AVAILABLE,
  status:Status.ACTIVE,
});

export interface RoomDetail extends CommonDto{
  hostelId:string;
  hostelName:string;
  roomTypeId:string;
  name:string;
  roomImage:string;
  roomImageUrl:string;
  roomNo:string;
  capacityPerRoom:string;
  price:number|null;
  availableUnit:number|null;
  roomStatus:RoomStatus | string;
  status:Status;

}

