import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface BookingListParams {
  [key: string]: any;
  packageId: number| null;
  customerId: number| null;
  paymentMethodId: number| null;
  travelersQty: number| null;
  status: Status| string;
  note: string;
  packageName: string;
  customerName: string;
  paymentMethodName: string;
  totalAmount: number| null;
  departureDate: string;

  
}

export const BookingListParamsModel = (): BookingListParams => ({
  packageId: null,
  customerId: null,
  paymentMethodId: null,
  travelersQty: null,
  status:'',
  note:'',
  packageName:'',
  customerName:'',
  paymentMethodName:'',
  totalAmount: null,
  departureDate:'',

});

export interface Booking extends CommonDto {
  packageId: number| null;
  customerId: number| null;
  paymentMethodId: number| null;
  paymentReceiveImage: string;
  paymentReceiveImageUrl: string;
  travelersQty: number| null;
  status: Status| string;
  note: string;
  packageName: string;
  customerName: string;
  paymentMethodName: string;
  totalAmount: number| null;
  departureDate: string;
}

export const BookingModel = (): Booking => ({
   packageId: null,
  customerId: null,
   paymentReceiveImage: '',
  paymentReceiveImageUrl: '',
  paymentMethodId: null,
  travelersQty: null,
  status:'',
  note:'',
  packageName:'',
  customerName:'',
  paymentMethodName:'',
  totalAmount: null,
  departureDate:'',


});
