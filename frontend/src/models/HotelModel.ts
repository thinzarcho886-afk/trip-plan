import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface HotelListParams {
  [key: string]: any;
  name: string;
  hotelImage: string;
  hotelImageUrl: string;
  destinationId: number | null;
  status: Status | string;
}

export const HotelListParamsModel = (): HotelListParams => ({
  name: '',
  hotelImage:'',
  hotelImageUrl: '',
  destinationId: null,
  status: '',
});

export interface Hotel extends CommonDto {
  name: string;
  destinationId: number | null;
  destinationName?: string;
  address: string;
  imageUrl?: string | null;
  description: string;
  pricePerNight: number | null;
  status: Status | string;
}

export const HotelModel = (): Hotel => ({
  name: '',
  destinationId: null,
  address: '',
  imageUrl: null,
  description: '',
  pricePerNight: null,
  status: Status.ACTIVE,
});