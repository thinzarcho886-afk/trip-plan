import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';
export interface PackageListParams {
  [key: string]: any;
  name: string;
  destinationId: number | null;
  status: string | Status;
}

export const PackageListParamsModel = (): PackageListParams => ({
  name: '',
  destinationId: null,
  status: '',
});

export interface Package extends CommonDto {
  name: string;
  destinationId: number | null;
  destinationName?: string;
  hotelId: number | null;
  hotelName?: string;
  transportId: number | null;
  transportName?: string;
  durationId: number | null;
  durationName?: string;
  departureDate: string | null; // Backend က Instant ကို string (ISO format) အဖြစ် လက်ခံပါမည်
  transportFee: number | null;
  hotelFee: number | null;
  serviceFee: number | null;
  budgetAmount: number | null;
  description: string;
  extraService: string;
  status: Status;
  packageDetails?: PackageDetail[];
  packageImageUrl: string | null;
  packageImage: string | null;
  busTypeName:string;
  busTypeId:number|null;
  busName:string;
  busId:number|null;
}

export const PackageModel = (): Package => ({ 
  id: undefined, 
  name: '',
  destinationId: null,
  busTypeId:null,
  busTypeName:'',
  busId:null,
  busName:'',
  destinationName: '',
  hotelId: null,
  hotelName: '',
  transportId: null,
  transportName: '',
  durationId: null,
  durationName: '',
  departureDate: null, 
  transportFee: null,
  hotelFee: null,
  serviceFee: null,
  budgetAmount: null,
  description: '',
  extraService: '',
  status: Status.ACTIVE,
  packageDetails: [],
  packageImageUrl:'',
  packageImage:''
});
 export interface PackageDetail extends CommonDto {
  placeToVisit: string;
  imageUrl: string | null;
  imageFullUrl: string | null;
}