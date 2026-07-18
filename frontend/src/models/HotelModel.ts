import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface HotelListParams {
  [key: string]: any;
  name: string;
  status: Status | string;
}

export const HotelListParamsModel = (): HotelListParams => ({
  name: '',
  status: '',
});

export interface Hotel extends CommonDto {
  destinationId: number | null;
  hotelId:number|null;
  hotelName:string;
  destinationName?: string;
  address: string;
  image:string;
  imageUrl?: string | null;
  description: string;
  pricePerNight: number | null;
  status: Status | string;
}

export const HotelModel = (): Hotel => ({
  destinationId: null,
  hotelId:null,
  hotelName:'',
  address: '',
  image: '',
  imageUrl: null,
  description: '',
  pricePerNight: null,
  status: Status.ACTIVE,
});